package org.team1515.BotterThanRevenge.Commands;

import edu.wpi.first.wpilibj2.command.Command;

import org.team1515.BotterThanRevenge.Subsystems.Climber;

import edu.wpi.first.wpilibj2.command.Command;

public class ClimberDown extends Command {

    private final Climber climber;

    public ClimberDown(Climber climber) {
        this.climber = climber;
        addRequirements(climber);
    }

    @Override
    public void execute() {
        climber.down();
    }

    @Override
    public void end(boolean interrupted) {
        climber.end();
    }
}