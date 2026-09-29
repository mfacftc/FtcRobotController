package org.firstinspires.ftc.teamcode

import com.pedropathing.follower.Follower
import com.pedropathing.follower.ManualDrive
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.pedro.Constants


@TeleOp
class ManualTeleOp : OpMode() {
    lateinit var follower: Follower
    var fieldCentricDriving: Boolean = false

    override fun init() {
        follower = Constants.create(hardwareMap)
    }

    override fun loop() {
        if (gamepad1.aWasPressed()) {
            fieldCentricDriving = !fieldCentricDriving
        }

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

        val robotPose = follower.pose()
        telemetry.addData("Robot X", robotPose.x())
        telemetry.addData("Robot Y", robotPose.y())
        telemetry.addData("Robot Heading", Math.toDegrees(robotPose.heading()));
    }
}