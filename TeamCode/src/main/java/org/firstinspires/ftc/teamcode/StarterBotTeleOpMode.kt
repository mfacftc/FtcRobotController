package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple


@TeleOp
class StarterBotTeleOp : LinearOpMode() {
    private var leftDrive: DcMotor? = null
    private var rightDrive: DcMotor? = null

    override fun runOpMode() {
        leftDrive = hardwareMap.get<DcMotor?>(DcMotor::class.java, "leftDrive")
        rightDrive = hardwareMap.get<DcMotor?>(DcMotor::class.java, "rightDrive")

        leftDrive!!.direction = DcMotorSimple.Direction.REVERSE

        waitForStart()
        if (opModeIsActive()) {
            while (opModeIsActive()) {
                // Calling our methods while the OpMode is running
                splitStickArcadeDrive()
                telemetry.update()
            }
        }
    }

    /**
     * Controls for the drivetrain. The robot uses a split stick style arcade drive.
     * Forward and back is on the left stick. Turning is on the right stick.
     */
    private fun splitStickArcadeDrive() {
        val x: Float
        val y: Float

        x = gamepad1.right_stick_x
        y = -gamepad1.left_stick_y
        leftDrive!!.power = (y - x).toDouble()
        rightDrive!!.power = (y + x).toDouble()
    }
}