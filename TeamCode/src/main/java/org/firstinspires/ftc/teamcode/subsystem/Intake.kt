package org.firstinspires.ftc.teamcode.subsystem

import com.pedropathing.ivy.Command
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap

class Intake(hardwareMap: HardwareMap) {
    private val intakeMotor: DcMotorEx = hardwareMap.get(DcMotorEx::class.java, "intake_motor")

    val setIntakePower = {power: Double -> Command.build()
        .setExecute { intakeMotor.power = power }
        .requiring(intakeMotor)
    }

    val getIntakePower = {intakeMotor.power}
}