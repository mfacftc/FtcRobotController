package org.firstinspires.ftc.teamcode.emulation

import com.qualcomm.robotcore.hardware.HardwareMap
import emulator.config.RobotConfig
import emulator.config.buildSimulatedRobot
import emulator.config.parseRobotConfigXml
import emulator.hardware.Direction
import emulator.hardware.RunMode
import emulator.sim.BatteryModel
import emulator.sim.MecanumRobot
import emulator.sim.Pose
import java.io.File

// Gradle runs unit tests with the module directory (TeamCode/) as the working directory.
val robotMap: RobotConfig = parseRobotConfigXml(File("src/main/res/xml/master_config.xml"))

// Left-side drive motors are mounted mirrored on the real robot (Constants sets them to REVERSE).
private val mountedReversedMotors = setOf("front_left_drive", "back_left_drive")
private val pinpointTags = setOf("GoBildaPinpoint", "GoBildaPinpointDriver")

/**
 * The simulated robot: devices, chassis physics and battery. Built once per emulator window (the
 * port monitor holds onto these devices); call [reset] and [buildHardwareMap] on every Init.
 */
class EmulatedRobot(private val config: RobotConfig = robotMap) {
    val sim = buildSimulatedRobot(config)
    val drive = MecanumRobot(
        sim.motors.getValue("front_left_drive"),
        sim.motors.getValue("front_right_drive"),
        sim.motors.getValue("back_left_drive"),
        sim.motors.getValue("back_right_drive")
    )
    val pinpoint = EmulatedPinpoint(drive)
    val battery = BatteryModel()

    init {
        if (sim.unrecognized.isNotEmpty()) {
            println("WARNING: couldn't place these devices: ${sim.unrecognized}")
        }
    }

    fun reset() {
        sim.motors.values.forEach {
            it.setPower(0.0)
            it.resetEncoder()
            it.mode = RunMode.RUN_WITHOUT_ENCODER
            it.direction = Direction.FORWARD
        }
        drive.resetPose(Pose(0.0, 0.0, 0.0))
    }

    fun buildHardwareMap(): HardwareMap {
        val hardwareMap = EmulatedHardwareMap()
        sim.motors.forEach { (name, motor) ->
            hardwareMap.put(name, EmulatedDcMotorEx(motor, mountedReversed = name in mountedReversedMotors))
        }
        // The config file doesn't distinguish servo kinds; register both and let get() pick by type.
        sim.servos.forEach { (name, servo) ->
            hardwareMap.put(name, emulatedServo(servo))
            hardwareMap.put(name, emulatedCRServo(servo))
        }
        config.devices.filter { it.tagName in pinpointTags }.forEach { hardwareMap.put(it.name, pinpoint) }
        return hardwareMap
    }

    fun update(dt: Double) {
        sim.updateAll(dt)
        drive.update(dt)
        pinpoint.onSimTick(dt)
        battery.update(sim.allDevices.sumOf { it.currentDrawAmps() })
    }
}
