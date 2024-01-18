package org.team1515.BotterThanRevenge.Commands.AutoCommands;

import java.util.function.DoubleSupplier;

import org.team1515.BotterThanRevenge.Subsystems.Drivetrain;
import org.team1515.BotterThanRevenge.RobotContainer;
import org.team1515.BotterThanRevenge.Utils.Point;

import com.team364.swervelib.util.SwerveConstants;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;

public class driveSegment extends Command {
    private final Drivetrain drivetrain;
    private Point start;
    private Point end;
    private double t; //initial time parameter
    private double speed;
    private double startTime; //actial system time
    private double i;
    private double j;
    private Pose2d originalPose;

    private PIDController angleController;
    private double maxRotate;
    private DoubleSupplier startAngle;
    private DoubleSupplier angle;
    private double ff = 0.0; // retune
    
    public driveSegment(Drivetrain drivetrain, DoubleSupplier theta, Point start, Point end, double t, Pose2d startPose) {
        this.drivetrain = drivetrain;
        this.originalPose = startPose;
        this.end = end;
        this.t = t*1000;
        startTime = System.currentTimeMillis();

        this.angle = theta;
        this.maxRotate = 0.5 * SwerveConstants.Swerve.maxAngularVelocity;
        this.startAngle = () -> RobotContainer.gyro.getGyroscopeRotation().getRadians();
        angleController = new PIDController(2, 1, 0);
        // TODO retune PID
        angleController.setTolerance(Units.degreesToRadians(3));
        angleController.enableContinuousInput(-Math.PI, Math.PI);

        addRequirements(drivetrain);
    }

    private double getAngle() {
        return startAngle.getAsDouble() + angle.getAsDouble();
    }

    @Override
    public void initialize(){
        start = new Point(drivetrain.getOdometry().getX() - originalPose.getX(), drivetrain.getOdometry().getY() - originalPose.getY());
        double dx = end.x-start.x;//change in x from start to end
        double dy = end.y-start.y;//change in y from start to end
        double mag = Math.sqrt(Math.pow(dx, 2)+Math.pow(dy, 2));//magnitude of the change vector
        i = dx/mag; //unit vector i component
        j = dy/mag; //unit vector j component
        speed = mag/(t/1000);
        startTime = System.currentTimeMillis();
        angleController.setSetpoint(MathUtil.angleModulus(getAngle()));
        //System.out.println("Start: " + MathUtil.angleModulus(getAngle()));
    }

    @Override
    public void execute() {
        double currentAngle = RobotContainer.gyro.getGyroscopeRotation().getRadians();
        double error = -MathUtil.angleModulus(currentAngle - angleController.getSetpoint());
        double rotation = (MathUtil.clamp(angleController.calculate(error + angleController.getSetpoint(), angleController.getSetpoint()) + (ff * Math.signum(-error)),
                -maxRotate, maxRotate)); // change setpoint?
        drivetrain.drive(new Translation2d(speed*i,speed*j), rotation,true,true);
        //System.out.println(" i: " + i + " j: " + j + " speed: " + speed + " length: " + speed*t);
    }

    @Override
    public boolean isFinished() {
        //System.out.println("t: " + t);
        return System.currentTimeMillis()-startTime >= t;
    }

    @Override
    public void end(boolean interrupted) {
        //double xoff = drivetrain.getOdometry().getX()-originalPose.getX();
        //double yoff = drivetrain.getOdometry().getY()-originalPose.getY();
        //System.out.println("x: " + xoff + " y:" + yoff);
        //System.out.print("t: " + (System.currentTimeMillis()-startTime));
        //System.out.println(" Speed: " + speed);
        //drivetrain.drive(new Translation2d(0.0, 0.0), 0.0, false, false);
        //System.out.println("start pose: " + start.x + ", " + start.y + " end pose: " + end.x + ", " + end.y + "\n");
        //System.out.println("odem x: " + drivetrain.getOdometry().getX() + " odem y: " + drivetrain.getOdometry().getY() + "\n");
    }
}