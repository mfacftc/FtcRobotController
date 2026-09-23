package org.firstinspires.ftc.teamcode.pedro

import com.pedropathing.revhub.drivetrains.Mecanum
import com.pedropathing.tuning.autotune.Procedure
import com.pedropathing.tuning.autotune.Tuner
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.pedro.procedures.MecanumTuner
import org.firstinspires.ftc.teamcode.pedro.procedures.Tests


class Tuning {
//    STEP 1: Tune drivetrain - Theoretically done
    @Tuner
    fun mecanumTuner(): Procedure {
        return MecanumTuner()
    }

//    SETP 2: Test drivetrain - Determin if theoretical was correct
    @Tuner
    fun tests(): Procedure {
        return Tests(
            { hardwareMap: HardwareMap -> Mecanum(hardwareMap, Constants.drivetrainConfig) },
            null,
            null
        )
    }

//    STEP 3: Tune pinpoint

//    @Tuner
//    fun pinpointTuner(): Procedure {
//        return PinpointTuner()
//    }

//    STEP 4: Test pinpoint

//    @Tuner
//    fun tests(): Procedure {
//        return Tests(
//             { hardwareMap: HardwareMap -> Mecanum(hardwareMap, Constants.drivetrainConfig) },
//             { hardwareMap: HardwareMap -> PinpointLocalizer(hardwareMap, Constants.localizerConfig) },
//            null
//        )
//    }

//    STEP 5: Tune foresight

//    @Tuner
//    fun foresightTuner(): Procedure {
//        return ForesightTuner(
//            { hardwareMap: HardwareMap -> PinpointLocalizer(hardwareMap, Constants.localizerConfig) },
//             { hardwareMap: HardwareMap -> Mecanum(hardwareMap, Constants.drivetrainConfig) }
//        )
//    }

//    STEP 6: Test follower

//    @Tuner
//    fun tests(): Procedure {
//        return Tests(
//            { hardwareMap: HardwareMap? -> Mecanum(hardwareMap, Constants.drivetrainConfig) },
//            { hardwareMap: HardwareMap? -> PinpointLocalizer(hardwareMap, Constants.localizerConfig)},
//            Supplier { Foresight(Constants.foresightConfig) }
//        )
//    }
}
