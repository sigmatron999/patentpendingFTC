package org.firstinspires.ftc.teamcode;




import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;


public class DriveTrain {
    private double speedScalar = 1.0;
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

        frontLeftMotor.setDirection(DcMotor.Direction.FORWARD);
        backLeftMotor.setDirection(DcMotor.Direction.FORWARD);

        frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
        backRightMotor.setDirection(DcMotor.Direction.REVERSE);


    }

    private double angleWrap(double rad){

        while (rad > Math.PI){

            rad -= Math.PI * 2;
        }
        while (rad < -Math.PI) {
            rad += Math.PI *2;
        }

        return rad;
    }

    public void fieldOrientedTranslate(double targetPowerX, double targetPowerY, double rotation, double currentRotation) {

        double robotYawDeg = Math.toDegrees(angleWrap(Math.toRadians(currentRotation)));

        double stickRotationDeg = Math.atan2(targetPowerY, targetPowerX) * 180/Math.PI;

        double thetaDeg = 360.0 - robotYawDeg + stickRotationDeg;


        double power = Math.hypot(targetPowerX, targetPowerY);

        power = Range.clip(power, -1.0, 1.0);

        double sin = Math.sin((thetaDeg*Math.PI/180.0) + Math.PI/4);
        double cos = Math.cos((thetaDeg*Math.PI/180.0) + Math.PI/4);

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



        if ((power + Math.abs(rotation) > 1)){


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
    private boolean motorsNotNull() {
        return frontLeftMotor != null && frontRightMotor != null && backLeftMotor != null && backRightMotor != null;
    }
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

            if (frontLeftMotor != null && frontRightMotor != null && backLeftMotor != null && backRightMotor != null) {



                frontLeftMotor.setPower(frontLeftPower * speedScalar);
                frontRightMotor.setPower(frontRightPower * speedScalar);

                backLeftMotor.setPower(backLeftPower * speedScalar);
                backRightMotor.setPower(backRightPower * speedScalar);
            }

        }

    }

    public void setSpeedScalar(double change)
    {
        //sets the speed scalar of the motors to a specified value in starter bot
        speedScalar = change;

    }








}
