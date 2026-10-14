package org.firstinspires.ftc.teamcode.opmode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.subsystem.Drivebase
import org.firstinspires.ftc.teamcode.subsystem.Intake
import org.firstinspires.ftc.teamcode.subsystem.Shooter
import org.firstinspires.ftc.teamcode.subsystem.Sweepers
import org.firstinspires.ftc.teamcode.toDouble


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
//        Pass input to subsystem
        drivebase.update(
            -gamepad1.left_stick_y.toDouble(),
            -gamepad1.left_stick_x.toDouble(),
            -gamepad1.right_stick_x.toDouble(),
            gamepad1.optionsWasPressed()
        )
        intake.update(
            gamepad1.right_trigger.toDouble() - gamepad1.left_trigger.toDouble()
        )
        sweepers.update(
            gamepad1.left_bumper.toDouble(),
            gamepad1.right_bumper.toDouble()
        )
        shooter.update(
            -gamepad1.dpad_up.toDouble(),
            gamepad1.dpad_left.toDouble() - gamepad1.dpad_right.toDouble()
        )
    }
}