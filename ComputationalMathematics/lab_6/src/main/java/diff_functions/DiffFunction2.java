package diff_functions;

// y' = 1
// y = x + c
public class DiffFunction2 extends DiffFunction {

    @Override
    public double f(double x, double y) {
        return 1;
    }

    @Override
    public double y(double x, double x0, double y0) {
        double c = y0 - x0;

        return x + c;
    }

    @Override
    public String toString() {
        return "y' = 1";
    }
}
