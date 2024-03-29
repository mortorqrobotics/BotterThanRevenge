package org.team1515.BotterThanRevenge.Commands.AutoCommands;


import java.util.function.DoubleSupplier;

import org.team1515.BotterThanRevenge.RobotContainer;
import org.team1515.BotterThanRevenge.Subsystems.Drivetrain;

import com.team364.swervelib.util.SwerveConstants;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;

public class RotateAngle extends Command {
    private Drivetrain drivetrainSubsystem;
    // l
    private PIDController angleController;
    private double maxRotate;

    private DoubleSupplier startAngle;
    private DoubleSupplier angle;

    private double ff = 0.0; // retune

    private double numticks = 5;
    private double currenticks;

    /**
     * Turn the robot a certain angle
     */
    public RotateAngle(Drivetrain drivetrainSubsystem, DoubleSupplier angle) {
        this.drivetrainSubsystem = drivetrainSubsystem;
        this.angle = angle;
        this.maxRotate = 0.5 * SwerveConstants.Swerve.maxAngularVelocity;
        this.startAngle = () -> RobotContainer.gyro.getGyroscopeRotation().getRadians();
        angleController = new PIDController(13.8, 55.2, 0.8625);
        // TODO retune PID
        angleController.setTolerance(Units.degreesToRadians(1));
        angleController.enableContinuousInput(-Math.PI, Math.PI);

        addRequirements(drivetrainSubsystem);
    }

    @Override
    public void initialize() {
        angleController.setSetpoint(MathUtil.angleModulus(getAngle()));
        //System.out.println("Start: " + MathUtil.angleModulus(getAngle()));
    }

    private double getAngle() {
        return startAngle.getAsDouble() + angle.getAsDouble();
    }

    @Override
    public void execute() {
        double currentAngle = RobotContainer.gyro.getGyroscopeRotation().getRadians();
        double error = MathUtil.angleModulus(angleController.getSetpoint() - currentAngle);
        double rotation = (MathUtil.clamp(angleController.calculate(error + angleController.getSetpoint(), angleController.getSetpoint()) + (ff * Math.signum(error)),
                -maxRotate, maxRotate)); // change setpoint?
        drivetrainSubsystem.drive(new Translation2d(0.0, 0.0), rotation, true, true);
        if (angleController.atSetpoint()){
            currenticks+=1;
        }
        else{
            currenticks=0;
        }
    }

    @Override
    public boolean isFinished() {
        return currenticks >= numticks;
    }
    @Override
    public void end(boolean interrupted) {
        drivetrainSubsystem.drive(new Translation2d(0.0, 0.0), 0.0, true, true);
        System.out.println("ended");
    }
}