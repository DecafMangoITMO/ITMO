package functions;

// y = x
public class Function3 implements Function {

    @Override
    public double compute(double x) {
        return x;
    }

    @Override
    public String toString() {
        return "y = x";
    }
}
