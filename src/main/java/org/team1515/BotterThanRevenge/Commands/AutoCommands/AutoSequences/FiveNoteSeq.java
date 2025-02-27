package org.team1515.BotterThanRevenge.Commands.AutoCommands.AutoSequences;

import org.team1515.BotterThanRevenge.RobotMap;
import org.team1515.BotterThanRevenge.Commands.AutoCommands.driveLine;
import org.team1515.BotterThanRevenge.Commands.IndexerCommands.AutoFeed;
import org.team1515.BotterThanRevenge.Commands.IntakeCommands.AutoIntakeIn;
import org.team1515.BotterThanRevenge.Commands.IntakeCommands.FlipDown;
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

public class FiveNoteSeq extends SequentialCommandGroup{
    public FiveNoteSeq(Drivetrain drivetrain, Shooter shooter, Indexer indexer, Intake intake, Flip flip, double direction){        
        double subwooferToNoteX = Units.inchesToMeters(RobotMap.SUBWOOFER_TO_NOTE - RobotMap.CHASSIS_WIDTH + 1);
        double subwooferToNoteY = Units.inchesToMeters(RobotMap.NOTE_TO_NOTE - (0.5 * RobotMap.CHASSIS_WIDTH) + 3);

        Point pose0 = new Point(0, 0);
        Point pose1 = new Point(subwooferToNoteX, 0);
        Point pose2 = new Point(subwooferToNoteX, -direction*subwooferToNoteY);
        Point pose3 = new Point(subwooferToNoteX, direction*(subwooferToNoteY-Units.inchesToMeters(3)));
        Point finalPose = new Point(Units.inchesToMeters(RobotMap.ROBOT_STARTING_ZONE_WIDTH + 5), direction*Units.inchesToMeters(63));
        Point pose4 = new Point(Units.inchesToMeters(RobotMap.SUBWOOFER_TO_CENTER), Units.inchesToMeters(RobotMap.SUBWOOFER_TO_FIFTH-(RobotMap.CHASSIS_WIDTH/2))*direction);

        double firstRotation = -RobotMap.AUTO_NOTE_ANGLE_OFFSET*direction;
        double secondRotation = RobotMap.AUTO_NOTE_ANGLE_OFFSET*direction;
        
        addCommands(new TriggeredFlipDown(flip));
        addCommands(new InstantCommand(()->shooter.shootSpeaker()));
        
        addCommands(Commands.waitSeconds(0.5));
        
        //Score Stoed Note
        addCommands(Commands.parallel(
                new AutoFeed(indexer, RobotMap.AUTO_FEED_TIME),
                new FlipDown(flip)
        ).withTimeout(2));

        //Pick Up Note 1
        addCommands(Commands.parallel(
                new driveLine(drivetrain, 0, pose1, 1),
                new AutoIntakeIn(intake, indexer, RobotMap.AUTO_INTAKE_TIME)
        ).withTimeout(2));

        //Score Note 1
        addCommands(new driveLine(drivetrain, 0, pose0, 1).withTimeout(2));
        addCommands(new AutoFeed(indexer, RobotMap.AUTO_FEED_TIME).withTimeout(2));

        //Pick Up Note 2
        addCommands(Commands.parallel(
                new driveLine(drivetrain, firstRotation, pose2, 1, true),
                new AutoIntakeIn(intake, indexer, RobotMap.AUTO_INTAKE_TIME)
        ).withTimeout(2));

        //Score Note 2
        addCommands(new driveLine(drivetrain, -firstRotation, pose0, 1).withTimeout(2));
        addCommands(new AutoFeed(indexer, RobotMap.AUTO_FEED_TIME).withTimeout(2));

        //Pick Up Note 3
        addCommands(Commands.parallel(
                new driveLine(drivetrain, secondRotation, pose3, 1, true),
                new AutoIntakeIn(intake, indexer, RobotMap.AUTO_INTAKE_TIME)
        ).withTimeout(2));

        //Score Note 3
        addCommands(new driveLine(drivetrain, -secondRotation, pose0, 1).withTimeout(2));
        addCommands(new AutoFeed(indexer, RobotMap.AUTO_FEED_TIME).withTimeout(2));

        //Drive Back
        addCommands(new driveLine(drivetrain, 0, finalPose, 1).withTimeout(2));
        addCommands(new InstantCommand(()->shooter.end()));

        //Drive To Note 5
        addCommands(Commands.parallel(
                new driveLine(drivetrain, 0, pose4, 2, true),
                new AutoIntakeIn(intake, indexer, RobotMap.AUTO_INTAKE_TIME+5)
        ).withTimeout(5));
    }
}
