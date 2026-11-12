package org.firstinspires.ftc.teamcode

import com.pedropathing.ivy.Scheduler
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.Gamepad
import emulator.sim.Pose
import emulator.ui.runRunnerShellAndBlock
import org.firstinspires.ftc.teamcode.emulation.EmulatedRobot
import org.firstinspires.ftc.teamcode.emulation.EmulatedTelemetry
import org.firstinspires.ftc.teamcode.emulation.copyGamepadSnapshotOnto
import org.firstinspires.ftc.teamcode.opmode.ManualTeleOp
import org.junit.Test

/**
 * Runs real OpModes against the emulated robot in a desktop window. Launch with
 *   ./gradlew :TeamCode:testDebugUnitTest --tests org.firstinspires.ftc.teamcode.TestMain
 * or the green run arrow next to [runEmulator] in Android Studio. Closing the window ends the test.
 */
class TestMain {
    private val opModes: List<Pair<String, () -> OpMode>> = listOf(
        "ManualTeleOp" to ::ManualTeleOp
    )

    private enum class State { IDLE, INIT, RUNNING, STOPPED, CRASHED }

    @Test
    fun runEmulator() {
        val robot = EmulatedRobot()
        var opMode: OpMode? = null
        var emulatedTelemetry: EmulatedTelemetry? = null
        var state = State.IDLE
        var crash: Throwable? = null

        fun guarded(block: () -> Unit) {
            try {
                block()
            } catch (t: Throwable) {
                crash = t
                state = State.CRASHED
            }
        }

        runRunnerShellAndBlock(
            title = "FtcRobotController Emulator",
            opModeNames = opModes.map { it.first },
            onInit = { index ->
                crash = null
                robot.reset()
                Scheduler.reset()
                guarded {
                    opMode = opModes[index].second().apply {
                        hardwareMap = robot.buildHardwareMap()
                        gamepad1 = Gamepad() // null until the SDK's OpModeManager would set them
                        gamepad2 = Gamepad()
                        emulatedTelemetry = EmulatedTelemetry(this).also { telemetry = it }
                        init()
                    }
                    state = State.INIT
                }
            },
            onStart = { if (state == State.INIT) guarded { opMode?.start(); state = State.RUNNING } },
            onStop = {
                if (state == State.INIT || state == State.RUNNING) guarded { opMode?.stop() }
                if (state != State.CRASHED) state = State.STOPPED
                Scheduler.reset()
                robot.sim.motors.values.forEach { it.setPower(0.0) }
            },
            onResetField = { robot.drive.resetPose(Pose(0.0, 0.0, 0.0)) },
            onTick = { dt, pads ->
                val op = opMode
                if (op != null && (state == State.INIT || state == State.RUNNING)) guarded {
                    copyGamepadSnapshotOnto(op.gamepad1, pads.gamepad1)
                    copyGamepadSnapshotOnto(op.gamepad2, pads.gamepad2)
                    if (state == State.INIT) op.init_loop() else op.loop()
                }
                robot.update(dt)
            },
            poseSupplier = { robot.drive.pose },
            portRowsSupplier = { robot.sim.toPortRows() },
            telemetrySupplier = { emulatedTelemetry?.lines ?: emptyList() },
            crashSupplier = { crash },
            statusSupplier = { "State: $state" },
            batteryVoltageSupplier = { robot.battery.voltage }
        )
    }
}
