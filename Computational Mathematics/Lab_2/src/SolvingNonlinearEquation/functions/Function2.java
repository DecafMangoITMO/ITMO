package SolvingNonlinearEquation.functions;

/*
function: y = 1.62x^3 - 8.15x^2 + 4.39x + 4.29
solutions of equation y = 0 and their isolation intervals:
    x1 = -0.489, [-inf, 0.295]
    x2 = 1.275d, [0.295, 3.059]
    x3 = 4.246, [3.059, +inf]
 */
public class Function2 extends Function {

    public Function2() {
        super(new Solution[]{
                new Solution(-0.489d, 0, 0.295d, false, true),
                new Solution(1.275d, 0.295d, 3.059d, true, true),
                new Solution(4.246d, 3.059d, 0, true, false)
        });
    }

    @Override
    public double compute(double argument) {
        return 1.62 * Math.pow(argument, 3) - 8.15 * Math.pow(argument, 2) + 4.39 * argument + 4.29;
    }

    @Override
    public double computeDerivative(double argument) {
        return 1.62 * 3 * Math.pow(argument, 2) - 8.15 * 2 * argument + 4.39;
    }

    @Override
    public double computeSecondDerivative(double argument) {
        return 1.62 * 3 * 2 * argument - 8.15 * 2;
    }

    @Override
    public String toString() {
        return "1.62x^3 - 8.15x^2 + 4.39x + 4.29 = 0";
    }
}
