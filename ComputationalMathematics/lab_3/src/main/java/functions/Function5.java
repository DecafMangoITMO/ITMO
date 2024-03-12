package functions;

// y = x / sqrt(1 + x^2)
public class Function5 implements Function {

    @Override
    public double compute(double x) {
        return x / Math.sqrt(1 + Math.pow(x, 2));
    }

    @Override
    public String toString() {
        return "x / sqrt(1 + x^2)";
    }
}
