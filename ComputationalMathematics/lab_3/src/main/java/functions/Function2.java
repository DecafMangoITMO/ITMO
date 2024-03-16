package functions;

import java.util.Collections;
import java.util.List;

// y = sin(x)
public class Function2 extends Function {

    public Function2() {
        super(List.of(new double[] {Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY}), Collections.emptyList());
    }

    @Override
    public double compute(double x) {
        return Math.sin(x);
    }

    @Override
    public double computeAntiderivative(double x) {
        return -Math.cos(x);
    }

    @Override
    public String toString() {
        return "y = sin(x)";
    }

}
