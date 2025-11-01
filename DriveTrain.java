package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

public class DriveTrain {
    private double speedScalar = 1.0;

    // initialize motors
    private DcMotor frontLeftMotor;
    private DcMotor frontRightMotor;
    private DcMotor backLeftMotor;
    private DcMotor backRightMotor;

    public boolean isMoving = false;


    public void init(HardwareMap hwMap){

        frontLeftMotor = hwMap.get(DcMotor.class, "frontLeftMotor");
        frontRightMotor = hwMap.get(DcMotor.class, "frontRightMotor");
        backLeftMotor = hwMap.get(DcMotor.class, "backLeftMotor");
        backRightMotor = hwMap.get(DcMotor.class, "backRightMotor");

        frontLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        /* -----------------------------------------
        IF THE ROBOT IS DRIVING IN REVERSE, FLIP THESE
        ---------------------------------------------
        */

        frontLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        backLeftMotor.setDirection(DcMotor.Direction.FORWARD);

        frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
        backRightMotor.setDirection(DcMotor.Direction.REVERSE);


    }

    // restrict the angle reading, in radians, from the imu [-π, π]
    private double angleWrap(double rad){
        while (rad > Math.PI){
            rad -= Math.PI * 2;
        }
        while (rad < -Math.PI) {
            rad += Math.PI *2;
        }
        return rad;
    }


    /*
    field oriented drive; movement control is relative to the field, disregarding robot direction
    ex. If the robot is facing backward relative to the field, but the joystick points forward, the robot drives forward
    */

    public void fieldOrientedTranslate(double targetPowerX, double targetPowerY, double rotation, double currentRotation) {


        // direction robot is facing, in degrees
        double robotYawDeg = Math.toDegrees(angleWrap(Math.toRadians(currentRotation)));

        // direction stick is pointing, mapped to [-180, 180]
        double stickRotationDeg = Math.toDegrees(Math.atan2(targetPowerY, targetPowerX));

        // offset joystick vector to account for robot orientation
        double thetaDeg = 360.0 - robotYawDeg + stickRotationDeg;

        // sets power to be length of joystick vector
        double power = Math.hypot(targetPowerX, targetPowerY);

        // restricting power between [-1, 1] because of a bug;
        // if abs(targetPowerX), abs(targetPowerY) = 1, then power would be abs(√2)
        power = Range.clip(power, -1.0, 1.0);


        // sin, cos of corrected angle, accounting for mecanum offset
        double sin = Math.sin((thetaDeg*Math.PI/180.0) + Math.PI/4);
        double cos = Math.cos((thetaDeg*Math.PI/180.0) + Math.PI/4);


        double maxSinCos = Math.max(Math.abs(sin), Math.abs(cos));


        double frontRightPower;
        double frontLeftPower;
        double backRightPower;
        double backLeftPower;


        rotation *= -1;

        /*
        Essentially, sin and cos are representative of the vertical and
        horizontal vectors that comprise the vector of
        the angle theta. By applying sin to the power of one set of wheels,
        and cos to other, we recreate that vector.
         */

        frontLeftPower = power*cos/maxSinCos + rotation;
        backLeftPower = power*sin/maxSinCos + rotation;

        frontRightPower = power*cos/maxSinCos - rotation;
        backRightPower = power*sin/maxSinCos - rotation;

        double frontMax = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
        double backMax = Math.max(Math.abs(backLeftPower), Math.abs(backRightPower));

        double maxPower = Math.max(frontMax, backMax);

        // a normalization
        if (maxPower > 1.0) {

            frontLeftPower /= maxPower;
            frontRightPower /= maxPower;

            backLeftPower /= maxPower;
            backRightPower /= maxPower;

        }
        // isMoving will serve for autonomous
        if (isMoving && this.motorsNotNull()){

            // speedScalar will be set from the main function (whenever its built)
            frontLeftMotor.setPower(frontLeftPower * speedScalar);
            frontRightMotor.setPower(frontRightPower * speedScalar);

            backLeftMotor.setPower(backLeftPower * speedScalar);
            backRightMotor.setPower(backRightPower * speedScalar);
        }
    }
    // android studio was complaining about the complex if statement
    private boolean motorsNotNull() {
        return frontLeftMotor != null && frontRightMotor != null && backLeftMotor != null && backRightMotor != null;
    }

    // drive relative to the robot
    // similar to the above code, though without angle adjustments
    public void robotOrientedTranslate(double targetPowerX, double targetPowerY, double rotation){

        double thetaRad = Math.atan2(targetPowerY, targetPowerX);
        double power = Math.hypot(targetPowerX,targetPowerY);

        double sin = Math.sin(thetaRad - Math.PI/4);
        double cos = Math.cos(thetaRad - Math.PI/4);

        double maxSinCos = Math.max(Math.abs(sin), Math.abs(cos));

        double frontRightPower;
        double frontLeftPower;
        double backRightPower;
        double backLeftPower;

        rotation *= -1;

        frontLeftPower = power*cos/maxSinCos + rotation;
        backLeftPower = power*sin/maxSinCos + rotation;

        frontRightPower = power*cos/maxSinCos - rotation;
        backRightPower = power*sin/maxSinCos - rotation;

        double frontMax = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
        double backMax = Math.max(Math.abs(backLeftPower), Math.abs(backRightPower));

        double maxPower = Math.max(frontMax, backMax);

        if (maxPower > 1.0){
            frontLeftPower /= maxPower;
            frontRightPower /= maxPower;

            backLeftPower /= maxPower;
            backRightPower /= maxPower;

        }

        if (this.isMoving && this.motorsNotNull()){

            frontLeftMotor.setPower(frontLeftPower * speedScalar);
            frontRightMotor.setPower(frontRightPower * speedScalar);

            backLeftMotor.setPower(backLeftPower * speedScalar);
            backRightMotor.setPower(backRightPower * speedScalar);
        }
    }
    public void setSpeedScalar(double change) {
        // assigned from main function (whenever we build it)
        speedScalar = Range.clip(change, 0.0, 1.0);
    }
}
