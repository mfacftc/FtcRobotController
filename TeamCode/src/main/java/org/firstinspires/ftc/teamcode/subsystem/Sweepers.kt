package org.firstinspires.ftc.teamcode.subsystem

import com.pedropathing.ivy.Command
import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.HardwareMap

class Sweepers(hardwareMap: HardwareMap) {
    private val leftSweepServo: CRServo = hardwareMap.get(CRServo::class.java, "left_sweep_servo")
    private val rightSweepServo: CRServo = hardwareMap.get(CRServo::class.java, "right_sweep_servo")

    val setSweepersPower = {power: Double -> Command.build()
        .setExecute {
            leftSweepServo.power = power
            rightSweepServo.power = -power
        }
        .requiring(leftSweepServo, rightSweepServo)
    }

    val getSweepersPower = {leftSweepServo.power}
}