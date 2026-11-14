package org.firstinspires.ftc.teamcode

import com.pedropathing.api.PoseFactory
import com.pedropathing.math.Pose

object Poses {
    val p: PoseFactory = PoseFactory.degrees()

    val startPose: Pose = p.of(33.0, 8.5, 90.0)
    val shootingPose: Pose = p.of(35.0,17.5,60.0)
    val flowerSweepPose: Pose = p.of(14.0, 34.0, 90.0)
    val parkPose: Pose = p.of(11.0, 98.0, 110.0)
    val parkControlPose: Pose = p.of(28.5, 65.0, 100.0)

//    Robot center when pressed into the bottom left corner facing +y: half its width from the
//    left wall, half its length from the bottom wall (matches the 8.5 in of startPose)
    val cornerPose: Pose = p.of(8.5, 8.5, 90.0)

    var autonomousEndPose: Pose = p.of(33.0, 8.5, 90.0)
}