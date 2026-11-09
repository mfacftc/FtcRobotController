package org.firstinspires.ftc.teamcode.subsystem

import com.pedropathing.follower.Follower
import com.pedropathing.follower.ManualDrive
import com.pedropathing.ivy.Command
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.pedro.Constants

class Drivebase(hardwareMap: HardwareMap) {
    private val follower: Follower = Constants.create(hardwareMap)
    var fieldCentricDriving: Boolean = false

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
        .requiring(follower, fieldCentricDriving)
    }

    val toggleFieldCentric: Command = Command.build()
        .setExecute { fieldCentricDriving = !fieldCentricDriving }
        .requiring(fieldCentricDriving)
}