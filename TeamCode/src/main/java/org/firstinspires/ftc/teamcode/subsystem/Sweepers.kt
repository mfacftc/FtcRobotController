package org.firstinspires.ftc.teamcode.subsystem

import com.pedropathing.ivy.Command
import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.hardware.HardwareMap

class Sweepers(hardwareMap: HardwareMap) {
    private val leftSweepServo: CRServo = hardwareMap.get(CRServo::class.java, "left_sweep_servo")
    private val rightSweepServo: Servo = hardwareMap.get(Servo::class.java, "right_sweep_servo")


    val setLeftSweepersPower = { power: Double -> Command.build()
        .setExecute {
            leftSweepServo.power = power
        }
        .requiring(leftSweepServo)
    }
    val setRightSweepersPostion = { postion: Double -> Command.build()
        .setExecute {
            rightSweepServo.position = postion
        }
        .requiring(rightSweepServo)
    }

    val getSweepersPower = {leftSweepServo.power}
    val getSweepersPostion = {rightSweepServo.position}
}