/*
 * Decompiled with CFR 0.152.
 */
package com.cauca;

public class PIDController {
    private double kp;
    private double ki;
    private double kd;
    private double lastError = 0.0;
    private double integral = 0.0;

    public PIDController(double kp, double ki, double kd) {
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
    }

    public double calculate(double target, double current) {
        double error = target - current;
        this.integral += error;
        double derivative = error - this.lastError;
        double output = this.kp * error + this.ki * this.integral + this.kd * derivative;
        this.lastError = error;
        return output;
    }

    public void reset() {
        this.lastError = 0.0;
        this.integral = 0.0;
    }
}

