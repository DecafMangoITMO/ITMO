package SolvingNonlinearEquation.functions;


/*
function: y = exp(x) - 5
solutions of equation y = 0 and their isolation intervals:
    x1 = 1.609, [-inf, +inf]
 */
public class Function3 extends Function {

    public Function3() {
        super(new Solution[]{
                new Solution(1.609, 0, 0, false, false)
        });
    }

    @Override
    public double compute(double argument) {
        return Math.exp(argument) - 5;
    }

    @Override
    public double computeDerivative(double argument) {
        return Math.exp(argument);
    }

    @Override
    public double computeSecondDerivative(double argument) {
        return Math.exp(argument);
    }

    @Override
    public String toString() {
        return "exp(x) - 5 = 0";
    }
}
