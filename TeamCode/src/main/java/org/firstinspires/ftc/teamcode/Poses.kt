package org.firstinspires.ftc.teamcode

import com.pedropathing.api.PoseFactory
import com.pedropathing.math.Pose

object Poses {
    val p: PoseFactory = PoseFactory.degrees()

    val startPose: Pose = p.of(32.0, robotCenterY, 90.0)
    val shootingPose: Pose = p.of(34.0,18.3,48.0)
//    x of the line through the hive that the shooting pose is mirrored across to shoot from its other side
    const val hiveMirrorX: Double = 58.0
    val farShootingPose: Pose = mirrorPoseX(shootingPose, hiveMirrorX)
//    Both sides of both hives; teleop drives to whichever is closest
    val shootingPoses: List<Pose> = listOf(shootingPose, farShootingPose).flatMap { listOf(it, invertPose(it)) }
    val flowerSweepPose: Pose = p.of(14.0, 35.0, 95.0)
    val flowerPokePose: Pose = p.of(14.0, 36.0, 85.0)

    val parkPose: Pose = p.of(11.2, 95.5, 110.0)
    val parkControlPose: Pose = p.of(40.5, 141.2, 100.0)
    val bananaParkControlPose: Pose = p.of(28.5, 65.0, 125.0)
    val bananaParkPose: Pose = p.of(11.5, 110.0, 180.0)


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

    var autonomousEndPose: Pose = startPose
}