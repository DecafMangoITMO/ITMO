package functions;

// y = x^3 - 3x^2 + 7x - 10
public class Function1 implements Function {

    public double compute(double x) {
        return Math.pow(x, 3) - 3 * Math.pow(x, 2) + 7 * x - 10;
    }

    @Override
    public String toString() {
        return "y = x^3 - 3x^2 + 7x - 10";
    }
}
