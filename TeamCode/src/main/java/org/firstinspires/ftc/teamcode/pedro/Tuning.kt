package org.firstinspires.ftc.teamcode.pedro

import com.pedropathing.algorithm.Foresight
import com.pedropathing.revhub.drivetrains.Mecanum
import com.pedropathing.revhub.localizers.PinpointLocalizer
import com.pedropathing.tuning.autotune.Procedure
import com.pedropathing.tuning.autotune.Tuner
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.pedro.procedures.ForesightTuner
import org.firstinspires.ftc.teamcode.pedro.procedures.PinpointTuner
import org.firstinspires.ftc.teamcode.pedro.procedures.Tests

@Tuner
fun pinpointTuner(): Procedure {
    return PinpointTuner()
}

@Tuner
fun foresightTuner(): Procedure {
    return ForesightTuner(
        { hardwareMap: HardwareMap -> PinpointLocalizer(hardwareMap, Constants.localizerConfig) },
        { hardwareMap: HardwareMap -> Mecanum(hardwareMap, Constants.drivetrainConfig) }
    )
}

@Tuner
fun tests(): Procedure {
    return Tests(
         { hardwareMap: HardwareMap -> Mecanum(hardwareMap, Constants.drivetrainConfig) },
         { hardwareMap: HardwareMap -> PinpointLocalizer(hardwareMap, Constants.localizerConfig) },
         { Foresight(Constants.foresightConfig) }
    )
}