package org.firstinspires.ftc.teamcode.emulation

import com.qualcomm.robotcore.hardware.CRServoImplEx
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorController
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareDevice
import com.qualcomm.robotcore.hardware.PIDCoefficients
import com.qualcomm.robotcore.hardware.PIDFCoefficients
import com.qualcomm.robotcore.hardware.PwmControl
import com.qualcomm.robotcore.hardware.ServoController
import com.qualcomm.robotcore.hardware.ServoControllerEx
import com.qualcomm.robotcore.hardware.ServoImplEx
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType
import com.qualcomm.robotcore.hardware.configuration.typecontainers.ServoConfigurationType
import emulator.hardware.Direction
import emulator.hardware.RunMode
import emulator.hardware.SimMotor
import emulator.hardware.SimServo
import emulator.hardware.ZeroPowerBehavior
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit
import kotlin.math.PI

/**
 * [mountedReversed] models a motor that is physically mounted mirrored (e.g. the left side of a
 * drivetrain): the sim treats positive encoder velocity as "wheel forward", so a motor your code
 * sets to REVERSE to drive forward needs to be flagged here or the simulated robot spins in place.
 */
class EmulatedDcMotorEx(
    private val sim: SimMotor,
    private val portNumber: Int = sim.port.index,
    private val mountedReversed: Boolean = false
) : DcMotorEx {
    private var direction = DcMotorSimple.Direction.FORWARD
    private var targetPositionTolerance = 10
    private var pidf = PIDFCoefficients(10.0, 3.0, 0.0, 0.0)

    private val maxTicksPerSec get() = sim.maxRpm / 60.0 * sim.ticksPerRev
    private val sign get() = if (mountedReversed) -1 else 1

    override fun setPower(power: Double) = sim.setPower(power)
    override fun getPower(): Double = sim.getPower()
    override fun getCurrentPosition(): Int = sign * sim.getCurrentPosition()
    override fun getVelocity(): Double = sign * sim.getVelocity()
    override fun getVelocity(unit: AngleUnit): Double = unit.fromRadians(getVelocity() / sim.ticksPerRev * 2 * PI)
    override fun setVelocity(angularRate: Double) = sim.setPower(angularRate / maxTicksPerSec)
    override fun setVelocity(angularRate: Double, unit: AngleUnit) =
        setVelocity(unit.toRadians(angularRate) / (2 * PI) * sim.ticksPerRev)
    override fun setTargetPosition(position: Int) { sim.targetPosition = sign * position }
    override fun getTargetPosition(): Int = sign * sim.targetPosition
    override fun isBusy(): Boolean =
        sim.mode == RunMode.RUN_TO_POSITION && kotlin.math.abs(sim.targetPosition - sim.getCurrentPosition()) > targetPositionTolerance

    // sim.mode/direction/zeroPowerBehavior are emulator.hardware enums named to match the SDK's own
    // enums member-for-member, so converting between them is just valueOf(name).
    override fun setMode(mode: DcMotor.RunMode) { sim.mode = RunMode.valueOf(mode.name) }
    override fun getMode(): DcMotor.RunMode = DcMotor.RunMode.valueOf(sim.mode.name)
    override fun setDirection(direction: DcMotorSimple.Direction) {
        this.direction = direction
        val reversed = (direction == DcMotorSimple.Direction.REVERSE) != mountedReversed
        sim.direction = if (reversed) Direction.REVERSE else Direction.FORWARD
    }
    override fun getDirection(): DcMotorSimple.Direction = direction
    override fun setZeroPowerBehavior(behavior: DcMotor.ZeroPowerBehavior) {
        // UNKNOWN has no sim equivalent; leave the sim as it was.
        if (behavior != DcMotor.ZeroPowerBehavior.UNKNOWN) sim.zeroPowerBehavior = ZeroPowerBehavior.valueOf(behavior.name)
    }
    override fun getZeroPowerBehavior(): DcMotor.ZeroPowerBehavior = DcMotor.ZeroPowerBehavior.valueOf(sim.zeroPowerBehavior.name)
    @Deprecated("Deprecated in SDK")
    override fun setPowerFloat() { sim.zeroPowerBehavior = ZeroPowerBehavior.FLOAT; sim.setPower(0.0) }
    override fun getPowerFloat(): Boolean = sim.zeroPowerBehavior == ZeroPowerBehavior.FLOAT && sim.getPower() == 0.0

    override fun getCurrent(unit: CurrentUnit): Double =
        if (unit == CurrentUnit.MILLIAMPS) sim.currentDrawAmps() * 1000 else sim.currentDrawAmps()
    override fun getCurrentAlert(unit: CurrentUnit): Double = 0.0
    override fun setCurrentAlert(current: Double, unit: CurrentUnit) {}
    override fun isOverCurrent(): Boolean = false

    // Everything below isn't modeled by SimMotor: accept writes, return sane defaults.
    override fun setMotorEnable() {}
    override fun setMotorDisable() {}
    override fun isMotorEnabled(): Boolean = true
    @Deprecated("Deprecated in SDK")
    override fun setPIDCoefficients(mode: DcMotor.RunMode, pidCoefficients: PIDCoefficients) {}
    @Deprecated("Deprecated in SDK")
    override fun getPIDCoefficients(mode: DcMotor.RunMode): PIDCoefficients = PIDCoefficients(pidf.p, pidf.i, pidf.d)
    override fun setPIDFCoefficients(mode: DcMotor.RunMode, pidf: PIDFCoefficients) { this.pidf = pidf }
    override fun getPIDFCoefficients(mode: DcMotor.RunMode): PIDFCoefficients = pidf
    override fun setVelocityPIDFCoefficients(p: Double, i: Double, d: Double, f: Double) { pidf = PIDFCoefficients(p, i, d, f) }
    override fun setPositionPIDFCoefficients(p: Double) {}
    override fun setTargetPositionTolerance(tolerance: Int) { targetPositionTolerance = tolerance }
    override fun getTargetPositionTolerance(): Int = targetPositionTolerance
    override fun getMotorType(): MotorConfigurationType = MotorConfigurationType.getUnspecifiedMotorType()
    override fun setMotorType(motorType: MotorConfigurationType) {}
    override fun getController(): DcMotorController? = null
    override fun getPortNumber(): Int = portNumber

    override fun getManufacturer(): HardwareDevice.Manufacturer = HardwareDevice.Manufacturer.Unknown
    override fun getDeviceName(): String = sim.name
    override fun getConnectionInfo(): String = "emulated"
    override fun getVersion(): Int = 1
    override fun resetDeviceConfigurationForOpMode() {}
    override fun close() {}
}

