package methods;

import diff_functions.DiffFunction;

public abstract class Method {

    public abstract Result calculate(double x0, double y0, double xn, int n, double e, DiffFunction diffFunction);
}
