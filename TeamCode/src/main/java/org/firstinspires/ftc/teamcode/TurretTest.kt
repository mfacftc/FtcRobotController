package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Servo


@TeleOp
class TurretTestOpMode : LinearOpMode() {
    private lateinit var turretServo: Servo

    override fun runOpMode() {
        turretServo = hardwareMap.get(Servo::class.java, "test_servo")

        waitForStart()

        while (opModeIsActive()) {
            if (gamepad1.a) {
                turretServo.position += 1.0
            } else if (gamepad1.b) {
                turretServo.position -= 1.0
            }

            telemetry.addData("Servo Position", turretServo.position)
            telemetry.update()
        }
    }
}