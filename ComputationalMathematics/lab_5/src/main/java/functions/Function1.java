package functions;

// y = sin(x)
public class Function1 extends Function {

    public Function1() {
        super(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    @Override
    public double calculate(double x) {
        return Math.sin(x);
    }

    @Override
    public String toString() {
        return "y = sin(x)";
    }

}
