package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

public class IMUOdometry {

    private IMU imu;
    private double lastHeading = 0.0;
    private double totalHeading = 0.0;

    public void init(HardwareMap hwMap) {

        imu = hwMap.get(IMU.class, "imu");

        // Define how the hub is mounted on the robot
        RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.UP;
        RevHubOrientationOnRobot.UsbFacingDirection usbDirection = RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD;

        // Pass that into the IMU parameters
        IMU.Parameters parameters = new IMU.Parameters(
                new RevHubOrientationOnRobot(logoDirection, usbDirection)
        );

        // Initialize IMU with those parameters
        imu.initialize(parameters);

        // Reset yaw to 0 at start
        imu.resetYaw();
    }

    /* Get current heading in degrees (-180 to 180) */
    public double getHeading() {
        YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
        return orientation.getYaw(AngleUnit.DEGREES);
    }

    /* Get continuous (unwrapped) heading — keeps counting beyond ±180° */
    public double getContinuousHeading() {
        double currentHeading = getHeading();
        double delta = currentHeading - lastHeading;

        if (delta > 180) delta -= 360;
        else if (delta < -180) delta += 360;

        totalHeading += delta;
        lastHeading = currentHeading;

        return totalHeading;
    }

    /* Reset heading to 0° */
    public void reset() {
        imu.resetYaw();
        totalHeading = 0.0;
        lastHeading = 0.0;
    }

    /* Check if IMU is initialized */
    public boolean isConnected() {
        return imu != null;
    }
}
