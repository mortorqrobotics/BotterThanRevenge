package org.team1515.BotterThanRevenge.Subsystems;

import org.team1515.BotterThanRevenge.RobotMap;

import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {

    private CANSparkMax lShooter;
    private CANSparkMax rShooter;

    private boolean speaker;
    private boolean amp;

    double leftMax;
    double rightMax;
    
    public Shooter(){
        lShooter = new CANSparkMax(RobotMap.L_SHOOTER_ID, MotorType.kBrushless);
        rShooter = new CANSparkMax(RobotMap.R_SHOOTER_ID, MotorType.kBrushless);
        rShooter.follow(lShooter, true);

        lShooter.setSmartCurrentLimit(RobotMap.SHOOTER_CURRENT_LIMIT);
        rShooter.setSmartCurrentLimit(RobotMap.SHOOTER_CURRENT_LIMIT);
        lShooter.burnFlash();
        rShooter.burnFlash();

        speaker = false;
        amp = false;

        leftMax = 0.0;
        rightMax = 0.0;
    }

    public void setAmp(boolean value){
        amp = value;
    }

    public void setSpeaker(boolean value){
        speaker = value;
    }

    public boolean getAmp(){
        return amp;
    }

    public boolean getSpeaker(){
        return speaker;
    }
    
    public double getLeft(){
        return lShooter.getEncoder().getVelocity();
    }
    public double getRight(){
        return rShooter.getEncoder().getVelocity();
    }

    public void shootAmp(){
        amp = true;
        speaker = false;
        lShooter.set(-RobotMap.AMP_SPEED);
    }

    public void shootSpeaker(){
        speaker = true;
        amp = false;
        lShooter.set(-RobotMap.SPEAKER_SPEED);
    }

    public void shoot(double speed){
        lShooter.set(-speed);
    }
    //
    public void in(){
        lShooter.set(RobotMap.AMP_SPEED);
    }

    public void end(){
        lShooter.set(0);
        setAmp(false);
        setSpeaker(false);
    }
    public void resetDash(){
        leftMax = 0.0;
        rightMax = 0.0;
    }
    @Override
    public void periodic(){
        // SmartDashboard.putNumber("Shooter Left RPM", getLeft());
        // SmartDashboard.putNumber("Shooter Right RPM", getRight());
        // SmartDashboard.putNumber("Shooter Left Current", lShooter.getOutputCurrent());
        // SmartDashboard.putNumber("Shooter Right Current", rShooter.getOutputCurrent());
        // SmartDashboard.putNumber("Shooter Left Voltage", lShooter.getBusVoltage());
        // SmartDashboard.putNumber("Shooter Right Voltage", rShooter.getBusVoltage());
        SmartDashboard.putBoolean("Amp", getAmp());
        SmartDashboard.putBoolean("Speaker", getSpeaker());

        // SmartDashboard.putNumber("Left Shooter Draw", lShooter.getOutputCurrent());
        // SmartDashboard.putNumber("Right Shooter Draw", rShooter.getOutputCurrent());

    }
}
