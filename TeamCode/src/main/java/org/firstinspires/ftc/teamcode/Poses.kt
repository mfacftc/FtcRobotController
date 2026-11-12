package org.firstinspires.ftc.teamcode

import com.pedropathing.api.PoseFactory
import com.pedropathing.math.Pose

object Poses {
    val p: PoseFactory = PoseFactory.degrees()

    val startPose: Pose = p.of(33.0, 8.5, 90.0)
    val shootingPose: Pose = p.of(19.0,16.0,50.0)
    val flowerSweepPose: Pose = p.of(14.0, 34.0, 90.0)
    val parkPose: Pose = p.of(11.0, 98.0, 110.0)
    val parkControlPose: Pose = p.of(33.0, 75.0, 100.0)

    var autonomousEndPose: Pose = p.of(33.0, 8.5, 90.0)
}