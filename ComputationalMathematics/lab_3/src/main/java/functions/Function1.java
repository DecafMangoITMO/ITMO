package functions;

import java.util.Collections;
import java.util.List;

// y = x^3 - 3x^2 + 7x - 10
public class Function1 extends Function {

    public Function1() {
        super(List.of(new double[] {Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY}), Collections.emptyList());
    }

    public double compute(double x) {
        return Math.pow(x, 3) - 3 * Math.pow(x, 2) + 7 * x - 10;
    }

    @Override
    public double computeAntiderivative(double x) {
        return 0.25d * Math.pow(x, 4) - Math.pow(x, 3) + 3.5d * Math.pow(x, 2) - 10 * x;
    }

    @Override
    public String toString() {
        return "y = x^3 - 3x^2 + 7x - 10";
    }
}
