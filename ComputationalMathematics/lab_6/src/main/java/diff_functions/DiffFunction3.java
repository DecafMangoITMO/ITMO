package diff_functions;

// y' = cos(x)
// y = sin(x) + c
public class DiffFunction3 extends DiffFunction {

    @Override
    public double f(double x, double y) {
        return Math.cos(x);
    }

    @Override
    public double y(double x, double x0, double y0) {
        double c = y0 - Math.sin(x0);

        return Math.sin(x) + c;
    }

    @Override
    public String toString() {
        return "y' = cos(x)";
    }
}
