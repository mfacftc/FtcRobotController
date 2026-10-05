package org.firstinspires.ftc.teamcode.subsystem

import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap
import org.firstinspires.ftc.teamcode.CRServoController

class Shooter {
    val flywheelMotor: DcMotor = hardwareMap.get(DcMotor::class.java, "flywheel_motor")
    val turretServo: CRServoController = CRServoController(
        hardwareMap.get(CRServo::class.java, "turret_servo",),
        1.0
    )

    fun update(flywheelPower: Double, rotationPower: Double) {
        turretServo.update(rotationPower)
    }
}