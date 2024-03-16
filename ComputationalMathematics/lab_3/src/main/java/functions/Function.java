package functions;

import lombok.Data;
import java.util.List;

@Data
public abstract class Function {

    private final List<double[]> domain;
    private final List<Double> pointsOfInfiniteDiscontinuity;

    public abstract double compute(double x);

    public abstract double computeAntiderivative(double x);

}
