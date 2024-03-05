package SolvingNonlinearEquation.functions;


/*
function: y = x^3 - x + 4
solutions of equation y = 0 and their isolation intervals:
    x1 = -1.796, [-inf, -1]
 */
public class Function1 extends Function {

    public Function1() {
        super(new Solution[]{
                new Solution(-1.796d, 0d, -0.577d, false, true)
        });
    }

    @Override
    public double compute(double argument) {
        return Math.pow(argument, 3) - argument + 4;
    }

    @Override
    public double computeDerivative(double argument) {
        return 3 * Math.pow(argument, 2) - 1;
    }

    @Override
    public double computeSecondDerivative(double argument) {
        return 6 * argument;
    }

    @Override
    public String toString() {
        return "x^3 - x + 4 = 0";
    }

}
