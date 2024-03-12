package functions;

// y = tanh(x)
public class Function4 implements Function {

    @Override
    public double compute(double x) {
        return Math.tanh(x);
    }

    @Override
    public String toString() {
        return "y = tanh(x)";
    }

}
