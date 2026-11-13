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

    val intakeTransferRoutine: Command = sequential(
        setIntakePower(1.0),
        waitMs(110.0),
        setIntakePower(0.0),
        waitMs(400.0)
    )
}