package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "BasicAuto", group = "")
public class BasicAuto extends LinearOpMode {

    private final ElapsedTime Time = new ElapsedTime();
    double Step;
    double Speed;
    double Drive;
    double Strafe;
    double Rotate;
    DcMotor FrontRight, FrontLeft, BackRight, BackLeft;

    @Override
    public void runOpMode() throws InterruptedException {
        //Speed Modifier
        Speed = 0.5;

        FrontRight = hardwareMap.get(DcMotor.class, "FrontRight"); //0
        FrontLeft = hardwareMap.get(DcMotor.class, "FrontLeft"); //1
        BackRight = hardwareMap.get(DcMotor.class, "BackRight"); //2
        BackLeft = hardwareMap.get(DcMotor.class, "BackLeft"); //3

        FrontRight.setDirection(DcMotor.Direction.REVERSE);
        FrontLeft.setDirection(DcMotor.Direction.FORWARD);
        BackRight.setDirection(DcMotor.Direction.REVERSE);
        BackLeft.setDirection(DcMotor.Direction.FORWARD);

        //Telemetry
        telemetry.addLine("Current Status: Initialized");
        telemetry.addLine("Autonomous Initialized, Preload TeleOp if Necessary");
        //On Start
        waitForStart();
        telemetry.update();
        Time.reset();
        Step = 0;
        telemetry.addLine("Current Status: Active");

        while (opModeIsActive()) {

            if (opModeIsActive() && (Time.seconds() < 1.5)) {
                Drive = 1;
            } else {
                Step = 1;
            }
            if ((Step == 1) && opModeIsActive() && Time.seconds() < 3) {

            }

            //Motor Power
            FrontRight.setPower(Speed * (Drive + Strafe + Rotate));
            FrontLeft.setPower(Speed * (Drive - Strafe - Rotate));
            BackRight.setPower(Speed * (Drive - Strafe + Rotate));
            BackLeft.setPower(Speed * (Drive + Strafe - Rotate));

            //Telemetry
            telemetry.addData("Auto Step", Step);
            telemetry.addData("Total Runtime", Time);
            telemetry.addData("Drive", Drive);
            telemetry.addData("Rotate", Rotate);
            telemetry.addData("Strafe", Strafe);
        }
    }
}
