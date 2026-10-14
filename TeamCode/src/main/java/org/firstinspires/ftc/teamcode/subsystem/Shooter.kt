package org.firstinspires.ftc.teamcode.subsystem

import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.CRServoController
import org.firstinspires.ftc.teamcode.fitToRange

class Shooter(hardwareMap: HardwareMap) {
    val flywheelMotor: DcMotor = hardwareMap.get(DcMotor::class.java, "flywheel_motor")
    val turretServo: CRServoController = CRServoController(
        hardwareMap.get(CRServo::class.java, "turret_servo",),
        1.0
    )

    fun update(flywheelPower: Double, rotationPower: Double) {
        turretServo.update(rotationPower)
        flywheelMotor.power = fitToRange(flywheelPower)
    }
}