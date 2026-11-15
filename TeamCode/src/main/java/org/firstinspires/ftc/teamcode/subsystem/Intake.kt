package org.firstinspires.ftc.teamcode.subsystem

import com.pedropathing.ivy.Command
import com.pedropathing.ivy.commands.Commands.instant
import com.pedropathing.ivy.commands.Commands.waitMs
import com.pedropathing.ivy.groups.Groups.sequential
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap

class Intake(hardwareMap: HardwareMap) {
    private val intakeMotor: DcMotorEx = hardwareMap.get(DcMotorEx::class.java, "intake_motor")

    val setIntakePower = {power: Double -> instant { intakeMotor.power = power }
        .requiring(intakeMotor)
    }

//    Runs the intake only while ready() is true, so balls are fed only when the flywheel is at speed.
//    Never finishes on its own; race it against a wait to bound it.
    val feedWhen = {ready: () -> Boolean -> Command.build()
        .setExecute { intakeMotor.power = if (ready()) 1.0 else 0.0 }
        .requiring(intakeMotor)
    }

    val intakeTransferRoutine: Command = sequential(
        setIntakePower(1.0),
        waitMs(110.0),
        setIntakePower(0.0),
        waitMs(400.0)
    )
}