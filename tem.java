
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;


@TeleOp
public class tem extends LinearOpMode {
    //Declaring the motors and servos
    private DcMotor frontLeft = null;
    private DcMotor frontRight = null;
    private DcMotor backLeft = null;
    private DcMotor backRight = null;


    @Override
    public void runOpMode(){
        //Initializing the motors and servos
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        
        frontLeft.setDirection(DcMotor.direction.REVERSE);
        backRight.setDirection(DcMotor.direction.FORWARD);
        frontRight.setDirection(DcMotor.direction.REVERSE);
        backLeft.setDirection(DcMotor.direction.FORWARD);
        waitForStart(); //Wait for the start button to be pressed on the Driver Hub
        while (opModeIsActive()){ // When start button is pressed opModISActive becomes true and the code inside the while loop runs

            double LeftY  = -gamepad1.left_stick_y;
            double LeftX  = gamepad1.left_stick_x;
            double RightX = gamepad1.right_stick_x;
            double RightY  = -gamepad1.left_stick_y;

            frontLeft.setPower(1);
            backLeft.setPower(1);

            //Telemetry for debugging
            // telemetry.addData("/*{Name to display the motor's output value under.}*/", /*[Declared name of the motor]*/);
            // telemetry.addData("/*{Name to display the servo's output value under.}*/", /*[Declared name of the servo]*/);
            // telemetry.addData("/*{Name to display the continuous rotation servo's output value under.}*/", /*[Declared name of the continuous rotation servo]*/);
            telemetry.update(); //Display updated telemetry data
        }
    }
}