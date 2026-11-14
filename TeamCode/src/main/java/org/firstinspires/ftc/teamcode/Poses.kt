package org.firstinspires.ftc.teamcode

import com.pedropathing.api.PoseFactory
import com.pedropathing.math.Pose

object Poses {
    val p: PoseFactory = PoseFactory.degrees()

    val startPose: Pose = p.of(33.0, robotCenterY, 90.0)
    val shootingPose: Pose = p.of(35.0,17.5,60.0)
    val flowerSweepPose: Pose = p.of(14.0, 34.0, 90.0)
    val parkPose: Pose = p.of(11.0, 98.0, 110.0)
    val parkControlPose: Pose = p.of(28.5, 65.0, 100.0)

//    Distance from the robot's center to its side (x) and to its back (y) when facing +y
    const val robotCenterX: Double = 8.0
    const val robotCenterY: Double = 9.0

//    Robot center when pressed into the bottom left corner facing +y
    val cornerPose: Pose = p.of(robotCenterX, robotCenterY, 90.0)
//    The same, pressed into the top left corner facing -y
    val topCornerPose: Pose = p.of(robotCenterX, 144.0 - robotCenterY, -90.0)

//    Inches the mirrored (positive y side) poses are pulled back toward the field center, to make
//    up for odometry reading short on that side. Set to 0 once the odometry tracks correctly.
    var invertYOffset: Double = 14.0

    var autonomousEndPose: Pose = p.of(33.0, robotCenterY, 90.0)
}