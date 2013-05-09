/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.simulation;

import com.wildstangs.simulation.accumulator.AccumulatorLimitSwitch;
import com.wildstangs.simulation.solenoids.WsSolenoidContainer;
import com.wildstangs.autonomous.WsAutonomousManager;
import com.wildstangs.configmanager.WsConfigManager;
import com.wildstangs.configmanager.WsConfigManagerException;
import com.wildstangs.inputmanager.base.WsInputManager;
import com.wildstangs.inputmanager.inputs.joystick.driver.WsDriverJoystickEnum;
import com.wildstangs.logger.*;
import com.wildstangs.logviewer.LogViewer;
import com.wildstangs.outputmanager.base.WsOutputManager;
import com.wildstangs.outputmanager.outputs.WsDriveSpeed;
import com.wildstangs.outputmanager.outputs.WsVictor;
import com.wildstangs.profiling.WsProfilingTimer;
import com.wildstangs.simulation.encoders.DriveBaseEncoders;
import com.wildstangs.simulation.encoders.FlywheelEncoders;
import com.wildstangs.simulation.funnelator.FunnelatorLimitSwitch;
import com.wildstangs.simulation.gyro.GyroSimulation;
import com.wildstangs.simulation.hopper.HopperLimitSwitches;
import com.wildstangs.subjects.base.Subject;
import com.wildstangs.subsystems.base.WsSubsystemContainer;
import edu.wpi.first.wpilibj.*;

/**
 *
 * @author ChadS
 */
public class WsSimulation {

    static String c = "WsSimulation";
    
    static boolean autonomousRun = true;
    
    //Display graphs 
    static boolean intakeMotorGraphs = false;
    static boolean driveMotorGraphs = true;
    static boolean flywheelSpeedGraphs = false;
    static boolean driveThrottleGraph = true; 
    
    static WsProfilingTimer durationTimer = new WsProfilingTimer("Periodic method duration", 50);
    static WsProfilingTimer periodTimer = new WsProfilingTimer("Periodic method period", 50);
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //Instantiate the Facades and Containers

        //start the log viewer.
        (new Thread(new LogViewer())).start();
        FileLogger.getFileLogger().startLogger();

        try {
            WsConfigManager.getInstance().setFileName("/Config/ws_config.txt");
            WsConfigManager.getInstance().readConfig();
            //System.out.println(WsConfigManager.getInstance().getConfigParamByName("com.wildstangs.WsInputManager.WsDriverJoystick.trim"));
        } catch (WsConfigManagerException wscfe) {
            System.out.println(wscfe.toString());
        }
//        WsConfigManager.getInstance().dumpConfigData();

        Logger logger = Logger.getLogger();

        //System.out.println(WsConfigManager.getInstance().getConfigItemName("com.wildstangs.WsInputManager.WsDriverJoystick.trim"));
        //System.out.println(WsConfigManager.getInstance().dumpConfigData());
        logger.always(c, "sim_startup", "Simulation starting.");
        FileLogger.getFileLogger().logData("Sim Started"); 

        //logger.setLogLevel(Level.ALL);
        //logger.fatal(c, "fatal_test", "fatal");
        //logger.error(c, "erro_test", "error");
        //logger.notice(c, "notice_test", "notice");
        //logger.info(c, "info_test", "info");
        //logger.debug(c, "debug_test", "debug");
        //logger.warning(c, "warning_test", "warning");
        //logger.always(c, "always_test", "always");
        WsInputManager.getInstance();
        WsOutputManager.getInstance();
        WsSubsystemContainer.getInstance();
        
        DoubleSubjectGraph leftDriveSpeed = new DoubleSubjectGraph() ; 
        DoubleSubjectGraph rightDriveSpeed = new DoubleSubjectGraph() ; 
        DoubleSubjectGraph accumulatorSpeed = new DoubleSubjectGraph() ; 
        DoubleSubjectGraph funnelatorSpeed = new DoubleSubjectGraph() ; 
        DoubleSubjectGraph driverThrottle = new DoubleSubjectGraph() ; 
        DoubleSubjectGraph enterSpeed = new DoubleSubjectGraph() ; 
        DoubleSubjectGraph exitSpeed = new DoubleSubjectGraph() ; 
        
        Subject subject;
        if (driveMotorGraphs){
            subject = ((WsDriveSpeed) WsOutputManager.getInstance().getOutput(WsOutputManager.LEFT_DRIVE_SPEED)).getSubject(null);
            leftDriveSpeed = new DoubleSubjectGraph("Left Drive Speed", subject);
            
            subject = ((WsDriveSpeed) WsOutputManager.getInstance().getOutput(WsOutputManager.RIGHT_DRIVE_SPEED)).getSubject(null);
            rightDriveSpeed = new DoubleSubjectGraph("Right Drive Speed", subject);
            
        }

        if(intakeMotorGraphs){
            subject = ((WsVictor) WsOutputManager.getInstance().getOutput(WsOutputManager.ACCUMULATOR_VICTOR)).getSubject(null);
            accumulatorSpeed = new DoubleSubjectGraph("Accumulator Speed", subject);

            subject = ((WsVictor) WsOutputManager.getInstance().getOutput(WsOutputManager.FUNNELATOR_ROLLER)).getSubject(null);
            funnelatorSpeed = new DoubleSubjectGraph("Funnelator Speed", subject);
            
        }
        
