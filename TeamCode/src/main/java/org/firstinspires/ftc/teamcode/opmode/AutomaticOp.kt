package org.firstinspires.ftc.teamcode.opmode

import com.pedropathing.api.Paths.curve
import com.pedropathing.api.Paths.line
import com.pedropathing.follower.Follower
import com.pedropathing.ivy.Command
import com.pedropathing.ivy.Scheduler
import com.pedropathing.ivy.Scheduler.schedule
import com.pedropathing.ivy.commands.Commands.waitMs
import com.pedropathing.ivy.groups.Groups
import com.pedropathing.ivy.groups.Groups.sequential
import com.pedropathing.ivy.pedro.PedroCommands.follow
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.teamcode.Poses
import org.firstinspires.ftc.teamcode.Poses.bananaParkControlPose
import org.firstinspires.ftc.teamcode.Poses.bananaParkPose
import org.firstinspires.ftc.teamcode.Poses.flowerPokePose
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


//    Shared setup for every autonomous; each subclass only supplies its routine
abstract class AutomaticOp: OpMode() {
    protected lateinit var follower: Follower

    protected lateinit var intake: Intake
    protected lateinit var sweepers: Sweepers
    protected lateinit var shooter: Shooter

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

        schedule(routine())
    }

//    The command sequence run once the match starts
    abstract fun routine(): Command

    override fun loop() {
        follower.update()
        Scheduler.execute()
        shooter.update()

        telemetry.addData("Flywheel Speed", shooter.getFlywheelSpeed())
        telemetry.addData("Flywheel Ready", shooter.shooterReady)
        telemetry.update()
    }

//    Hand the final pose to teleop so it starts out knowing where the robot is
    override fun stop() {
        Poses.autonomousEndPose = follower.pose()
    }
}

@Autonomous(name = "Auto: Shoot + Flowers")
class ShootFlowersAuto: AutomaticOp() {
    override fun routine(): Command {
        return sequential(
//            Spin up while driving, then wait at the shooting pose only for whatever spin-up is left
            shooter.startFlywheel(0.52),
            follow(follower, line(follower.pose(), shootingPose).linear(follower.pose(), shootingPose)),
            shooter.setFlywheelPower(0.52),
            intake.feedWhen { shooter.shooterReady }.raceWith(waitMs(3000.0)),
//            Poke flowers
            shooter.stopFlywheel,
            intake.setIntakePower(1.0),
            sweepers.setCornerSweepPower(1.0),
            follow(follower, line(shootingPose, flowerSweepPose).linear(shootingPose, flowerSweepPose)),
            sweepers.setFlowerSweepPosition(0.3),
            waitMs(600.0),
            Groups.repeat(sequential(
                follow(follower, line(flowerSweepPose, flowerPokePose).linear(flowerSweepPose, flowerPokePose)),
                follow(follower, line(flowerPokePose, flowerSweepPose).linear(flowerPokePose, flowerSweepPose))
            ), 4),
//            Park
            follow(follower, curve(flowerSweepPose, parkControlPose, parkPose).linear(flowerSweepPose, parkPose)),
            intake.setIntakePower(0.0),
            sweepers.setCornerSweepPower(0.0)
        )
    }
}

@Autonomous(name = "Auto: Park")
class ParkAuto: AutomaticOp() {
    override fun routine(): Command {
        return sequential(
            waitMs(23000.0),
            follow(follower, curve(startPose, parkControlPose, parkPose).linear(startPose, parkPose))
        )
    }
}

@Autonomous(name = "Auto: Kings")
class KingsAuto: AutomaticOp() {
    override fun routine(): Command {
        return sequential(
//            Spin up while driving, then wait at the shooting pose only for whatever spin-up is left
            shooter.startFlywheel(0.525),
            follow(follower, line(follower.pose(), shootingPose).linear(follower.pose(), shootingPose)),
            shooter.setFlywheelPower(0.516),
            intake.feedWhen { shooter.shooterReady }.raceWith(waitMs(3000.0)),
//            Poke flowers
            shooter.stopFlywheel,
            intake.setIntakePower(1.0),
            sweepers.setCornerSweepPower(1.0),
//            Park
            follow(follower, curve(shootingPose, parkControlPose, parkPose).linear(shootingPose, parkPose)),
            intake.setIntakePower(0.0),
            sweepers.setCornerSweepPower(0.0))
    }
}

@Autonomous(name = "Auto: Bananas")
class BananaAuto: AutomaticOp() {
    override fun routine(): Command {
        return sequential(
//            Spin up while driving, then wait at the shooting pose only for whatever spin-up is left
            shooter.startFlywheel(0.525),
            follow(follower, line(follower.pose(), shootingPose).linear(follower.pose(), shootingPose)),
            shooter.setFlywheelPower(0.516),
            intake.feedWhen { shooter.shooterReady }.raceWith(waitMs(3000.0)),
//            Poke flowers
            shooter.stopFlywheel,
            intake.setIntakePower(1.0),
            sweepers.setCornerSweepPower(1.0),
//            Park
            follow(follower, curve(shootingPose, bananaParkControlPose, bananaParkPose).linear(shootingPose, bananaParkPose)),
            intake.setIntakePower(0.0),
            sweepers.setCornerSweepPower(0.0))
    }
}
