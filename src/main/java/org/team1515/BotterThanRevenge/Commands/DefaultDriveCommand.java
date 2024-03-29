package org.team1515.BotterThanRevenge.Commands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import org.team1515.BotterThanRevenge.Subsystems.Drivetrain;

import com.team364.swervelib.util.SwerveConstants;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;

public class DefaultDriveCommand extends Command {
    private Drivetrain drivetrain;
    private DoubleSupplier translationSup;
    private DoubleSupplier strafeSup;
    private DoubleSupplier rotationSup;
    private DoubleSupplier directionSup;
    private BooleanSupplier robotCentricSup;

    public DefaultDriveCommand(Drivetrain drivetrain, DoubleSupplier translationSup, DoubleSupplier strafeSup,
            DoubleSupplier rotationSup, DoubleSupplier directionSup, BooleanSupplier robotCentricSup) {
        this.drivetrain = drivetrain;
        addRequirements(drivetrain);

        this.translationSup = translationSup;
        this.strafeSup = strafeSup;
        this.rotationSup = rotationSup;
        this.directionSup = directionSup;
        this.robotCentricSup = robotCentricSup;
    }

    @Override
    public void execute() {
        /* Get Values, Deadband */
        double translationVal = translationSup.getAsDouble();
        double strafeVal = strafeSup.getAsDouble();
        double rotationVal = rotationSup.getAsDouble();
        double direction = directionSup.getAsDouble();
        /* Drive */
        //only change direction if driving robot centric
        if (robotCentricSup.getAsBoolean()){
            drivetrain.drive(
                new Translation2d(direction*translationVal, direction*strafeVal).times(SwerveConstants.Swerve.maxSpeed),
                rotationVal * SwerveConstants.Swerve.maxAngularVelocity,
                !robotCentricSup.getAsBoolean(),
                true);
        }
        else{
            drivetrain.drive(
                    new Translation2d(translationVal, strafeVal).times(SwerveConstants.Swerve.maxSpeed),
                    rotationVal * SwerveConstants.Swerve.maxAngularVelocity,
                    !robotCentricSup.getAsBoolean(),
                    true);
        }
    }
}