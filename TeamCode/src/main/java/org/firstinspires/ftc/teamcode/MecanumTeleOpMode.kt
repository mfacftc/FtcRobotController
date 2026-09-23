package org.firstinspires.ftc.teamcode

import com.bylazar.configurables.annotations.Configurable
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@Configurable
class ConfigureableValue {
    var previousValue: Double = 1.0

    @JvmField
    var setValue: Double = 1.0

    fun hasChanged(): Boolean {
        if (previousValue != setValue) {
            previousValue = setValue
            return true
        }
        return false
    }
}

@TeleOp
class MecanumTeleOp : LinearOpMode() {
    @Throws(InterruptedException::class)

    override fun runOpMode() {
        val driveMode = MecanumDrive(
            hardwareMap.dcMotor.get("FrontLeftDrive"),
            hardwareMap.dcMotor.get("BackLeftDrive"),
            hardwareMap.dcMotor.get("FrontRightDrive"),
            hardwareMap.dcMotor.get("BackRightDrive"))
        val configureableSensitivity = ConfigureableValue()

        waitForStart()
        if (isStopRequested) return

        while (opModeIsActive()) {
            if (configureableSensitivity.hasChanged()) {
                driveMode.sensitivity = configureableSensitivity.setValue
            }

            if (gamepad1.dpad_up and (driveMode.sensitivity < 1)) {
                driveMode.sensitivity += 0.002
            } else if (gamepad1.dpad_down and  (driveMode.sensitivity > 0)) {
                driveMode.sensitivity -= 0.002
            }

            driveMode.updateMotorState(gamepad1.left_stick_x.toDouble(),
                gamepad1.left_stick_y.toDouble(),
                -gamepad1.right_stick_x.toDouble())
        }
    }
}