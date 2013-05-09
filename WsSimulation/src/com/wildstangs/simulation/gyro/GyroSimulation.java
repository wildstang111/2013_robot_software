package com.wildstangs.simulation.gyro;

import com.wildstangs.graph.DoubleGraph;
import com.wildstangs.outputmanager.base.IOutputEnum;
import com.wildstangs.outputmanager.base.WsOutputManager;
import com.wildstangs.simulation.encoders.*;
import com.wildstangs.subsystems.WsDriveBase;
import com.wildstangs.subsystems.base.WsSubsystemContainer;
import edu.wpi.first.wpilibj.Gyro;

public class GyroSimulation {
    
    private double left_drive_speed = 0.0;
    private double right_drive_speed = 0.0;
    DoubleGraph gyroAngle;
    
    public GyroSimulation() {
        
        left_drive_speed = 0.0;
        right_drive_speed = 0.0;
        gyroAngle = new DoubleGraph("Gyro Angle", 0, 0);
    }
    
    public void update() {
        left_drive_speed = ((Double) WsOutputManager.getInstance().getOutput(WsOutputManager.LEFT_DRIVE_SPEED).get((IOutputEnum) null));
        right_drive_speed = ((Double) WsOutputManager.getInstance().getOutput(WsOutputManager.RIGHT_DRIVE_SPEED).get((IOutputEnum) null));
        
        Gyro gyro = ((WsDriveBase) (WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE))).getGyro();
        double angle = gyro.getAngle();
        //Handle brakes
        if ((left_drive_speed > 0.1) && (right_drive_speed < 0.1)) {
            angle--;
        } else if ((left_drive_speed < 0.1) && (right_drive_speed > 0.1)) {
            angle++;
        }
        gyro.setAngle(angle);
        gyroAngle.updateWithValue(angle, 360);
    }
}
