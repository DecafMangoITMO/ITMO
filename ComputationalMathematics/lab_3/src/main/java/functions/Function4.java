package functions;

import java.util.Collections;
import java.util.List;

// y = x / sqrt(1 + x^2)
public class Function4 extends Function {

    public Function4() {
        super(List.of(new double[] {Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY}), Collections.emptyList());
    }

    @Override
    public double compute(double x) {
        return x / Math.sqrt(1 + Math.pow(x, 2));
    }

    @Override
    public double computeAntiderivative(double x) {
        return Math.sqrt(1 + Math.pow(x, 2));
    }

    @Override
    public String toString() {
        return "x / sqrt(1 + x^2)";
    }
}
