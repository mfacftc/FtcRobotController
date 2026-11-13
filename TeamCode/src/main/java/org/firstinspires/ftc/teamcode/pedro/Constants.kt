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
        c.xPodOffset.set(4.3)
        c.yPodOffset.set(-2.5)
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD)
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED)
        c.globalDistanceUnit.set(DistanceUnit.INCH)
        c.offsetUnits.set(DistanceUnit.INCH)
    }

    var foresightConfig: ForesightConfig = ForesightConfig({ c: ForesightConfig ->
        val primaryTranslationalForward = Controller.proportional(0.3972890726828331)
        val secondaryTranslationalForward = Controller.proportional(0.14678769659677152)
        val primaryTranslationalLateral = Controller.proportional(0.6101464993924853)
        val secondaryTranslationalLateral = Controller.proportional(0.22543282811079526)

        c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward)
                .put(2.5, primaryTranslationalForward))
        c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral)
                .put(2.5, primaryTranslationalLateral))

        c.coast.set(Controller.proportionalFeedforward(0.010769150865146277))
        c.brake.set(Controller.proportionalFeedforward(0.009153778235374336))

        c.headingFeedback.set(Controller.proportional(4.5))
        c.headingBrakeCoefficients.set(Vector2D.cartesian(0.06929976339080424, 0.011581897171959298))

        c.linearBrakeCoefficients.set(Matrix.diag(0.12925908090144722, 0.098162843745787))
        c.quadraticBrakeCoefficients.set(Matrix.diag(0.001871465838141401, 0.002389023438960475))

        c.maxAchievableForwardVelocity.set(84.96157558555532)
        c.maxAchievableStrafeVelocity.set(69.88120287121474)
        c.naturalForwardDeceleration.set(29.63005502093597)
        c.naturalStrafeDeceleration.set(55.304241413382144)
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