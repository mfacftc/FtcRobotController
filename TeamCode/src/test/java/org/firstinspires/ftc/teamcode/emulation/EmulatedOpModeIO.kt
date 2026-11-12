package org.firstinspires.ftc.teamcode.emulation

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.robocol.TelemetryMessage
import emulator.input.GamepadSnapshot
import org.firstinspires.ftc.robotcore.internal.opmode.TelemetryImpl

/**
 * Copies an emulator gamepad snapshot onto an SDK [Gamepad]. Goes through [Gamepad.copy] (rather
 * than setting fields directly) so the SDK's own edge detection behind aWasPressed() etc. runs.
 */
fun copyGamepadSnapshotOnto(target: Gamepad, snapshot: GamepadSnapshot) {
    val source = Gamepad().apply {
        left_stick_x = snapshot.leftStickX
        left_stick_y = snapshot.leftStickY
        right_stick_x = snapshot.rightStickX
        right_stick_y = snapshot.rightStickY
        left_trigger = snapshot.leftTrigger
        right_trigger = snapshot.rightTrigger
        a = snapshot.a
        b = snapshot.b
        x = snapshot.x
        y = snapshot.y
        left_bumper = snapshot.leftBumper
        right_bumper = snapshot.rightBumper
        dpad_up = snapshot.dpadUp
        dpad_down = snapshot.dpadDown
        dpad_left = snapshot.dpadLeft
        dpad_right = snapshot.dpadRight
        // The SDK aliases options to start (PlayStation naming), so either key triggers both.
        start = snapshot.start || snapshot.options
        back = snapshot.back
    }
    target.copy(source)
}

/**
 * The real TelemetryImpl, except update() keeps the composed lines for the emulator's telemetry
 * panel instead of sending them to a Driver Station (which would crash with no OpModeManager).
 */
class EmulatedTelemetry(opMode: OpMode) : TelemetryImpl(opMode) {
    @Volatile
    var lines: List<String> = emptyList()
        private set

    override fun tryUpdate(updateReason: UpdateReason): Boolean {
        synchronized(theLock) {
            actions.forEach { it.run() }
            saveToTransmitter(true, TelemetryMessage())
            lines = ArrayList(composedLines)
            if (updateReason == UpdateReason.USER) clearOnAdd = isAutoClear
            return true
        }
    }
}
