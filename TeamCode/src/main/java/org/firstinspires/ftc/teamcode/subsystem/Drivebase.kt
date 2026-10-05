package org.firstinspires.ftc.teamcode.subsystem

import com.pedropathing.follower.Follower
import com.pedropathing.follower.ManualDrive
import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap
import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry
import org.firstinspires.ftc.teamcode.pedro.Constants

class Drivebase {
    val follower: Follower = Constants.create(hardwareMap)

    var fieldCentricDriving: Boolean = false

    fun update(forward: Double, lateral: Double, turn: Double, toggleFieldCentric: Boolean) {
        if (toggleFieldCentric) {
            fieldCentricDriving = !fieldCentricDriving
        }

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
        telemetry.addData("Robot Heading", Math.toDegrees(robotPose.heading()))
    }

}