package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name="TeleOp - Basic Mecanum (Linear)")
public class TeleOpMecanum extends LinearOpMode {
    DcMotor frontLeft, frontRight, backLeft, backRight;

    @Override
    public void runOpMode() {
        // Hardware mapping
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeftMotor");
        frontRight = hardwareMap.get(DcMotor.class, "frontRightMotor");
        backLeft = hardwareMap.get(DcMotor.class, "backLeftMotor");
        backRight = hardwareMap.get(DcMotor.class, "backRightMotor");

        // Set motor directions (reverse left side)
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        // Let driver know robot is ready
        telemetry.addLine("Initialized — ready to start!");
        telemetry.update();

        // Wait for the start button
        waitForStart();

        // Run until stop is pressed
        while (opModeIsActive()) {
           drive();
        }
    }

    void drive() {
        // note: The y-axis is reversed, so we negate it
        double y = -gamepad1.left_stick_y;   // forward/backward
        double x = gamepad1.left_stick_x;    // strafe left/right
        double rx = gamepad1.right_stick_x;  // rotate right/left

        // basic mecanum drive math
        double fl = y + x + rx;
        double bl = y - x + rx;
        double fr = y - x - rx;
        double br = y + x - rx;

        // normalize if any value is above 1
        double max = Math.max(Math.max(Math.abs(fl), Math.abs(fr)), Math.max(Math.abs(bl), Math.abs(br)));
        if (max > 1.0) {
            fl /= max;
            fr /= max;
            bl /= max;
            br /= max;
        }

        // apply calculated powers to motors
        frontLeft.setPower(fl);
        backLeft.setPower(bl);
        frontRight.setPower(fr);
        backRight.setPower(br);

        // telemetry for debugging
        telemetry.addLine("CONTROLLER INPUT");
        telemetry.addData("y", y);
        telemetry.addData("x", x);
        telemetry.addData("rx", rx);

        telemetry.addLine("MOTOR VALUES");
        telemetry.addData("Front Left Power", fl);
        telemetry.addData("Front Right Power", fr);
        telemetry.addData("Back Left Power", bl);
        telemetry.addData("Back Right Power", br);
        telemetry.update();
    }
}
