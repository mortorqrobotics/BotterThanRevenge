package org.team1515.BotterThanRevenge.Subsystems;

import org.team1515.BotterThanRevenge.RobotMap;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
    
    private CANSparkMax bottomIntake;
    private CANSparkMax topIntake;

    public Intake(){
        bottomIntake = new CANSparkMax(RobotMap.INTAKE_ID, MotorType.kBrushless);
        topIntake = new CANSparkMax(RobotMap.INTAKE_ID, MotorType.kBrushless);

    }

    public void in(){
        bottomIntake.set(RobotMap.INTAKE_SPEED);
        topIntake.set(RobotMap.INTAKE_SPEED);

    }

    public void out(){
        bottomIntake.set(-RobotMap.INTAKE_SPEED);
        topIntake.set(-RobotMap.INTAKE_SPEED);

    }

    public void end(){
        bottomIntake.set(0);
        topIntake.set(0);
    }
}
