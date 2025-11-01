/*
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.hardware.limelightvision.*;
import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

import java.util.List;

public class Limelight {

    private Limelight3A limelight;

    private SparkFunOTOS.Pose2D lastPosition = new SparkFunOTOS.Pose2D();
    private boolean isValid = false;

    private double averageX = 0;
    private double averageY = 0;

    public void init(HardwareMap hwMap) {
        limelight = hwMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(30); // ask for data ~30 times per second
        limelight.start();
        limelight.pipelineSwitch(0);
    }

    public SparkFunOTOS.Pose2D getLimelightData(boolean redAlliance, double orientation) {
        // update robot orientation for pose estimation
        limelight.updateRobotOrientation(orientation);

        LLResult result = limelight.getLatestResult();

        // prevent null crashes
        if (result == null || result.getFiducialResults().isEmpty()) {
            isValid = false;
            return this.lastPosition;
        }

        List<LLResultTypes.FiducialResult> fiducialResult = result.getFiducialResults();
        Pose3D pose = fiducialResult.get(0).getRobotPoseFieldSpace();

        if (null == pose || null == pose.getPosition()) {
            this.isValid = false;
            return this.lastPosition;
        }

        SparkFunOTOS.Pose2D output = new SparkFunOTOS.Pose2D();
        output.x = pose.getPosition().x;
        output.y = pose.getPosition().y;

        // process coordinates for alliance/offsets
        output = this.processCoordinates(redAlliance, output);

        // optional smoothing
        output.x = this.lowPass(averageX, output.x);
        output.y = this.lowPass(averageY, output.y);
        this.averageX = output.x;
        this.averageY = output.y;

        // store last valid position
        if (output.x != 0 && output.y != 0) {
            this.lastPosition = output;
        }

        this.isValid = true;
        return output;
    }

    private SparkFunOTOS.Pose2D processCoordinates(boolean redAlliance, SparkFunOTOS.Pose2D pos) {
        SparkFunOTOS.Pose2D processedPosition = new SparkFunOTOS.Pose2D();

        // convert from meters to inches
        processedPosition.x = this.MtoIn(pos.x);
        processedPosition.y = this.MtoIn(pos.y);

        // flip depending on alliance color
        if (!redAlliance) {
            processedPosition.x *= -1;
        } else {
            processedPosition.y *= -1;
        }

        // apply field offsets (calibration)
        processedPosition.x -= 3;
        processedPosition.y -= 10;

        return processedPosition;
    }

    private double lowPass(double average, double value) {
        return (average * 0.5) + (value * 0.5);
    }

    private double MtoIn(double input) {
        return input / 0.0254; // meters → inches
    }

    public boolean getIsValid() {
        return isValid;
    }

    public SparkFunOTOS.Pose2D getLastPosition() {
        return lastPosition;
    }
}

*/