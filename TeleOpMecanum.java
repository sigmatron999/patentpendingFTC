package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name="TeleOp - Basic Mecanum")
public class TeleOpMecanum extends OpMode {

    private DcMotor frontLeft, frontRight, backLeft, backRight;

    @Override
    public void init() {
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeftMotor");
        frontRight = hardwareMap.get(DcMotor.class, "frontRightMotor");
        backLeft = hardwareMap.get(DcMotor.class, "backLeftMotor");
        backRight = hardwareMap.get(DcMotor.class, "backRightMotor");

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop() {
        double y = -gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x;
        double rx = gamepad1.right_stick_x;

        double fl = y + x + rx;
        double bl = y - x + rx;
        double fr = y - x - rx;
        double br = y + x - rx;

        double max = Math.max(Math.max(Math.abs(fl), Math.abs(fr)), Math.max(Math.abs(bl), Math.abs(br)));
        if (max > 1) {
            fl /= max; fr /= max; bl /= max; br /= max;
        }

        frontLeft.setPower(fl);
        backLeft.setPower(bl);
        frontRight.setPower(fr);
        backRight.setPower(br);

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

        // UNCOMMENT THIS IF YOU NEED TO TEST THE MOTOR CONNECTION
        // frontLeft.setPower(1);
        // frontRight.setPower(1);
        // backLeft.setPower(1);
        // backRight.setPower(1);
    }
}