        if (driveThrottleGraph){
            subject = ((WsInputManager.getInstance().getOiInput(WsInputManager.DRIVER_JOYSTICK)).getSubject(WsDriverJoystickEnum.THROTTLE));
            driverThrottle = new DoubleSubjectGraph("Driver Throttle", subject);
        }


        if (flywheelSpeedGraphs){
            
            subject = ((WsVictor) WsOutputManager.getInstance().getOutput(WsOutputManager.SHOOTER_VICTOR_ENTER)).getSubject(null);
            enterSpeed = new DoubleSubjectGraph("Enter Speed", subject);

            subject = ((WsVictor) WsOutputManager.getInstance().getOutput(WsOutputManager.SHOOTER_VICTOR_EXIT)).getSubject(null);
            exitSpeed = new DoubleSubjectGraph("Exit Speed", subject);
        }


//        double pid_setpoint = 10;
//        ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).enableDistancePidControl();
//        ((WsDriveBase) WsSubsystemContainer.getInstance().getSubsystem(WsSubsystemContainer.WS_DRIVE_BASE)).setDriveDistancePidSetpoint(pid_setpoint);
        
        DriveBaseEncoders dbEncoders = new DriveBaseEncoders(); 
        FlywheelEncoders flywheelEncoders = new FlywheelEncoders(); 
        HopperLimitSwitches limitSwitches = new HopperLimitSwitches(); 
        AccumulatorLimitSwitch aclimitSwitches = new AccumulatorLimitSwitch(); 
        FunnelatorLimitSwitch funnellimitSwitches = new FunnelatorLimitSwitch();
        GyroSimulation gyro = new GyroSimulation();
        periodTimer.startTimingSection();
        
//        ContinuousAccelFilter accelFilter = new ContinuousAccelFilter(0, 0, 0);
//        double distance_to_go = 60.5;
//        double currentProfileX =0.0; 
//        double currentProfileV =0.0; 
//        double currentProfileA =0.0; 
//        for (int i = 0; i < 60; i++) {
//            //Update measured values 
//            
//            //Update PID using profile velocity as setpoint and measured velocity as PID input 
//            
//            //Update system to get feed forward terms
//            double distance_left = distance_to_go - currentProfileX;
//            logger.debug(c, "AccelFilter", "distance_left: " + distance_left + " p: " + accelFilter.getCurrPos()+ " v: " + accelFilter.getCurrVel() + " a: " + accelFilter.getCurrAcc() );
//            accelFilter.calculateSystem(distance_left , currentProfileV, 0, 600, 102, 0.020);
//            currentProfileX = accelFilter.getCurrPos();
//            currentProfileV = accelFilter.getCurrVel();
//            currentProfileA = accelFilter.getCurrAcc();
//            
//            //Update motor output with PID output and feed forward velocity and acceleration 
//            
//        }
        
        
        logger.always(c, "sim_startup", "Simulation init done.");
        if(autonomousRun)
        {
            WsAutonomousManager.getInstance().setPosition(1);
            WsAutonomousManager.getInstance().setProgram(10);
            WsAutonomousManager.getInstance().startCurrentProgram();
        }
        
        while (true) {
            periodTimer.endTimingSection();
            periodTimer.startTimingSection();
            durationTimer.startTimingSection();
//            if (false == autonomousRun || (false == WsAutonomousManager.getInstance().getRunningProgramName().equalsIgnoreCase("Sleeper"))){
            if (false == autonomousRun  || (false == WsAutonomousManager.getInstance().getRunningProgramName().equalsIgnoreCase("Sleeper"))){
                
                //Update the Victor graphs
                if (driveMotorGraphs){
                    leftDriveSpeed.update();
                    rightDriveSpeed.update();
                }
                if(intakeMotorGraphs){
                    accumulatorSpeed.update(); 
                    funnelatorSpeed.update(); 
                }
                if (driveThrottleGraph){
                    driverThrottle.update(); 
                }
                if (flywheelSpeedGraphs){
                    enterSpeed.update();
                    exitSpeed.update();
                }
                
                gyro.update();

                //Update the encoders
                dbEncoders.update();
                WsInputManager.getInstance().updateSensorData();
                if(autonomousRun)
                {
                    WsInputManager.getInstance().updateOiDataAutonomous();
                    WsAutonomousManager.getInstance().update();
                }
                else
                {
                    WsInputManager.getInstance().updateOiData();
                }
                WsSubsystemContainer.getInstance().update();
                WsOutputManager.getInstance().update();
                WsSolenoidContainer.getInstance().update();

                flywheelEncoders.update(); 
                limitSwitches.update();
                aclimitSwitches.update();
                funnellimitSwitches.update();
            }

            double spentTime = durationTimer.endTimingSection();
            int spentMS = (int)(spentTime *1000); 
            int timeToSleep = ((20-spentMS)>0 ?(20-spentMS) :0 );
            try {
                Thread.sleep(timeToSleep);
            } catch (InterruptedException e) {
            }

        }

    }
}