class EmulatedServoController(private val sim: SimServo) : ServoControllerEx {
    override fun setServoPosition(servo: Int, position: Double) = sim.setPosition(position)
    override fun getServoPosition(servo: Int): Double = sim.getPosition()
    override fun forgetLastKnownPosition(servo: Int) {}
    override fun setServoPwmRange(servo: Int, range: PwmControl.PwmRange) {}
    override fun getServoPwmRange(servo: Int): PwmControl.PwmRange = PwmControl.PwmRange.defaultRange
    override fun setServoPwmEnable(servo: Int) {}
    override fun setServoPwmDisable(servo: Int) {}
    override fun isServoPwmEnabled(servo: Int): Boolean = true
    override fun setServoType(servo: Int, type: ServoConfigurationType) {}
    override fun setPulseWidth(servo: Int, usWidth: Double) {}
    override fun getPulseWidth(servo: Int): Double = 1500.0
    override fun pwmEnable() {}
    override fun pwmDisable() {}
    override fun getPwmStatus(): ServoController.PwmStatus = ServoController.PwmStatus.ENABLED
    override fun getManufacturer(): HardwareDevice.Manufacturer = HardwareDevice.Manufacturer.Unknown
    override fun getDeviceName(): String = "EmulatedServoController"
    override fun getConnectionInfo(): String = "emulated"
    override fun getVersion(): Int = 1
    override fun resetDeviceConfigurationForOpMode() {}
    override fun close() {}
}

fun emulatedServo(sim: SimServo): ServoImplEx =
    ServoImplEx(EmulatedServoController(sim), sim.port.index, ServoConfigurationType())

// The real CRServoImplEx maps power -1..1 onto servo position 0..1, so the port monitor shows
// pos=0.5 for stopped, 1.0 for full forward, 0.0 for full reverse.
fun emulatedCRServo(sim: SimServo): CRServoImplEx =
    CRServoImplEx(EmulatedServoController(sim), sim.port.index, ServoConfigurationType())
