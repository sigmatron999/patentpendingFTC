package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class RobotFunctions {

    DcMotor leftIntakeMotor;
    DcMotor rightIntakeMotor;

    DcMotor leftOuttakeMotor;
    DcMotor rightOuttakeMotor;


    public void init(HardwareMap hwMap){

        leftIntakeMotor = hwMap.get(DcMotor.class, "leftIntakeMotor");
        rightIntakeMotor = hwMap.get(DcMotor.class, "rightIntakeMotor");

        leftOuttakeMotor = hwMap.get(DcMotor.class, "leftOuttakeMotor");
        rightOuttakeMotor = hwMap.get(DcMotor.class, "rightOuttakeMotor");

        leftIntakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightIntakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        leftOuttakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightOuttakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        leftIntakeMotor.setDirection(DcMotor.Direction.FORWARD);
        rightIntakeMotor.setDirection(DcMotor.Direction.REVERSE);

        leftOuttakeMotor.setDirection(DcMotor.Direction.FORWARD);
        rightOuttakeMotor.setDirection(DcMotor.Direction.REVERSE);

    }

    public void activateIntake(double power, boolean isPressed) {

        if (isPressed) {
            leftIntakeMotor.setPower(power);
            rightIntakeMotor.setPower(power);
        }
    }

    public void activateOuttake(double power, boolean isPressed) {

        if (isPressed) {

            leftOuttakeMotor.setPower(power);
            rightOuttakeMotor.setPower(power);
        }

    }

}
