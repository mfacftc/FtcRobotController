package org.firstinspires.ftc.teamcode.subsystem

import com.pedropathing.ivy.Command
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.toDouble
import org.firstinspires.ftc.teamcode.velocityToRpm

class Shooter(hardwareMap: HardwareMap) {
    private val flywheelMotor: DcMotorEx = hardwareMap.get(DcMotorEx::class.java, "flywheel_motor")
    private val flywheelMotorRpm: Double = 6000.0

    val setFlywheel = { enabled: Boolean -> Command.build()
        .setExecute { flywheelMotor.power = enabled.toDouble() }
        .setDone {
            (enabled && velocityToRpm(flywheelMotor.velocity, flywheelMotorRpm) > 3000) ||
                    (!enabled && velocityToRpm(flywheelMotor.velocity, flywheelMotorRpm) < 60)
        }
        .requiring(flywheelMotor, flywheelMotorRpm)
    }

}