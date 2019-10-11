/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.Constants;
import frc.robot.OI;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.lib.RebelDrive;

/**
 * Add your docs here.
 */
public class Drive extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  private Victor leftFront;
  private Victor leftBack;
  private Victor rightFront;
  private Victor rightBack;

  public int teleOpDriveSide;

  private RebelDrive robotDrive;
  private Robot robot;

  public Drive(){
    
    leftFront = new Victor(RobotMap.LEFT_FRONT_MOTOR);
    leftBack = new Victor(RobotMap.LEFT_BACK_MOTOR);
    rightFront = new Victor(RobotMap.RIGHT_FRONT_MOTOR);
    rightBack = new Victor(RobotMap.RIGHT_BACK_MOTOR);
    teleOpDriveSide = 1;
    robot = new Robot();
    robotDrive = RebelDrive.getInstance(leftFront, leftBack, rightFront, rightBack);
  }

  protected double applyDeadband(double value, double deadband) {
    if (Math.abs(value) > deadband) {
      if (value > 0.0) {
        return (value - deadband) / (1.0 - deadband);
      } else {
        return (value + deadband) / (1.0 - deadband);
      }
    } else {
      return 0.0;
    }
  }

  public boolean quickTurnController() {
    if (Robot.oi.getDriverStick().getRawAxis(OI.OVERRIDESTICK_LEFT_Y) < 0.2
        && Robot.oi.getDriverStick().getRawAxis(OI.OVERRIDESTICK_LEFT_Y) > -0.2) {
      return true;
    } else {
      return false;
    }
  }

  /*public void createDriveSignal(boolean squaredInputs) {
    boolean quickTurn = Robot.drive.quickTurnController();
    double rawMoveValue = Robot.oi.getDriverStick().getRawAxis(OI.JOYSTICK_LEFT_Y);
    double rawRotateValue = Robot.oi.getDriverStick().getRawAxis(OI.JOYSTICK_RIGHT_X);

    double moveValue = 0;
    double rotateValue = 0;
    if (squaredInputs == true) {
      double deadBandMoveValue = applyDeadband(rawMoveValue, 0.02);
      double deadBandRotateValue = applyDeadband(rawRotateValue, 0.02);
      moveValue = Math.copySign(deadBandMoveValue * deadBandMoveValue, deadBandMoveValue);
      rotateValue = Math.copySign(deadBandRotateValue * deadBandRotateValue, deadBandRotateValue);
    } else {
      rawMoveValue = moveValue;
      rotateValue = rawRotateValue;
    }
   
    if (this.findHighestMotorCurrent() > 20) {
      currentDriveMultiplier = 0.9;
    } else {
      currentDriveMultiplier = 1;
    }
    DriveSignal driveSignal = driveHelper.rebelDrive(Constants.k_drive_coefficient * moveValue,
                    Constants.k_turn_coefficient * rotateValue, quickTurn, false);
    Robot.drive.driveWithHelper(ControlMode.PercentOutput, driveSignal);

}*/
  public void drive(Joystick driveStick, Joystick overrideStick) {
    double moveValue = 0;
    double rotateValue = 0;
    if(Math.abs(overrideStick.getRawAxis(OI.OVERRIDESTICK_LEFT_Y)) > 0 || Math.abs(overrideStick.getRawAxis(OI.OVERRIDESTICK_RIGHT_X)) > 0){
      moveValue = Constants.k_drive_override_coefficient * overrideStick.getRawAxis(OI.OVERRIDESTICK_LEFT_Y);
      rotateValue = Constants.k_turn_override_coefficient * overrideStick.getRawAxis(OI.OVERRIDESTICK_RIGHT_X);
    }
    else if(overrideStick.getRawButton(2)){
      moveValue = 0;
      rotateValue = 0;
    }
    else {
      moveValue = Constants.k_drive_coefficient * driveStick.getRawAxis(OI.DRIVESTICK_Y);
      rotateValue = Constants.k_turn_coefficient * -driveStick.getRawAxis(OI.DRIVESTICK_X);
    }
    
    robotDrive.arcadeDrive(teleOpDriveSide * moveValue, rotateValue, true);
  }

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
