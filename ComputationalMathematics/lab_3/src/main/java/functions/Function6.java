package functions;

import java.util.List;

// y = 1 / sqrt(x)
public class Function6 extends Function {

    public Function6() {
        super(List.of(new double[] {0d, Double.POSITIVE_INFINITY}), List.of(0d));
    }

    @Override
    public double compute(double x) {
        return 1 / Math.sqrt(x);
    }

    @Override
    public double computeAntiderivative(double x) {
        return 2 * Math.sqrt(x);
    }

    @Override
    public String toString() {
        return "y = 1 / sqrt(x)";
    }
}
