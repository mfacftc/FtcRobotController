package org.firstinspires.ftc.teamcode.subsystem

import com.pedropathing.api.Paths.line
import com.pedropathing.follower.Follower
import com.pedropathing.follower.ManualDrive
import com.pedropathing.ivy.Command
import com.pedropathing.ivy.commands.Commands.instant
import com.pedropathing.ivy.pedro.PedroCommands.follow
import com.pedropathing.math.Pose
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.Poses
import org.firstinspires.ftc.teamcode.invertPose
import org.firstinspires.ftc.teamcode.pedro.Constants

class Drivebase(hardwareMap: HardwareMap) {
    private val follower: Follower = Constants.create(hardwareMap)
    var fieldCentricDriving: Boolean = false

    init {
        follower.setPose(Poses.autonomousEndPose)
    }

    fun pose(): Pose = follower.pose()

    fun setPose(pose: Pose) {
        follower.setPose(pose)
    }

    val invertIfNeccessary = { pose: Pose ->
        if (follower.pose().y() > 72.0) {
            invertPose(pose)
        } else {
            pose
        }
    }

    val moveToPosition = {pose: Pose ->
        follow(follower, line(follower.pose(), pose).linear(follower.pose(), pose))
    }
    val setDriveDirection = {forward: Double, lateral: Double, turn: Double -> Command.build()
        .setExecute {
            if (fieldCentricDriving) {
                val powers = ManualDrive.fieldCentric(forward, lateral, turn, follower.pose().heading())
                ManualDrive.driveOrHold(follower, powers)
            } else {
                ManualDrive.driveOrHold(follower, forward, lateral, turn)
            }
            follower.update()
        }
        .requiring(follower)
    }

    val toggleFieldCentric: Command = instant { fieldCentricDriving = !fieldCentricDriving }
        .requiring(fieldCentricDriving)


}