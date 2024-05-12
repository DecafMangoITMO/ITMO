package diff_functions;

// y' = y + (1 + x)y^2
// y = - (e^x) / (xe^x + c)
public class DiffFunction1 extends DiffFunction {

    @Override
    public double f(double x, double y) {
        return y + (1 + x) * Math.pow(y, 2);
    }

    @Override
    public double y(double x, double x0, double y0) {
        double c = -Math.exp(x0) / y0 - x0 * Math.exp(x0);

        return -Math.exp(x) / (x * Math.exp(x) + c);
    }

    @Override
    public String toString() {
        return "y' = y + (1 + x)y^2";
    }
}
