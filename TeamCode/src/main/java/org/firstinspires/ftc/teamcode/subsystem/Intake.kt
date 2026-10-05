package org.firstinspires.ftc.teamcode.subsystem

import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap
import org.firstinspires.ftc.teamcode.fitToRange

class Intake {
    val intakeMotor: DcMotor = hardwareMap.get(DcMotor::class.java, "intake_motor")

    fun update(power: Double) {
        intakeMotor.power = fitToRange(power)
    }
}