package functions;

// y = sin(x)
public class Function2 implements Function {

    @Override
    public double compute(double x) {
        return Math.sin(x);
    }

    @Override
    public String toString() {
        return "y = sin(x)";
    }

}
