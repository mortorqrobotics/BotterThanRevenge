package org.team1515.BotterThanRevenge.Commands.AutoCommands.AutoSequences;

import java.util.function.DoubleSupplier;

import org.team1515.BotterThanRevenge.RobotMap;
import org.team1515.BotterThanRevenge.Commands.AutoCommands.RotateAngle;
import org.team1515.BotterThanRevenge.Commands.AutoCommands.driveLine;
import org.team1515.BotterThanRevenge.Commands.IndexerCommands.AutoFeed;
import org.team1515.BotterThanRevenge.Commands.IntakeCommands.AutoIntakeIn;
import org.team1515.BotterThanRevenge.Commands.IntakeCommands.FlipDown;
import org.team1515.BotterThanRevenge.Commands.IntakeCommands.FlipUp;
import org.team1515.BotterThanRevenge.Commands.IntakeCommands.TriggeredFlipDown;
import org.team1515.BotterThanRevenge.Subsystems.Drivetrain;
import org.team1515.BotterThanRevenge.Subsystems.Flip;
import org.team1515.BotterThanRevenge.Subsystems.Indexer;
import org.team1515.BotterThanRevenge.Subsystems.Intake;
import org.team1515.BotterThanRevenge.Subsystems.Shooter;
import org.team1515.BotterThanRevenge.Utils.Point;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class PassNotesSeq extends SequentialCommandGroup {
    public PassNotesSeq(Drivetrain drivetrain, Shooter shooter, Indexer indexer, Intake intake, Flip flip, double direction){
        double notesX = Units.inchesToMeters(Units.inchesToMeters(RobotMap.WALL_TO_NOTE));
        double notesY = direction*(Units.inchesToMeters(RobotMap.NOTE_TO_AMP_Y +(RobotMap.NOTE_TO_NOTE/2.0) - RobotMap.AMP_ZONE));
        double centerX = Units.inchesToMeters(RobotMap.WALL_TO_CENTER);
        double centerY = direction*Units.inchesToMeters(-RobotMap.AMP_ZONE+RobotMap.BARRIER_TO_CENTER-RobotMap.CHASSIS_WIDTH);
        double secondNoteY = direction*(centerY+Units.inchesToMeters(RobotMap.CENTER_NOTE_TO_NOTE));
        double passDownY = secondNoteY-(direction*RobotMap.PASS_SPEAKER_OFFSET);
        double thirdNoteY = direction*(centerY+2*(Units.inchesToMeters(RobotMap.CENTER_NOTE_TO_NOTE)));
        double fourthNoteY = direction*(centerY+2*(Units.inchesToMeters(RobotMap.CENTER_NOTE_TO_NOTE)));
        double passUpY = fourthNoteY+(direction*RobotMap.PASS_SPEAKER_OFFSET);
        double fifthNoteY = direction*(centerY+2*(Units.inchesToMeters(RobotMap.CENTER_NOTE_TO_NOTE)));


        //in between amp and center notes
        Point pose1 = new Point(notesX, notesY);
        //top note
        Point pose2 = new Point(centerX, centerY);
        //second note
        Point pose3 = new Point(centerX, secondNoteY);
        //upper shot pose
        Point pose4 = new Point(centerX, passDownY);
        //third note
        Point pose5 = new Point(centerX, thirdNoteY);
        //pass to shoot
        Point pose6 = new Point(centerX, passDownY);
        //fourth note
        Point pose7 = new Point(centerX, fourthNoteY);
        //pass to shoot
        Point pose8 = new Point(centerX, passUpY);
        //fifth note
        Point pose9 = new Point(centerX, fifthNoteY);




        //startup sequence
        addCommands(new TriggeredFlipDown(flip));
        addCommands(new InstantCommand(()->shooter.shoot(RobotMap.AMP_SPEED)));

        addCommands(Commands.parallel(
            Commands.sequence(
                new AutoFeed(indexer, RobotMap.AUTO_FEED_TIME),
                new InstantCommand(()->shooter.shoot(RobotMap.PASS_SPEED))),
            new FlipDown(flip)
        ).withTimeout(2));

        //drive to center and pick up first note

        addCommands(new driveLine(drivetrain, 0, pose1, 1).withTimeout(1));
        addCommands(Commands.parallel(
            new driveLine(drivetrain, 0, pose2, 1),
            new AutoIntakeIn(intake, indexer, (1+.75))).withTimeout(1));

        //turn to second note
        DoubleSupplier turnToNote = () -> Units.degreesToRadians(direction*-90.0);
        addCommands(new RotateAngle(drivetrain, turnToNote).withTimeout(2));

        //get second note
        addCommands(Commands.parallel(
            new driveLine(drivetrain, 0, pose3, 1),
            new AutoIntakeIn(intake, indexer, RobotMap.AUTO_INTAKE_TIME)).withTimeout(1.5)
        );

        //drive out from speaker
        addCommands(new driveLine(drivetrain, 0, pose4, 1));

        //turn to our field and shoot second note
        DoubleSupplier turnToAllies = () -> Units.degreesToRadians(direction*90.0);
        addCommands(new RotateAngle(drivetrain, turnToAllies).withTimeout(2));
        addCommands(new AutoFeed(indexer, RobotMap.AUTO_FEED_TIME).withTimeout(2));
        
        //turn to third note
        addCommands(new RotateAngle(drivetrain, turnToNote).withTimeout(2));

        //get third note
        addCommands(
        Commands.parallel(
            new driveLine(drivetrain, 0, pose5, 1),
            new AutoIntakeIn(intake, indexer, (1+.75))).withTimeout(2)
        );

        //drive out from speaker
        addCommands(new driveLine(drivetrain, 0, pose6, 1).withTimeout(2));

        // turn and fire third note
        addCommands(new RotateAngle(drivetrain, turnToAllies).withTimeout(2));
        addCommands(new AutoFeed(indexer, RobotMap.AUTO_FEED_TIME).withTimeout(2));

        //turn to fourth note
        addCommands(new RotateAngle(drivetrain, turnToNote).withTimeout(2));

        //get fourth note
        addCommands(Commands.parallel(
            new driveLine(drivetrain, 0, pose7, 1),
            new AutoIntakeIn(intake, indexer, (1+.75))).withTimeout(2)
        );

        //drive front from speaker
        addCommands(new driveLine(drivetrain, 0, pose8, 1).withTimeout(1));
        // turn and fire
        addCommands(new RotateAngle(drivetrain, turnToAllies).withTimeout(2));
        addCommands(new AutoFeed(indexer, RobotMap.AUTO_FEED_TIME).withTimeout(2));

        //turn to fifth note
        addCommands(new RotateAngle(drivetrain, turnToNote).withTimeout(2));

        //get fifth note
        addCommands(Commands.parallel(
            new driveLine(drivetrain, 0, pose9, 1),
            new AutoIntakeIn(intake, indexer, (1+.75))).withTimeout(2)
        );

        // turn and fire
        addCommands(new RotateAngle(drivetrain, turnToAllies).withTimeout(2));
        addCommands(Commands.parallel(
            new AutoFeed(indexer, RobotMap.AUTO_FEED_TIME),
            new FlipUp(flip)).withTimeout(2)
            );

    }
}

// SOMEONE HELP MEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
// =sum(help+skilledmentor)*HelpOfGod /* */