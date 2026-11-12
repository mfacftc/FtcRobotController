package org.firstinspires.ftc.teamcode.opmode

import com.pedropathing.ivy.Scheduler
import com.pedropathing.ivy.Scheduler.schedule
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.Poses
import org.firstinspires.ftc.teamcode.subsystem.Drivebase
import org.firstinspires.ftc.teamcode.subsystem.Intake
import org.firstinspires.ftc.teamcode.subsystem.Shooter
import org.firstinspires.ftc.teamcode.subsystem.Sweepers


@TeleOp
class ManualTeleOp : OpMode() {
//    Declare subsystems
    lateinit var drivebase: Drivebase
    lateinit var intake: Intake
    lateinit var sweepers: Sweepers
    lateinit var shooter: Shooter

    private var n = 0
// in the block:


//    Initialize subsystems
    override fun init() {
        drivebase = Drivebase(hardwareMap = hardwareMap)
        intake = Intake(hardwareMap = hardwareMap)
        sweepers = Sweepers(hardwareMap = hardwareMap)
        shooter = Shooter(hardwareMap = hardwareMap)
    }

    override fun loop() {
//        Schedule commands
//        Drivebase
        schedule(drivebase.setDriveDirection(
            -gamepad1.left_stick_y.toDouble(),
            -gamepad1.left_stick_x.toDouble(),
            -gamepad1.right_stick_x.toDouble())
        )

        if (gamepad1.optionsWasPressed()) {
            schedule(drivebase.toggleFieldCentric)
        }

        if (gamepad1.yWasPressed()) {
            schedule(drivebase.moveToPosition(Poses.shootingPose1))
        }

//        Intake
        if (gamepad1.rightTriggerWasPressed()) {
            schedule(intake.setIntakePower(1.0))
        } else if (gamepad1.leftTriggerWasPressed()) {
            schedule(intake.setIntakePower(-1.0))
        } else if (gamepad1.rightTriggerWasReleased() or gamepad1.leftTriggerWasReleased()) {
            schedule(intake.setIntakePower(0.0))
        }

//        Sweepers
        if (gamepad1.rightBumperWasPressed()) {
            schedule(sweepers.setCornerSweepPower(1.0))
        } else if (gamepad1.rightBumperWasReleased()) {
            schedule(sweepers.setCornerSweepPower(0.0))
        }

        if (gamepad1.left_bumper and !Scheduler.isScheduled(sweepers.flowerSweepRoutine)) {
            schedule(sweepers.flowerSweepRoutine)
        }

//        Shooter
        if (gamepad1.aWasPressed()) {
            schedule(shooter.setFlywheelPower(0.65))
        }

        if (gamepad1.dpadUpWasPressed()) {
            schedule(shooter.setFlywheelPower(shooter.getFlywheelPower()+0.02))
        } else if (gamepad1.dpadDownWasPressed()) {
            schedule(shooter.setFlywheelPower(shooter.getFlywheelPower()-0.02))
        }

        telemetry.isAutoClear = true
        telemetry.addData("n", n++)
        telemetry.addData("Flywheel Speed",shooter.getFlywheelSpeed())
        telemetry.addData("Flywheel Power", shooter.getFlywheelPower())
        telemetry.addData("Intake Power", intake.getIntakePower())
//        telemetry.addData("Other", Scheduler.isScheduled(sweepers.flowerSweepRoutine))
//        telemetry.addData("Button1", gamepad1.left_bumper)
        telemetry.update()

        Scheduler.execute()
    }
}