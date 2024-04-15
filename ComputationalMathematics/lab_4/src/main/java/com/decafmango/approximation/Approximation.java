package com.decafmango.approximation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public abstract class Approximation {
    private final List<double[]> points;
    private final Map<String, Double> coefficients = new HashMap<>();
    private boolean isCorrect = true;

    abstract void calculateCoefficients();
    public abstract double calculateValue(double x);
    public abstract String getName();

    double calculateStandartDeviation() {
        double sumOfSquareDeviations = 0;
        for (double[] point : points)
            sumOfSquareDeviations += Math.pow(calculateValue(point[0]) - point[1], 2);
        return Math.sqrt(sumOfSquareDeviations / n());
    }

    public double calculateDeterminationCoefficient() {
        double avgValue = 0;
        for (double[] point : points)
            avgValue += calculateValue(point[0]);
        avgValue /= points.size();
        
        double sumOfSquareDeviations1 = 0;
        for (double[] point : points)
            sumOfSquareDeviations1 += Math.pow(point[1] - calculateValue(point[0]), 2);
        
        double sumOfSquareDeviations2 = 0;
        for (double[] point : points)
            sumOfSquareDeviations2 += Math.pow(point[1] - avgValue, 2);

        return 1 - sumOfSquareDeviations1 / sumOfSquareDeviations2;
    }

    String getDeterminationCoefficientMessage(double determinationCoefficient) {
        if (determinationCoefficient >= 0.95)
            return "Высокая точность аппроксимации.";
        else if (determinationCoefficient >= 0.75)
            return "Удовлетворительная точность аппроксимации.";
        else if (determinationCoefficient >= 0.5)
            return "Слабая точность аппроксимации.";
        else 
            return "Недостаточная точность аппроксимации.";

    }

    double n() {
        return points.size();
    }

    double sx() {
        double res = 0;
        for (double[] point : points)
            res += point[0];
        return res;
    }

    double sxx() {
        double res = 0;
        for (double[] point : points) 
            res += Math.pow(point[0], 2);
        return res;
    }

    double sxxx() {
        double res = 0;
        for (double[] point : points) 
            res += Math.pow(point[0], 3);
        return res;
    }

    double sxxxx() {
        double res = 0;
        for (double[] point : points) 
            res += Math.pow(point[0], 4);
        return res;
    }

    double sy() {
        double res = 0;
        for (double[] point : points)
            res += point[1];
        return res;
    }

    double sxy() {
        double res = 0;
        for (double[] point : points)
            res += point[0] * point[1];
        return res;
    }

    double sxxy() {
        double res = 0;
        for (double[] point : points)
            res += Math.pow(point[0], 2) * point[1];
        return res;
    }

    double sX() {
        double res = 0;
        for (double[] point : points)
            res += Math.log(point[0]);
        return res;
    }

    double sXX() {
        double res = 0;
        for (double[] point : points) 
            res += Math.pow(Math.log(point[0]), 2);
        return res;
    }

    double sY() {
        double res = 0;
        for (double[] point : points)
            res += Math.log(point[1]);
        return res;
    }
    
    double sXy() {
        double res = 0;
        for (double[] point : points)
            res += Math.log(point[0]) * point[1];
        return res;
    }

    double sxY() {
        double res = 0;
        for (double[] point : points)
            res += point[0] * Math.log(point[1]);
        return res;
    }

    double sXY() {
        double res = 0;
        for (double[] point : points)
            res += Math.log(point[0]) * Math.log(point[1]);
        return res;
    }
    
}
