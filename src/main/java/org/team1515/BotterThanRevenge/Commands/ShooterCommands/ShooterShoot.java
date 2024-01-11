package org.team1515.BotterThanRevenge.Commands.ShooterCommands;

import edu.wpi.first.wpilibj2.command.Command;

import org.team1515.BotterThanRevenge.Subsystems.Shooter;

public class ShooterShoot extends Command {
    private final Shooter shooter; 
    private double speed;

    public ShooterShoot(Shooter shooter, double speed) {
        this.shooter = shooter;
        this.speed = speed;
        shooter.setAmp(false);
        shooter.setSpeaker(false);
        addRequirements(shooter);
    }

    @Override
    public void execute() {
        shooter.shoot(speed);
    }

    @Override
    public void end(boolean interrupted) {
        shooter.end();
    }
}