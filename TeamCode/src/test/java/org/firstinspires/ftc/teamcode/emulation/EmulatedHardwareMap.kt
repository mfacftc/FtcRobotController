package org.firstinspires.ftc.teamcode.emulation

import com.qualcomm.robotcore.hardware.HardwareMap

// The real HardwareMap.tryGet/get call Device.isRevControlHub(), which loads the Android-only
// "RobotCore" native library and crashes on a desktop JVM -- so look devices up ourselves.
class EmulatedHardwareMap : HardwareMap(null, null) {
    override fun <T> tryGet(classOrInterface: Class<out T>, deviceName: String): T? {
        val list = allDevicesMap[deviceName.trim()] ?: return null
        for (device in list) {
            if (classOrInterface.isInstance(device)) return classOrInterface.cast(device)
        }
        return null
    }

    override fun <T> get(classOrInterface: Class<out T>, deviceName: String): T =
        tryGet(classOrInterface, deviceName)
            ?: throw IllegalArgumentException(
                "Unable to find a hardware device with name \"$deviceName\" and type ${classOrInterface.simpleName}"
            )
}
