package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;





/*
---------------------------------------------------------------------------------------------------------------
CODE IS STILL INCOMPLETE: MISSING MANY COMPONENTS
---------------------------------------------------------------------------------------------------------------
 */
@TeleOp
public class AssembledBot extends LinearOpMode {

    DriveTrain dt = new DriveTrain();
    //Limelight limelight = new Limelight();
    IMUOdometry imu = new IMUOdometry();
    RobotFunctions func = new RobotFunctions();
    @Override
    public void runOpMode(){

        dt.init(hardwareMap);
        imu.init(hardwareMap);
        func.init(hardwareMap);


        waitForStart();


        dt.isMoving = true;
        dt.setSpeedScalar(1.0);

        while (opModeIsActive()){

            double targetPowerX = gamepad2.left_stick_x;
            double targetPowerY = -gamepad2.left_stick_y;
            double targetRotation = gamepad2.right_stick_x;

            boolean intakeButton = gamepad2.a;
            boolean outtakeButton = gamepad2.b;

            double currentHeadingDeg = imu.getHeading();
            dt.fieldOrientedTranslate(targetPowerX, targetPowerY, targetRotation, currentHeadingDeg);


            func.activateIntake(1.0, intakeButton);
            func.activateOuttake(1.0, outtakeButton);
        }
    }

}
