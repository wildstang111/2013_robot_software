/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.autonomous;

import com.wildstangs.autonomous.programs.*;
import com.wildstangs.autonomous.programs.test.*;
import com.wildstangs.inputmanager.base.WsInputManager;
import com.wildstangs.logger.Logger;
import com.wildstangs.subjects.base.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author coder65535
 */
public class WsAutonomousManager implements IObserver {

    private WsAutonomousProgram[] programs;
    private int currentProgram, lockedProgram;
    private float selectorSwitch, positionSwitch;
    private WsAutonomousProgram runningProgram;
    private boolean programFinished, programRunning, lockInSwitch;
    private static WsAutonomousManager instance = null;
    private WsAutonomousStartPositionEnum currentPosition;

    private WsAutonomousManager() {
        definePrograms();
        WsInputManager.getInstance().getOiInput(WsInputManager.AUTO_PROGRAM_SELECTOR).getSubject((ISubjectEnum) null).attach(this);
        WsInputManager.getInstance().getOiInput(WsInputManager.LOCK_IN_SWITCH).getSubject((ISubjectEnum) null).attach(this);
        WsInputManager.getInstance().getOiInput(WsInputManager.START_POSITION_SELECTOR).getSubject((ISubjectEnum) null).attach(this);
        selectorSwitch = 0;
        lockInSwitch = false;
        positionSwitch = 0;
        currentPosition = WsAutonomousStartPositionEnum.UNKNOWN;
        clear();
    }

    public void update() {
        if (programFinished) {
            runningProgram.cleanup();
            programFinished = false;
            lockedProgram = 0;
            startCurrentProgram();
        }
        runningProgram.update();
        if (runningProgram.isFinished()) {
            programFinished = true;
        }
    }

    public void startCurrentProgram() {
        runningProgram = programs[lockedProgram];
        Logger.getLogger().always("Auton", "Running Autonomous Program", runningProgram.toString());
        runningProgram.initialize();
        SmartDashboard.putString("Running Autonomous Program", runningProgram.toString());
    }

    public void clear() {
        programFinished = false;
        programRunning = false;
        if (runningProgram != null) {
            runningProgram.cleanup();
        }
        runningProgram = programs[0];
        lockedProgram = 0;
        SmartDashboard.putString("Running Autonomous Program", "No Program Running");
        SmartDashboard.putString("Locked Autonomous Program", programs[lockedProgram].toString());
        SmartDashboard.putString("Current Autonomous Program", programs[currentProgram].toString());
        SmartDashboard.putString("Current Start Position", currentPosition.toString());
    }

    public WsAutonomousProgram getRunningProgram() {
        if (programRunning) {
            return runningProgram;
        } else {
            return (WsAutonomousProgram) null;
        }
    }

    public String getRunningProgramName() {
        return runningProgram.toString();
    }

    public String getSelectedProgramName() {
        return programs[currentProgram].toString();
    }

    public String getLockedProgramName() {
        return programs[lockedProgram].toString();
    }

    public WsAutonomousStartPositionEnum getStartPosition() {
        return currentPosition;
    }

    public void acceptNotification(Subject cause) {
        if (cause instanceof DoubleSubject) {
            if (cause == WsInputManager.getInstance().getOiInput(WsInputManager.START_POSITION_SELECTOR)
                    .getSubject((ISubjectEnum) null)) {
                positionSwitch = (float) ((DoubleSubject) cause).getValue();
                if (positionSwitch >= 3.3) {
                    positionSwitch = 3.3f;
                }
                if (positionSwitch < 0) {
                    positionSwitch = 0;
                }
                currentPosition = WsAutonomousStartPositionEnum.getEnumFromValue((int) (Math.floor((positionSwitch / 3.4) * WsAutonomousStartPositionEnum.POSITION_COUNT)));
                SmartDashboard.putString("Current Start Position", currentPosition.toString());
            } else if (cause == WsInputManager.getInstance().getOiInput(WsInputManager.AUTO_PROGRAM_SELECTOR)
                    .getSubject((ISubjectEnum) null)) {
                selectorSwitch = (float) ((DoubleSubject) cause).getValue();
                if (selectorSwitch >= 3.3) {
                    selectorSwitch = 3.3f;
                }
                if(selectorSwitch < 0) {
                    selectorSwitch = 0;
                }
                currentProgram = (int) (Math.floor((selectorSwitch / 3.4) * programs.length));
                SmartDashboard.putString("Current Autonomous Program", programs[currentProgram].toString());
            }
        } else if (cause instanceof BooleanSubject) {
            lockInSwitch = ((BooleanSubject) cause).getValue();
            lockedProgram = !lockInSwitch ? currentProgram : 0;
            SmartDashboard.putString("Locked Autonomous Program", programs[lockedProgram].toString());
        }
    }

    public static WsAutonomousManager getInstance() {
        if (WsAutonomousManager.instance == null) {
            WsAutonomousManager.instance = new WsAutonomousManager();
        }
        return WsAutonomousManager.instance;
    }

    public void setProgram(int index) {
        if (index >= programs.length) {
            index = 0;
        }
        currentProgram = index;
        lockedProgram = currentProgram;
    }

    public void setPosition(int index) {
        if (index >= WsAutonomousStartPositionEnum.POSITION_COUNT) {
            index = 0;
        }
        currentPosition = WsAutonomousStartPositionEnum.getEnumFromValue(index);
    }

    private void definePrograms() {
        programs = new WsAutonomousProgram[15];
        programs[0] = new WsAutonomousProgramSleeper(); //Always leave Sleeper as 0. Other parts of the code assume 0 is Sleeper.
        programs[1] = new WsAutonomousProgramShootFive();
        programs[2] = new WsAutonomousProgramShootFiveFeederStation();
        programs[3] = new WsAutonomousProgramShootFiveUnprotectedFeederStation();
        programs[4] = new WsAutonomousProgramShootFiveFromMiddleLine(); 
        programs[5] = new WsAutonomousProgramShootThree();
        programs[6] = new WsAutonomousProgramShootSevenSensor();
        programs[7] = new WsAutonomousProgramDriveDistanceMotionProfile();
        programs[8] = new WsAutonomousProgramDrivePatterns();
        programs[9] = new WsAutonomousProgramShootSevenDriveAfterOne();
        programs[10] = new WsAutonomousProgramShootSevenActiveAccumulator();
        programs[11] = new WsAutonomousProgramShootFiveFallback();
        programs[12] = new WsAutonomousProgramTestShootSevenDrive();
        programs[13] = new WsAutonomousProgramTestShootSevenDriveComplete();
        programs[14] = new WsAutonomousProgramTestShootSevenIntake();
    }
}
