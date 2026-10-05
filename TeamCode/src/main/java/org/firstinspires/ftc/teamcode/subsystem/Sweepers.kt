package org.firstinspires.ftc.teamcode.subsystem

import com.qualcomm.robotcore.hardware.CRServo
import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap
import org.firstinspires.ftc.teamcode.fitToRange

class Sweepers {
    val leftSweepServo: CRServo = hardwareMap.get(CRServo::class.java, "left_sweep_servo")
    val rightSweepServo: CRServo = hardwareMap.get(CRServo::class.java, "right_sweep_servo")

    fun update(leftPower: Double, rightPower: Double) {
        leftSweepServo.power = fitToRange(leftPower)
        rightSweepServo.power = fitToRange(rightPower)
    }
}