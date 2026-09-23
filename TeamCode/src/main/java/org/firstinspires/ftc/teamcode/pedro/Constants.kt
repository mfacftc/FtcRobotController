package org.firstinspires.ftc.teamcode.pedro

import com.pedropathing.config.Configuration
import com.pedropathing.follower.Follower
import com.pedropathing.revhub.drivetrains.MecanumConfig
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap


object Constants {
     var drivetrainConfig: MecanumConfig = MecanumConfig(
        Configuration { c: MecanumConfig ->
            c.frontLeftName.set("FrontLeftDrive")
            c.backLeftName.set("BackLeftDrive")
            c.frontRightName.set("FrontRightDrive")
            c.backRightName.set("BackRightDrive")

            c.frontLeftDirection.set(DcMotorSimple.Direction.FORWARD)
            c.backLeftDirection.set(DcMotorSimple.Direction.FORWARD)
            c.frontRightDirection.set(DcMotorSimple.Direction.REVERSE)
            c.backRightDirection.set(DcMotorSimple.Direction.REVERSE)
        }
    )

    fun create(h: HardwareMap?): Follower? {
        // return new Follower(Drivetrain, Localizer, Foresight);
        return null
    }
}