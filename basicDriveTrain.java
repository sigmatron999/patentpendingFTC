package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


@TeleOp
public class basicDriveTrain extends LinearOpMode{

    private DcMotor FrontRightMotor;
    private DcMotor FrontLeftMotor;
    private DcMotor BackRightMotor;
    private DcMotor BackLeftMotor;

    private boolean useFavouredDriving = true;


    @Override
    public void runOpMode(){

        // Initializing motors
        FrontLeftMotor = hardwareMap.get(DcMotor.class, "FrontLeft");
        FrontRightMotor = hardwareMap.get(DcMotor.class, "FrontRight");
        BackLeftMotor = hardwareMap.get(DcMotor.class, "BackRight");
        BackRightMotor = hardwareMap.get(DcMotor.class, "BackLeft");

        FrontLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FrontRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BackLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        BackRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        FrontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FrontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BackLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BackRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        /*
         *-------------------------------------------------------------
         *  IF THE ROBOT IS DRIVING IN REVERSE SWITCH THESE
         * ------------------------------------------------------------
         */


        FrontLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        BackLeftMotor.setDirection(DcMotor.Direction.FORWARD);

        // reverse so they drive same way
        FrontRightMotor.setDirection(DcMotor.Direction.REVERSE);
        BackRightMotor.setDirection(DcMotor.Direction.REVERSE);


        telemetry.addLine("motors should be working");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            // driving style
            if (gamepad1.x){
                useFavouredDriving = true;
            } else if (gamepad1.y) {
                useFavouredDriving = false;
            }

            if (useFavouredDriving){
                favouredDriving();
            } else {
                unfavouredDriving();
            }


        }
    }
    // standard arcade style driving (1 joystick)
    private void favouredDriving() {

        double drive = -gamepad1.left_stick_y;
        double turning = gamepad1.left_stick_x;

        double leftPower = drive + turning;
        double rightPower = drive - turning;

        double max  = Math.max(Math.abs(drive), Math.abs(turning));

        if (max > 1.0) {

            leftPower /= max;
            rightPower /= max;
        }

        double speedScale = 0.8;

        // slow it down, easier to drive
        leftPower *= speedScale;
        rightPower *= speedScale;

        setMotorPower(leftPower, rightPower);

    }

    // tank style driving(lowk weird imo)
    // 2 joysticks (put them in opposite directions to turn)
    // ex. left forward, right backward -> turn right
    private void unfavouredDriving() {

        double leftPower = -gamepad1.left_stick_y;
        double rightPower = -gamepad1.right_stick_y;


        double speedScale = 0.8;

        // slow it down, easier to drive
        leftPower *= speedScale;
        rightPower *= speedScale;

        setMotorPower(leftPower, rightPower);
    }

    private void setMotorPower(double leftPower, double rightPower) {

        frontLeftMotor.setPower(leftPower);
        backLeftMotor.setPower(leftPower);
        frontRightMotor.setPower(rightPower);
        backRightMotor.setPower(rightPower);
    }












}
