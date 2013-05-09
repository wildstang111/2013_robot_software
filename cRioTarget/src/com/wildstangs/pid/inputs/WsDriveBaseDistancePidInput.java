package com.wildstangs.pid.inputs;

import com.wildstangs.pid.inputs.base.IPidInput;
import com.wildstangs.subsystems.WsDriveBase;
import com.wildstangs.subsystems.base.WsSubsystemContainer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Nathan
 */
public class WsDriveBaseDistancePidInput implements IPidInput {

    public WsDriveBaseDistancePidInput() {
        //Nothing to do here
    }

    public double pidRead() {
        double /*left_encoder_value,*/ right_encoder_value, final_encoder_value;
        //left_encoder_value = ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).getLeftDistance();
        right_encoder_value = ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).getRightDistance();
        final_encoder_value = (/*left_encoder_value + */right_encoder_value)/* / 2*/;
        SmartDashboard.putNumber("Distance: ", final_encoder_value);
        return final_encoder_value;
    }
}
