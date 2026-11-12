package org.firstinspires.ftc.teamcode.emulation

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver
import com.qualcomm.robotcore.hardware.I2cDeviceSynchSimple
import emulator.sim.MecanumRobot
import emulator.sim.Pose
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D
import org.firstinspires.ftc.robotcore.external.navigation.UnnormalizedAngleUnit
import java.lang.reflect.Proxy
import kotlin.math.PI

// Pedro's field origin is the bottom-left corner (0..144in); the emulator's is field center.
private const val HALF_FIELD_MM = 72.0 * 25.4

/**
 * A goBILDA Pinpoint that reports the emulator's simulated chassis pose instead of reading the
 * I2C bus. Pedro's `setPose` (via [setPosition]) teleports the simulated robot, so the field view
 * always shows where Pedro thinks the robot is.
 *
 * Call [onSimTick] once per tick after `drive.update(dt)` so velocities are available.
 */
class EmulatedPinpoint(private val drive: MecanumRobot) :
    GoBildaPinpointDriver(nullI2cDevice(), true) {

    private var xMm = 0.0
    private var yMm = 0.0
    private var headingRad = 0.0
    private var unwrappedHeadingRad = 0.0
    private var velXMm = 0.0
    private var velYMm = 0.0
    private var velHeadingRad = 0.0
    private var lastSimPose: Pose? = null

    fun onSimTick(dt: Double) {
        val pose = drive.pose
        val last = lastSimPose
        if (last != null && dt > 0) {
            var dh = pose.headingRad - last.headingRad
            if (dh > PI) dh -= 2 * PI
            if (dh < -PI) dh += 2 * PI
            velXMm = (pose.x - last.x) / dt
            velYMm = (pose.y - last.y) / dt
            velHeadingRad = dh / dt
            unwrappedHeadingRad += dh
        }
        lastSimPose = pose
    }

    override fun update() {
        val pose = drive.pose
        xMm = pose.x + HALF_FIELD_MM
        yMm = pose.y + HALF_FIELD_MM
        headingRad = pose.headingRad
    }

    override fun update(data: ReadData) = update()

    override fun setPosition(pos: Pose2D) {
        drive.resetPose(
            Pose(
                pos.getX(DistanceUnit.MM) - HALF_FIELD_MM,
                pos.getY(DistanceUnit.MM) - HALF_FIELD_MM,
                pos.getHeading(AngleUnit.RADIANS)
            )
        )
        lastSimPose = drive.pose
        update()
    }

    override fun setPosX(posX: Double, distanceUnit: DistanceUnit) =
        setPosition(Pose2D(DistanceUnit.MM, distanceUnit.toMm(posX), yMm, AngleUnit.RADIANS, headingRad))
    override fun setPosY(posY: Double, distanceUnit: DistanceUnit) =
        setPosition(Pose2D(DistanceUnit.MM, xMm, distanceUnit.toMm(posY), AngleUnit.RADIANS, headingRad))
    override fun setHeading(heading: Double, angleUnit: AngleUnit) =
        setPosition(Pose2D(DistanceUnit.MM, xMm, yMm, AngleUnit.RADIANS, angleUnit.toRadians(heading)))

    override fun getPosX(distanceUnit: DistanceUnit): Double = distanceUnit.fromMm(xMm)
    override fun getPosY(distanceUnit: DistanceUnit): Double = distanceUnit.fromMm(yMm)
    override fun getHeading(angleUnit: AngleUnit): Double = angleUnit.fromRadians(headingRad)
    override fun getHeading(unnormalizedAngleUnit: UnnormalizedAngleUnit): Double =
        unnormalizedAngleUnit.fromRadians(unwrappedHeadingRad)
    override fun getVelX(distanceUnit: DistanceUnit): Double = distanceUnit.fromMm(velXMm)
    override fun getVelY(distanceUnit: DistanceUnit): Double = distanceUnit.fromMm(velYMm)
    override fun getHeadingVelocity(unnormalizedAngleUnit: UnnormalizedAngleUnit): Double =
        unnormalizedAngleUnit.fromRadians(velHeadingRad)
    override fun getPosition(): Pose2D = Pose2D(DistanceUnit.MM, xMm, yMm, AngleUnit.RADIANS, headingRad)
    override fun getDeviceStatus(): DeviceStatus = DeviceStatus.READY

    // Configuration that only matters to the real device's onboard math.
    override fun setOffsets(xOffset: Double, yOffset: Double, distanceUnit: DistanceUnit) {}
    override fun setEncoderResolution(pods: GoBildaOdometryPods) {}
    override fun setEncoderResolution(ticksPerUnit: Double, distanceUnit: DistanceUnit) {}
    override fun setEncoderDirections(xEncoder: EncoderDirection, yEncoder: EncoderDirection) {}
    override fun setYawScalar(yawScalar: Double) {}
    override fun recalibrateIMU() {}
    override fun resetPosAndIMU() {}
    override fun getDeviceName(): String = "Emulated goBILDA Pinpoint"
}

// GoBildaPinpointDriver's constructor needs an I2C client; nothing reads through it because every
// data-path method is overridden above, so a do-nothing proxy is enough.
private fun nullI2cDevice(): I2cDeviceSynchSimple =
    Proxy.newProxyInstance(
        I2cDeviceSynchSimple::class.java.classLoader,
        arrayOf(I2cDeviceSynchSimple::class.java)
    ) { _, method, _ ->
        when (method.returnType) {
            java.lang.Boolean.TYPE -> false
            Integer.TYPE -> 0
            java.lang.Byte.TYPE -> 0.toByte()
            ByteArray::class.java -> ByteArray(0)
            String::class.java -> "emulated"
            else -> null
        }
    } as I2cDeviceSynchSimple
