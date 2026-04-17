package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name = "BasicTeleOp", group = "Lunar")
public class BasicTeleOp extends LinearOpMode {
    private final ElapsedTime Time = new ElapsedTime();
    private DistanceSensor Ds;
    double DistanceLow;
    double DistanceHigh;
    double Speed;
    double Drive;
    double Strafe;
    double Rotate;
    DcMotor FrontRight, FrontLeft, BackRight, BackLeft, OtL, OtR, I, T;
    Servo Dl;

    @Override
    public void runOpMode() {
        //Modifiers
        Speed = 0.5;
        //Distance Measurements in Centimeters (CM)
        DistanceLow = 0;

        DistanceHigh = 20;

        //Motor Identification (* = Odometery Pod)
        FrontRight = hardwareMap.get(DcMotor.class, "FrontRight"); //0
        FrontLeft = hardwareMap.get(DcMotor.class, "FrontLeft"); //1
        BackRight = hardwareMap.get(DcMotor.class, "BackRight"); //2*
        BackLeft = hardwareMap.get(DcMotor.class, "BackLeft"); //3*

        T = hardwareMap.get(DcMotor.class, "T"); //e0
        OtR = hardwareMap.get(DcMotor.class, "OtR"); //e1
        OtL = hardwareMap.get(DcMotor.class, "OtL"); //e2
        I = hardwareMap.get(DcMotor.class, "I"); //e3

        //Other
        Dl = hardwareMap.get(Servo.class, "Dl");
        Ds = hardwareMap.get(DistanceSensor.class, "Ds");
        Rev2mDistanceSensor sensorTimeOfFlight = (Rev2mDistanceSensor) Ds;

        //Motor Direction Identification
        FrontRight.setDirection(DcMotor.Direction.FORWARD);
        FrontLeft.setDirection(DcMotor.Direction.REVERSE);
        BackRight.setDirection(DcMotor.Direction.REVERSE);
        BackLeft.setDirection(DcMotor.Direction.REVERSE);

        T.setDirection(DcMotor.Direction.FORWARD);
        OtR.setDirection(DcMotor.Direction.FORWARD);
        OtL.setDirection(DcMotor.Direction.REVERSE);
        I.setDirection(DcMotor.Direction.FORWARD);

        //Telemetry
        telemetry.addLine("SYSTEM<< Current Status: Initialized");
        telemetry.addLine("SYSTEM<< TeleOp Initialized");
        telemetry.addLine();
        telemetry.addLine("SYSTEM<< Press \"▶\" to Start");
        //On Start
        Dl.setPosition(.388);
        waitForStart();
        telemetry.update();
        while (opModeIsActive()) {
            telemetry.addLine("SYSTEM<< Current Status: Active");

            //Gamepad 2 Debug
            if (gamepad2.a) {
                telemetry.addData("DISTANCE<< Distance Sensor Device Name", Ds.getDeviceName() );
                telemetry.addData("DISTANCE<< Distance Light Device Name", Dl.getDeviceName() );
                telemetry.addLine();
                telemetry.addData("DRIVETRAIN<< Front Right Motor Device Name", FrontRight.getDeviceName() );
                telemetry.addData("DRIVETRAIN<< Front Left Motor Device Name", FrontLeft.getDeviceName() );
                telemetry.addData("DRIVETRAIN<< Back Right Motor Device Name", BackRight.getDeviceName() );
                telemetry.addData("DRIVETRAIN<< Back Left Motor Device Name", BackLeft.getDeviceName() );
                telemetry.addLine();
                telemetry.addData("OUTTAKE<< Outtake Right Motor Device Name", OtR.getDeviceName() );
                telemetry.addData("OUTTAKE<< Outtake Left Motor Device Name", OtL.getDeviceName() );
                telemetry.addData("TRANSFER<< Transfer Motor Device Name", T.getDeviceName() );
                telemetry.addData("INTAKE<< Intake Motor Device Name", I.getDeviceName() );
            }

            //Outtake
            if (gamepad1.y) {
                if (gamepad1.left_bumper) {
                    OtL.setPower(-1);
                    OtR.setPower(-1);
                } else {
                    OtL.setPower(1);
                    OtR.setPower(1);
                }
                if ((Ds.getDistance(DistanceUnit.CM) >= DistanceLow) && (Ds.getDistance(DistanceUnit.CM) <= DistanceHigh)) {
                    Dl.setPosition(.277);
                    telemetry.addLine("#######################################");
                    telemetry.addLine("# DRIVETRAIN<< Within Launch Position #");
                    telemetry.addLine("#######################################");
                    telemetry.addLine();
                } else {
                    Dl.setPosition(0);
                }
            } else {
                OtL.setPower(0);
                OtR.setPower(0);
            }

            //Intake
            if (gamepad1.b) {
                if (gamepad1.left_bumper) {
                    I.setPower(-1);
                } else {
                    I.setPower(1);
                }
            } else {
                I.setPower(0);
            }
            //Drivetrain Controls
            if (gamepad1.right_bumper) {
                //Delicate Drive Mode
                Drive = (Speed * gamepad1.left_stick_y);
                Rotate = (Speed * gamepad1.right_stick_x);
                Strafe = (Speed * gamepad1.left_stick_x);
            } else if (gamepad1.left_bumper) {
                //Full Speed Drive Mode
                Drive = (Speed / gamepad1.left_stick_y);
                Rotate = (Speed / gamepad1.right_stick_x);
                Strafe = (Speed / gamepad1.left_stick_x);
            } else {
                //Default Drive Mode
                Drive = (gamepad1.left_stick_y);
                Rotate = (gamepad1.right_stick_x);
                Strafe = (gamepad1.left_stick_x);
            }

            //Motor Power
            FrontRight.setPower(Speed * (Rotate + Strafe + Drive));
            FrontLeft.setPower(Speed * (Rotate - Strafe - Drive));
            BackRight.setPower(Speed * (-Rotate - Strafe + Drive));
            BackLeft.setPower(Speed * (-Rotate + Strafe - Drive));

            //Telemetry

            //Distance Sensor
            telemetry.addData("DISTANCE<< Device Name", Ds.getDeviceName() );
            telemetry.addData("DISTANCE<< Range Millimeter ", (Ds.getDistance(DistanceUnit.MM)));
            telemetry.addData("DISTANCE<< Range Centimeter ", (Ds.getDistance(DistanceUnit.CM)));
            telemetry.addData("DISTANCE<< Range Meter ", (Ds.getDistance(DistanceUnit.METER)));
            telemetry.addData("DISTANCE<< Rance Inch ", (Ds.getDistance(DistanceUnit.INCH)));

            telemetry.addLine();

            //Rev2mDistanceSensor specific methods.
            telemetry.addData("DISTANCE<< ID", (sensorTimeOfFlight.getModelID()));
            telemetry.addData("DISTANCE<< did time out", Boolean.toString(sensorTimeOfFlight.didTimeoutOccur()));

            telemetry.addLine();

            //Drivetrain
            telemetry.addData("DRIVETRAIN<< Total Runtime", Time);
            telemetry.addData("DRIVETRAIN<< Drive", Drive);
            telemetry.addData("DRIVETRAIN<< Rotate", Rotate);
            telemetry.addData("DRIVETRAIN<< Strafe", Strafe);

            telemetry.addLine();

            //System
            telemetry.addData("SYSTEM<< Runtime", Time);
            telemetry.update();
        }
    }
}