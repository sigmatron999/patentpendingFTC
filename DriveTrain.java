package org.firstinspires.ftc.teamcode;




import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;


public class DriveTrain {
    private double speedScalar = 1.0;

    // initial motors
    private DcMotor frontLeftMotor = null;
    private DcMotor frontRightMotor = null;
    private DcMotor backLeftMotor = null;
    private DcMotor backRightMotor = null;
    
    public boolean isMoving = true;


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


        // direction robot is facing, in angles
        double robotYawDeg = Math.toDegrees(angleWrap(Math.toRadians(currentRotation)));

        // direction stick is pointing, mapped to [-180, 180]
        double stickRotationDeg = Math.atan2(targetPowerY, targetPowerX) * 180/Math.PI;

        // offset joystick vector to account for robot orientation
        double thetaDeg = 360.0 - robotYawDeg + stickRotationDeg;
        
        // sets power to be length of joystick vector
        double power = Math.hypot(targetPowerX, targetPowerY);

        // restricting power between [-1, 1] because of a bug;
        // if abs(targetPowerX), abs(targetPowerY) = 1, then power would be abs(√2)
        power = Range.clip(power, -1.0, 1.0);


        // sin, cos of corrected angle
        double sin = Math.sin((thetaDeg*Math.PI/180.0) + Math.PI/4);
        double cos = Math.cos((thetaDeg*Math.PI/180.0) + Math.PI/4);

        // max of sin, cos; prevents sin, cos from exceding 1
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


        // a normalization
        if ((power + Math.abs(rotation) > 1)){


            frontLeftPower /= power + Math.abs(rotation);
            backLeftPower /= power + Math.abs(rotation);

            frontRightPower /= power - Math.abs(rotation);
            backRightPower /= power - Math.abs(rotation);
        }

        if (isMoving){

            if (motorsNotNull()) {

                // speedScalar will be set from the main function (whenever its built)
                frontLeftMotor.setPower(frontLeftPower * speedScalar);
                frontRightMotor.setPower(frontRightPower * speedScalar);

                backLeftMotor.setPower(backLeftPower * speedScalar);
                backRightMotor.setPower(backRightPower * speedScalar);
            }

        }
        
    }
    // android studio was complaining about the complex if statement
    private boolean motorsNotNull() {
        return frontLeftMotor != null && frontRightMotor != null && backLeftMotor != null && backRightMotor != null;
    }

    // drive relative to the robot
    // ex. forward on joystick = forward on robot, even if the robot is sideways
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

        
        if ((1.0 < power + Math.abs(rotation))){


            frontLeftPower /= power + Math.abs(rotation);
            backLeftPower /= power + Math.abs(rotation);

            frontRightPower /= power - Math.abs(rotation);
            backRightPower /= power - Math.abs(rotation);
        }

        if (isMoving){

            if (motorsNotNull()) {

                frontLeftMotor.setPower(frontLeftPower * speedScalar);
                frontRightMotor.setPower(frontRightPower * speedScalar);

                backLeftMotor.setPower(backLeftPower * speedScalar);
                backRightMotor.setPower(backRightPower * speedScalar);
            }

        }

    }

    public void setSpeedScalar(double change)
    {
        // asssigned from main function (whenever we build it)
        speedScalar = change;

    }








}
