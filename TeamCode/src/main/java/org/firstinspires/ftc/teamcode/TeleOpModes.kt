package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp
class MecanumTeleOp : LinearOpMode() {
    @Throws(InterruptedException::class)

    override fun runOpMode() {
        val driveMode = MecanumDrive(
            hardwareMap.dcMotor.get("frontLeftMotor"),
            hardwareMap.dcMotor.get("backLeftMotor"),
            hardwareMap.dcMotor.get("frontRightMotor"),
            hardwareMap.dcMotor.get("backRightMotor"))

        waitForStart()
        if (isStopRequested) return

        while (opModeIsActive()) {
            driveMode.updateMotorState(gamepad1.left_stick_y.toDouble(),
                gamepad1.left_stick_x.toDouble(),
                gamepad1.right_stick_x.toDouble())
        }
    }
}