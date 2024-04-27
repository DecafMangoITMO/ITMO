package functions;

// y = ln(x)
public class Function2 extends Function {

    public Function2() {
        super(0d, Double.POSITIVE_INFINITY);
    }

    @Override
    public double calculate(double x) {
        return Math.log(x);
    }

    @Override
    public String toString() {
        return "y = ln(x)";
    }
}
