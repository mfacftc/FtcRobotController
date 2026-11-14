package org.firstinspires.ftc.teamcode.subsystem

import com.pedropathing.ivy.commands.Commands.instant
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.velocityToRpm
import kotlin.math.abs
import kotlin.math.sign

class Shooter(hardwareMap: HardwareMap) {
    private val flywheelMotor: DcMotorEx = hardwareMap.get(DcMotorEx::class.java, "flywheel_motor")
    private val flywheelMotorRpm: Double = 6000.0

//    Extra power added on top of the target while the flywheel is below speed
    private val boostPower: Double = 0.3
//    Power applied against the spin direction to slow the flywheel down when it is set to zero
    private val brakePower: Double = 0.15
//    How far below the target rpm the flywheel can be before boosting kicks in
    private val boostThresholdRpm: Double = 100.0
//    How close to the target rpm the flywheel must be to count as up to speed
    private val readyToleranceRpm: Double = 300.0
//    Speed below which braking stops, so the flywheel doesn't spin backwards
    private val stoppedRpm: Double = 300.0

    private var targetPower: Double = 0.0
    private val targetRpm get() = flywheelMotorRpm * targetPower

    val shooterActive: Boolean get() = targetPower > 0.0
    val shooterReady: Boolean get() = shooterActive && abs(getFlywheelSpeed() - targetRpm) < readyToleranceRpm

    val setFlywheelPower = {power: Double -> instant { targetPower = power }
        .setDone {
            when {
                power > 0.0 -> shooterReady
                power == 0.0 -> getFlywheelSpeed() < stoppedRpm
                else -> true
            }
        }
        .requiring(flywheelMotor)
    }

//    Sets the flywheel to stop without waiting for it to spin down; update() brakes it
    val stopFlywheel = instant { targetPower = 0.0 }
        .requiring(flywheelMotor)

//    Call every loop to boost the flywheel up to speed and brake it to a stop
    fun update() {
        val rpm = getFlywheelSpeed()
        flywheelMotor.power = when {
            targetPower > 0.0 && rpm < targetRpm - boostThresholdRpm -> (targetPower + boostPower).coerceAtMost(1.0)
            targetPower == 0.0 && rpm > stoppedRpm -> -sign(flywheelMotor.velocity) * brakePower
            else -> targetPower
        }
    }

    val getFlywheelPower = {targetPower}
    val getFlywheelSpeed = {velocityToRpm(flywheelMotor.velocity, flywheelMotorRpm)}

}
