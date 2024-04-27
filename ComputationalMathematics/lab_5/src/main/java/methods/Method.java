package methods;

import lombok.Data;

@Data
public abstract class Method {
    private final double[] x;
    private final double[] y;
    private final int n;

    public abstract double calculate(double value);

}
