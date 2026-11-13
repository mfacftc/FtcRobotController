package org.firstinspires.ftc.teamcode.subsystem

import com.pedropathing.ivy.commands.Commands.instant
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.velocityToRpm

class Shooter(hardwareMap: HardwareMap) {
    private val flywheelMotor: DcMotorEx = hardwareMap.get(DcMotorEx::class.java, "flywheel_motor")
    private val flywheelMotorRpm: Double = 6000.0

    var shooterReady: Boolean = false

    val setFlywheelPower = {power: Double -> instant { flywheelMotor.power = power }
        .setDone {
            val rpm = velocityToRpm(flywheelMotor.velocity, flywheelMotorRpm)
            var atSpeed = false
            if (rpm > flywheelMotorRpm * (power - 0.17) && rpm < flywheelMotorRpm * (power + 0.17)) {
                shooterReady = (power != 0.0)
                atSpeed = true
            }
            atSpeed
        }
        .requiring(flywheelMotor)
    }

    val stopFlywheel = {

    }
    val getFlywheelPower = {flywheelMotor.power}
    val getFlywheelSpeed = {velocityToRpm(flywheelMotor.velocity, flywheelMotorRpm)}

}