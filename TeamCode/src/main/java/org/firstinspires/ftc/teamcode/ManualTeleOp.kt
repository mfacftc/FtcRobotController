package org.firstinspires.ftc.teamcode

import com.pedropathing.follower.Follower
import com.pedropathing.follower.ManualDrive
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.pedro.Constants


@TeleOp
class ManualTeleOp : OpMode() {
    lateinit var follower: Follower
    lateinit var leftSweepServo: CRServo
    lateinit var intakeMotor: DcMotor
    var fieldCentricDriving: Boolean = false

    override fun init() {
        follower = Constants.create(hardwareMap)
        leftSweepServo = hardwareMap.get(CRServo::class.java, "test_servo")
        intakeMotor = hardwareMap.get(DcMotor::class.java, "intake_motor")
    }

    override fun loop() {
//        Process Buttons
        if (gamepad1.options) {
            fieldCentricDriving = !fieldCentricDriving
        }

        if (gamepad1.dpad_left) {
            leftSweepServo.power = 1.0
        } else {
            leftSweepServo.power = 0.0
        }

        if (gamepad1.right_trigger > 0.5) {
            intakeMotor.power = 2.0
        } else if (gamepad1.right_bumper) {
            intakeMotor.power = -2.0
        } else {
            intakeMotor.power = 0.0
        }

        if (gamepad1.aWasPressed()) {
            fieldCentricDriving = !fieldCentricDriving
        }

//        Driving Control
        val forward = -gamepad1.left_stick_y.toDouble()
        val lateral = -gamepad1.left_stick_x.toDouble()
        val turn = -gamepad1.right_stick_x.toDouble()

        if (fieldCentricDriving) {
            val powers = ManualDrive.fieldCentric(forward, lateral, turn, follower.pose().heading())
            ManualDrive.driveOrHold(follower, powers)
        } else {
            ManualDrive.driveOrHold(follower, forward, lateral, turn)
        }
        follower.update()

//        Driving Telemetry
        val robotPose = follower.pose()
        telemetry.addData("Robot X", robotPose.x())
        telemetry.addData("Robot Y", robotPose.y())
        telemetry.addData("Robot Heading", Math.toDegrees(robotPose.heading()));

//        Intake Telemetry
        telemetry.addData("Intake Power", intakeMotor.power)
        telemetry.addData("Servo Position", leftSweepServo.power)
        telemetry.update()
    }
}