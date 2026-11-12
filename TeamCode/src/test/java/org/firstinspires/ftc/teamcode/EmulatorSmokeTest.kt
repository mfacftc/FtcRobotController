package org.firstinspires.ftc.teamcode

import com.pedropathing.ivy.Scheduler
import com.qualcomm.robotcore.hardware.Gamepad
import emulator.input.GamepadSnapshot
import org.firstinspires.ftc.teamcode.emulation.EmulatedRobot
import org.firstinspires.ftc.teamcode.emulation.EmulatedTelemetry
import org.firstinspires.ftc.teamcode.emulation.copyGamepadSnapshotOnto
import org.firstinspires.ftc.teamcode.opmode.ManualTeleOp
import org.junit.Assert.assertTrue
import org.junit.Test

/** Headless version of [TestMain]: drives ManualTeleOp forward for a second, no window. */
class EmulatorSmokeTest {
    @Test
    fun manualTeleOpDrivesForward() {
        val robot = EmulatedRobot()
        Scheduler.reset()
        val opMode = ManualTeleOp().apply {
            hardwareMap = robot.buildHardwareMap()
            gamepad1 = Gamepad()
            gamepad2 = Gamepad()
            telemetry = EmulatedTelemetry(this)
            init()
        }
        val startPose = robot.drive.pose
        opMode.start()

        repeat(20) {
            copyGamepadSnapshotOnto(opMode.gamepad1, GamepadSnapshot(leftStickY = -1f))
            opMode.loop()
            robot.update(0.05)
        }

        val endPose = robot.drive.pose
        println("start=$startPose end=$endPose")
        println((opMode.telemetry as EmulatedTelemetry).lines)
        // Starts facing 90 degrees (Poses.autonomousEndPose), so forward is +y.
        assertTrue("robot should have driven forward", endPose.y - startPose.y > 200)
    }
}
