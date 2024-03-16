package util;

import functions.Function;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConvergenceChecker {

    private final double deviation = 0.000000001d;

    public List<double[]> checkConvergence(Function function, double a, double b) {
        List<double[]> intervals = new ArrayList<>();
        List<Double> points = function.getPointsOfInfiniteDiscontinuity();

        int intervalsCount = 0;
        for (double point : points) {
            if (point < a || point > b)
                continue;
            double antiderivativeValue = function.computeAntiderivative(point);

            if (Double.isNaN(antiderivativeValue) || Double.isInfinite(antiderivativeValue))
                return Collections.emptyList();

            if (point == a) {
                intervals.add(new double[]{a + deviation, b});
                intervalsCount++;
            } else if (point == b) {
                if (intervals.isEmpty())
                    intervals.add(new double[] {a, b - deviation});
                else
                    intervals.get(intervalsCount - 1)[1] = b - deviation;
            } else {
                intervals.get(intervalsCount - 1)[1] = point - deviation;
                intervals.add(new double[] {point + deviation, b});
                intervalsCount++;
            }
        }
        return intervals.isEmpty() ? List.of(new double[] {a, b}) : intervals;
    }

}
