package SolvingSystemOfNonlinearEquations.systems;

import SolvingSystemOfNonlinearEquations.functions.Function;

public class SystemOfNonlinearEquations {

    private final Function[] functions;

    public SystemOfNonlinearEquations(Function[] functions) {
        this.functions = functions;
    }

    public Function[] getFunctions() {
        return functions;
    }

    @Override
    public String toString() {
        return "  _\n | " + functions[0].toString() + "\n-|\n | " + functions[1].toString() + "\n  ‾";
    }
}
