package org.firstinspires.ftc.teamcode.opmode

import com.pedropathing.api.Paths.curve
import com.pedropathing.api.Paths.line
import com.pedropathing.follower.Follower
import com.pedropathing.ivy.Scheduler
import com.pedropathing.ivy.Scheduler.schedule
import com.pedropathing.ivy.commands.Commands.waitMs
import com.pedropathing.ivy.groups.Groups
import com.pedropathing.ivy.groups.Groups.sequential
import com.pedropathing.ivy.pedro.PedroCommands.follow
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.teamcode.Poses
import org.firstinspires.ftc.teamcode.Poses.flowerSweepPose
import org.firstinspires.ftc.teamcode.Poses.parkControlPose
import org.firstinspires.ftc.teamcode.Poses.parkPose
import org.firstinspires.ftc.teamcode.Poses.shootingPose
import org.firstinspires.ftc.teamcode.Poses.startPose
import org.firstinspires.ftc.teamcode.invertPose
import org.firstinspires.ftc.teamcode.pedro.Constants
import org.firstinspires.ftc.teamcode.subsystem.Intake
import org.firstinspires.ftc.teamcode.subsystem.Shooter
import org.firstinspires.ftc.teamcode.subsystem.Sweepers


@Autonomous
class AutomaticOp: OpMode() {
    private lateinit var follower: Follower

    private lateinit var intake: Intake
    private lateinit var sweepers: Sweepers
    private lateinit var shooter: Shooter

    private var inverted: Boolean = false

    override fun init() {
        Scheduler.reset();

        follower = Constants.create(hardwareMap)
        follower.setPose(startPose)
        follower.update()

        intake = Intake(hardwareMap = hardwareMap)
        sweepers = Sweepers(hardwareMap = hardwareMap)
        shooter = Shooter(hardwareMap = hardwareMap)
    }

//    Press X during init to toggle starting from the mirrored start pose
    override fun init_loop() {
        if (gamepad1.xWasPressed()) {
            inverted = !inverted
        }
        telemetry.addData("Side", if (inverted) "Inverted" else "Normal")
        telemetry.update()
    }

    override fun start() {
        follower.setPose(if (inverted) invertPose(startPose) else startPose)
        follower.update()

        schedule(sequential(
            follow(follower, line(follower.pose(), shootingPose).linear(follower.pose(), shootingPose)),
            shooter.setFlywheelPower(0.52),
            intake.setIntakePower(1.0),
            waitMs(3000.0),
            shooter.stopFlywheel,
            sweepers.setCornerSweepPower(1.0),
            follow(follower, line(shootingPose, flowerSweepPose).linear(shootingPose, flowerSweepPose)),
            Groups.repeat(sweepers.flowerSweepRoutine, 4),
            follow(follower, curve(flowerSweepPose, parkControlPose, parkPose).linear(flowerSweepPose, parkPose)),
            intake.setIntakePower(0.0),
            sweepers.setCornerSweepPower(0.0)
        ))
    }

    override fun loop() {
        follower.update()
        Scheduler.execute()
        shooter.update()
    }

//    Hand the final pose to teleop so it starts out knowing where the robot is
    override fun stop() {
        Poses.autonomousEndPose = follower.pose()
    }
}
