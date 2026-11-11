package org.firstinspires.ftc.teamcode.opmode

import com.pedropathing.ivy.Scheduler
import com.pedropathing.ivy.Scheduler.schedule
import com.pedropathing.ivy.groups.Groups.sequential
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.subsystem.Drivebase
import org.firstinspires.ftc.teamcode.subsystem.Intake
import org.firstinspires.ftc.teamcode.subsystem.Shooter
import org.firstinspires.ftc.teamcode.subsystem.Sweepers
import kotlin.math.abs


@TeleOp
class ManualTeleOp : OpMode() {
//    Declare subsystems
    lateinit var drivebase: Drivebase
    lateinit var intake: Intake
    lateinit var sweepers: Sweepers
    lateinit var shooter: Shooter

//    Initialize subsystems
    override fun init() {
        drivebase = Drivebase(hardwareMap = hardwareMap)
        intake = Intake(hardwareMap = hardwareMap)
        sweepers = Sweepers(hardwareMap = hardwareMap)
        shooter = Shooter(hardwareMap = hardwareMap)
    }

    override fun loop() {
//        Schedule commands
        schedule(drivebase.setDriveDirection(
            -gamepad1.left_stick_y.toDouble(),
            -gamepad1.left_stick_x.toDouble(),
            -gamepad1.right_stick_x.toDouble())
        )

        if (gamepad1.optionsWasPressed()) {
            schedule(drivebase.toggleFieldCentric)
        }

        if (gamepad1.leftBumperWasPressed()) {
            schedule(sweepers.setRightSweepersPower(
                abs(sweepers.getSweepersPower()-1)
            ))
        }
        if (gamepad1.rightBumperWasPressed()) {
            schedule(sweepers.setLeftSweepersPower(
                abs(sweepers.getSweepersPower()-1)
            ))
        }

        if (gamepad1.rightTriggerWasPressed()) {
            schedule(intake.setIntakePower(
                abs(intake.getIntakePower()-1)
            ))
        } else if (gamepad1.left_trigger > 0.5) {
            schedule(intake.setIntakePower(-1.0))
        } else if (gamepad1.leftTriggerWasReleased()) {
            schedule(intake.setIntakePower(0.0))
        }

        if (gamepad1.rightBumperWasPressed()) {
            schedule(sequential(
                shooter.setFlywheel(true),
                intake.setIntakePower(1.0)
            ))
        }

        Scheduler.execute()
    }
}