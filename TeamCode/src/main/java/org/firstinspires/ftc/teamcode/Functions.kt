package org.firstinspires.ftc.teamcode

import com.pedropathing.math.Pose
import kotlin.math.abs

fun Boolean.toDouble(): Double = if (this) 1.0 else 0.0

fun fitToRange(value: Double, max: Double = 1.0, min: Double = -1.0): Double {
    var newValue: Double = value
    if (value > max) {
        newValue = max
    } else if (value < min) {
        newValue = min
    }
    return newValue
}

fun velocityToRpm(motorVelocity: Double, maxRpm: Double): Double {
    return abs((motorVelocity / 28.0) * (maxRpm / 100.0))
}

fun invertPose(pose: Pose): Pose {
    // Pose stores heading in radians, so build it directly rather than through a degrees PoseFactory
    return Pose(pose.x(), 144-pose.y(), -pose.heading())
}