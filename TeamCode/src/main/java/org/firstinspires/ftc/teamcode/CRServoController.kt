package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.hardware.CRServo

class CRServoController(private val servo: CRServo, private val rotationTime: Double) {
    var rotationChange: Double = 0.0

    fun update(power: Double) {
        servo.power = fitToRange(power)
    }
}