package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import kotlin.math.abs
import kotlin.math.max

// Base class
open class DriveMode() {
    open fun updateMotorState(lx: Double, ly: Double, rx: Double = 0.0, ry: Double = 0.0) {}
}

// Mecanum drive code
class MecanumDrive(
    private val frontLeftMotor: DcMotor,
    private val backLeftMotor: DcMotor,
    private val frontRightMotor: DcMotor,
    private val backRightMotor: DcMotor
) : DriveMode() {

    init {
        frontRightMotor.direction = DcMotorSimple.Direction.REVERSE
        backRightMotor.direction = DcMotorSimple.Direction.REVERSE
    }

    override fun updateMotorState(lx: Double, ly: Double, rx: Double, ry: Double) {
        val x = -lx
        val y = ly * 1.1 // Strafing

        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio,
        // but only if at least one is out of the range [-1, 1]
        val denominator = max(abs(y) + abs(x) + abs(rx), 1.0)
        val frontLeftPower = (y + x + rx) / denominator
        val backLeftPower = (y - x + rx) / denominator
        val frontRightPower = (y - x - rx) / denominator
        val backRightPower = (y + x - rx) / denominator

        frontLeftMotor.power = frontLeftPower
        backLeftMotor.power = backLeftPower
        frontRightMotor.power = frontRightPower
        backRightMotor.power = backRightPower
    }
}