package org.firstinspires.ftc.teamcode.pedro

import com.pedropathing.algorithm.Foresight
import com.pedropathing.algorithm.ForesightConfig
import com.pedropathing.controllers.Controller
import com.pedropathing.follower.Follower
import com.pedropathing.math.Matrix
import com.pedropathing.math.Vector2D
import com.pedropathing.revhub.drivetrains.Mecanum
import com.pedropathing.revhub.drivetrains.MecanumConfig
import com.pedropathing.revhub.localizers.PinpointConfig
import com.pedropathing.revhub.localizers.PinpointLocalizer
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit


object Constants {
     var drivetrainConfig: MecanumConfig = MecanumConfig { c: MecanumConfig ->
         c.frontLeftName.set("front_left_drive")
         c.backLeftName.set("back_left_drive")
         c.frontRightName.set("front_right_drive")
         c.backRightName.set("back_right_drive")

         c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE)
         c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE)
         c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD)
         c.backRightDirection.set(DcMotorSimple.Direction.FORWARD)
     }

    var localizerConfig: PinpointConfig = PinpointConfig { c: PinpointConfig ->
        c.name.set("pinpoint")
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
        c.xPodOffset.set(2.713321175162248)
        c.yPodOffset.set(4.59409788837583)
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD)
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED)
        c.globalDistanceUnit.set(DistanceUnit.INCH)
        c.offsetUnits.set(DistanceUnit.INCH)
    }

    var foresightConfig: ForesightConfig = ForesightConfig({ c: ForesightConfig ->
            val primaryTranslationalForward = Controller.proportional(0.3378948515931291)
            val secondaryTranslationalForward = Controller.proportional(0.12484311894694236)
            val primaryTranslationalLateral = Controller.proportional(0.5757290525648917)
            val secondaryTranslationalLateral = Controller.proportional(0.21271650115911614)

            c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward))
            c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral))

            c.coast.set(Controller.proportionalFeedforward(0.011035134783097889))
            c.brake.set(Controller.proportionalFeedforward(0.009379864565633205))

            c.headingFeedback.set(Controller.proportional(5.136107026235783))
            c.headingBrakeCoefficients.set(Vector2D.cartesian(0.0645832094902392, 0.008422063898822609))

            c.linearBrakeCoefficients.set(Matrix.diag(0.11458305647948028, 0.11531196598584367))
            c.quadraticBrakeCoefficients.set(Matrix.diag(0.0013590337568602452, 0.0016458788488539634))

            c.maxAchievableForwardVelocity.set(92.09921257949706)
            c.maxAchievableStrafeVelocity.set(69.34758736968334)
            c.naturalForwardDeceleration.set(49.5535185270945)
            c.naturalStrafeDeceleration.set(70.98431170657246)
        }
    )


    fun create(h: HardwareMap): Follower {
         return Follower(
             PinpointLocalizer(h, localizerConfig),
             Mecanum(h, drivetrainConfig),
             Foresight(foresightConfig)
         )
    }
}