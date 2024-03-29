package org.team1515.BotterThanRevenge.Subsystems;

import org.team1515.BotterThanRevenge.RobotMap;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Indexer extends SubsystemBase {
    
    private CANSparkMax indexer;
    private DigitalInput sensor;

    public Indexer(){
        indexer = new CANSparkMax(RobotMap.INDEXER_ID, MotorType.kBrushless);
        indexer.setSmartCurrentLimit(RobotMap.INDEXER_CURRENT_LIMIT);
        indexer.burnFlash();
        sensor = new DigitalInput(RobotMap.INDEX_SENSOR_CHANNEL);
    }

    public boolean isBlocked(){
        return !sensor.get(); // TODO inverted, false if is blocked?
        //return false;
    }

    public void up(){
        indexer.set(-RobotMap.INDEXER_SPEED);
    }

    public void down(){
        indexer.set(RobotMap.INDEXER_SPEED);
    }

    public void end() {
        indexer.set(0);
    }

    @Override
    public void periodic(){
        SmartDashboard.putBoolean("beamBreak", isBlocked());
        //SmartDashboard.putNumber("Indexer RPM", indexer.getEncoder().getVelocity());
        SmartDashboard.putNumber("Indexer Draw", indexer.getOutputCurrent());
        //SmartDashboard.putNumber("Indexer Voltage", indexer.getBusVoltage());
    }
}