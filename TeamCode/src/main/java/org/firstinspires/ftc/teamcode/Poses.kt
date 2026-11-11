package org.firstinspires.ftc.teamcode

import com.pedropathing.api.PoseFactory
import com.pedropathing.math.Pose

object Poses {
    val p: PoseFactory = PoseFactory.degrees()

    var autonomousEndPose: Pose = p.of(33.0, 8.5, 90.0)

    val shootingPose1: Pose = p.of(19.0,16.0,50.0)
}
