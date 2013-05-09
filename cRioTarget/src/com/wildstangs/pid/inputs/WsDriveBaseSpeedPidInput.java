package com.wildstangs.pid.inputs;

import com.wildstangs.pid.inputs.base.IPidInput;
import com.wildstangs.subsystems.WsDriveBase;
import com.wildstangs.subsystems.base.WsSubsystemContainer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Nathan
 */
public class WsDriveBaseSpeedPidInput implements IPidInput {

    public WsDriveBaseSpeedPidInput() {
        //Nothing to do here
    }

    public double pidRead() {
        double /*left_encoder_value,*/ right_encoder_value, final_encoder_value;
        //left_encoder_value = ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).getLeftDistance();
        double currentVelocity = ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).getVelocity();
        return currentVelocity;
    }
}
