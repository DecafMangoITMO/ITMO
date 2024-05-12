package diff_functions;

import lombok.Data;

@Data
public abstract class DiffFunction {

    public abstract double f(double x, double y);
    public abstract double y(double x, double x0, double y0);

}
