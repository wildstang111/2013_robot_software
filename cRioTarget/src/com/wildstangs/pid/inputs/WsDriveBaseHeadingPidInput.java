package com.wildstangs.pid.inputs;

import com.wildstangs.pid.inputs.base.IPidInput;
import com.wildstangs.subsystems.WsDriveBase;
import com.wildstangs.subsystems.base.WsSubsystemContainer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Nathan
 */
public class WsDriveBaseHeadingPidInput implements IPidInput {

    public WsDriveBaseHeadingPidInput() {
        //Nothing to do here
    }

    public double pidRead() {
        double gyro_angle;
        gyro_angle = ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).getGyroAngle();
        SmartDashboard.putNumber("Gyro angle: ", gyro_angle);
        return gyro_angle;
    }
}
