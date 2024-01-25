package org.team1515.BotterThanRevenge.Commands.AutoCommands.AutoSequences;

import java.util.function.DoubleSupplier;

import org.team1515.BotterThanRevenge.RobotMap;
import org.team1515.BotterThanRevenge.Commands.AutoCommands.driveArcLength;
import org.team1515.BotterThanRevenge.Commands.AutoCommands.driveSegment;
import org.team1515.BotterThanRevenge.Subsystems.Drivetrain;
import org.team1515.BotterThanRevenge.Utils.Point;
import org.team1515.BotterThanRevenge.Utils.bezierUtil;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class ThreeNoteSeq extends SequentialCommandGroup{
     public ThreeNoteSeq(Drivetrain drivetrain, int direction){
        double finalPose = direction*(0.5*RobotMap.CHASSIS_WIDTH + 0.5*RobotMap.SUBWOOFER_LONG_WIDTH + RobotMap.BUMPER_WIDTH); //assuming red
        //go negative direction
        Point[] path = {
            new Point(0, 0),
            new Point(Units.inchesToMeters(53.5), Units.inchesToMeters(2.3)), 
            // 
            new Point(Units.inchesToMeters(RobotMap.SUBWOOFER_DEPTH), Units.inchesToMeters(finalPose)) 
            // subwoffer extends 3 ft 1/8 in, a little more to be safe
        };
        path = bezierUtil.spacedPoints(path, 25);
        DoubleSupplier angle = () -> Units.degreesToRadians(180); //make sure shooter is forward
        
        //start shooter speed up
        addCommands(new driveArcLength(drivetrain, path, 3.5, angle));
        //run indexer into shooter
        //wait 1 seconds + flip down intake
        
        angle = () -> Units.degreesToRadians(0);
        double time = 0.75;
        double speed = Units.inchesToMeters(RobotMap.SUBWOOFER_TO_NOTE-RobotMap.CHASSIS_WIDTH-2*RobotMap.BUMPER_WIDTH)/time;

        Pose2d startPoint = new Pose2d(new Translation2d(Units.inchesToMeters(RobotMap.SUBWOOFER_DEPTH), Units.inchesToMeters(finalPose)), new Rotation2d(180.0));
        Point finalPoint = new Point(Units.inchesToMeters(RobotMap.SUBWOOFER_TO_NOTE-RobotMap.CHASSIS_WIDTH-2*RobotMap.BUMPER_WIDTH), 0);
        addCommands(new driveSegment(drivetrain, angle, finalPoint, speed, startPoint, true));
        //run intake+indexer
        startPoint = new Pose2d(new Translation2d(startPoint.getX()+finalPoint.x, startPoint.getY()+finalPoint.y), new Rotation2d(180.0));
        finalPoint = new Point(-Units.inchesToMeters(RobotMap.SUBWOOFER_TO_NOTE-RobotMap.CHASSIS_WIDTH-2*RobotMap.BUMPER_WIDTH), 0);
        addCommands(Commands.waitSeconds(0.5));
        addCommands(new driveSegment(drivetrain, angle, finalPoint, speed, startPoint, true));
        //run indexer into shooter
        startPoint = new Pose2d(new Translation2d(startPoint.getX()+finalPoint.x, startPoint.getY()+finalPoint.y), new Rotation2d(180.0));
        finalPoint = new Point(Units.inchesToMeters(RobotMap.SUBWOOFER_TO_NOTE-RobotMap.CHASSIS_WIDTH-2*RobotMap.BUMPER_WIDTH), direction*RobotMap.NOTE_TO_NOTE);
        addCommands(new driveSegment(drivetrain, angle, finalPoint, speed, startPoint, true));
        //indexer+intake
        startPoint = new Pose2d(new Translation2d(startPoint.getX()+finalPoint.x, startPoint.getY()+finalPoint.y), new Rotation2d(180.0));
        finalPoint = new Point(-Units.inchesToMeters(RobotMap.SUBWOOFER_TO_NOTE-RobotMap.CHASSIS_WIDTH-2*RobotMap.BUMPER_WIDTH), direction*-RobotMap.NOTE_TO_NOTE);
        addCommands(new driveSegment(drivetrain, angle, finalPoint, speed, startPoint, true));
        //run indexer into shooter
        //end all
    }
}
