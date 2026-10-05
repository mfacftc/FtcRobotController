package org.firstinspires.ftc.teamcode

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