package SolvingSystemOfNonlinearEquations.functions;

import java.util.ArrayList;
import java.util.List;

// function x^2 + y^2 = 4
public class Function1 extends Function {

    @Override
    public double compute(double x, double y) {
        return Math.pow(x, 2) + Math.pow(y, 2) - 4;
    }

    @Override
    public List<List<Double>> computePoints() {
        List<Double> xPoints = new ArrayList<>();
        List<Double> yPoints = new ArrayList<>();

        for (double x = -2d; x <= 2d; x+=0.01d) {
            xPoints.add(x);
            yPoints.add(Math.sqrt(4 - Math.pow(x, 2)));
        }
        for (double x = 2d; x >= -2d; x -=0.01d) {
            xPoints.add(x);
            yPoints.add(-Math.sqrt(4 - Math.pow(x, 2)));
        }

        return List.of(xPoints, yPoints);
    }

    @Override
    public double computeDerivativeX(double x, double y) {
        return 2 * x;
    }

    @Override
    public double computeDerivativeY(double x, double y) {
        return 2 * y;
    }

    @Override
    public String toString() {
        return "x^2 + y^2 = 4";
    }
}
