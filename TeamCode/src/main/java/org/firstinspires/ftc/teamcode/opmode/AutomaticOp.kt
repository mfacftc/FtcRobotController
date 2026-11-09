package org.firstinspires.ftc.teamcode.opmode

import com.pedropathing.api.Paths.curve
import com.pedropathing.api.PoseFactory
import com.pedropathing.follower.Follower
import com.pedropathing.ivy.Scheduler
import com.pedropathing.ivy.Scheduler.schedule
import com.pedropathing.ivy.groups.Groups.sequential
import com.pedropathing.math.Pose
import com.pedropathing.paths.Path
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.teamcode.pedro.Constants


@Autonomous
class AutomaticOp: OpMode() {
    private lateinit var follower: Follower
    private val p: PoseFactory = PoseFactory.degrees()

//    Poses
    private val startPose: Pose = p.of(24.0, 24.0, 0.0)
    private val park: Pose = p.of(48.0, 48.0, 90.0)
    private val controlPose: Pose? = p.of(36.0, 60.0, 45.0)

//    Paths
    private fun park(): Path {
        return curve(startPose, controlPose, park).linear(startPose, park);
    }

    override fun init() {
        Scheduler.reset();

        follower = Constants.create(hardwareMap);
        follower.setPose(startPose);
        follower.update();
    }

    override fun start() {
        schedule(sequential());
    }

    override fun loop() {
        follower.update();
        Scheduler.execute();
    }
}
