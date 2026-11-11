package org.firstinspires.ftc.teamcode


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.Servo




@TeleOp
class TurretTestOpMode : LinearOpMode() {
    //    private lateinit var turretServo: Servo
    lateinit var leftSweepServo: Servo
    lateinit var rightSweepServo: CRServo
    lateinit var testMotor: DcMotor


    override fun runOpMode() {
//        turretServo = hardwareMap.get(Servo::class.java, "test_servo")
        leftSweepServo = hardwareMap.get(Servo::class.java, "left_servo")
        rightSweepServo = hardwareMap.get(CRServo::class.java, "right_sweeper")
        testMotor = hardwareMap.get(DcMotor::class.java, "rear_motor")


        waitForStart()


        while (opModeIsActive()) {
//            if (gamepad1.a) {
//                turretServo.position += 1.0
//            } else if (gamepad1.b) {
//                turretServo.position -= 1.0
//            }
            if (gamepad1.right_stick_y < -0.5){
                leftSweepServo.position += 1.0
            } else if (gamepad1.right_stick_y < 0.5){
                leftSweepServo.position -= 1.0
            }
            if (gamepad1.dpad_down) {
                rightSweepServo.power = 1.0
            } else if (gamepad1.dpad_up) {
                rightSweepServo.power = -1.0
            } else {
                rightSweepServo.power = 0.0
            }


            testMotor.power = gamepad1.left_stick_y.toDouble()


//            telemetry.addData("Servo Position", turretServo.position)
            telemetry.addData("Sweeper Power", rightSweepServo.power
            )
            telemetry.update()
        }
    }
}


// useful: adb connect 192.168.43.1:5555
//export PATH=$PATH:~/Library/Android/sdk/platform-tools
