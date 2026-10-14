package org.firstinspires.ftc.teamcode.subsystem

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.fitToRange

class Intake(hardwareMap: HardwareMap) {
    val intakeMotor: DcMotor = hardwareMap.get(DcMotor::class.java, "intake_motor")

    fun update(power: Double) {
        intakeMotor.power = fitToRange(power)
    }
}