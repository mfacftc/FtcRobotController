package org.firstinspires.ftc.teamcode.subsystem

import com.pedropathing.ivy.commands.Commands.instant
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.velocityToRpm

class Shooter(hardwareMap: HardwareMap) {
    private val flywheelMotor: DcMotorEx = hardwareMap.get(DcMotorEx::class.java, "flywheel_motor")
    private val flywheelMotorRpm: Double = 6000.0

    val setFlywheelPower = {power: Double -> instant { flywheelMotor.power = power }
        .setDone {
            val rpm = velocityToRpm(flywheelMotor.velocity, flywheelMotorRpm)
            rpm > flywheelMotorRpm * (power - 0.1) && rpm < flywheelMotorRpm * (power + 0.1)
        }
        .requiring(flywheelMotor)
    }

    val getFlywheelPower = {flywheelMotor.power}
    val getFlywheelSpeed = {velocityToRpm(flywheelMotor.velocity, flywheelMotorRpm)}

}