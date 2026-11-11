package org.firstinspires.ftc.teamcode.subsystem

import com.pedropathing.ivy.Command
import com.pedropathing.ivy.commands.Commands.waitMs
import com.pedropathing.ivy.groups.Groups.sequential
import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo


class Sweepers(hardwareMap: HardwareMap) {
    private val cornerSweepServo: CRServo = hardwareMap.get(CRServo::class.java, "corner_sweep_servo")
    private val flowerSweepServo: Servo = hardwareMap.get(Servo::class.java, "flower_sweep_servo")

    val setCornerSweepPower = {power: Double -> Command.build()
        .setExecute {
            cornerSweepServo.power = power
        }
        .requiring(cornerSweepServo)
    }

    private val setFlowerSweepPosition = {position: Double -> Command.build()
        .setExecute {
            flowerSweepServo.position = position
        }
        .requiring(flowerSweepServo)
    }
     val flowerSweepRoutine: Command = sequential(
    setFlowerSweepPosition(0.25),
        waitMs(250.0),
        setFlowerSweepPosition(0.0),
        waitMs(250.0)
     )
}