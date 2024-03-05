package SolvingSystemOfNonlinearEquations.methods;

import SolvingSystemOfNonlinearEquations.functions.Function;
import SolvingSystemOfNonlinearEquations.systems.SystemOfNonlinearEquations;

import java.util.ArrayList;
import java.util.List;

public class NewtonsMethod implements Method {

    public Result compute(SystemOfNonlinearEquations system, double x0, double y0, double accuracy, int digitsAfterComma) {
        Kramer kramer = new Kramer();
        Function function1 = system.getFunctions()[0];
        Function function2 = system.getFunctions()[1];

        List<String> headers = List.of("№ шага", "x_i", "y_i", "x_i+1", "y_i+1", "|x_i+1 - x_i|", "|y_i+1 - y_i|");
        List<List<String>> data = new ArrayList<>();

        List<String> row;
        int counter = 0;
        while (true) {
            counter++;
            row = new ArrayList<>();
            row.add(String.format("%d", counter));
            row.add(String.format("%." + digitsAfterComma + "f", x0));
            row.add(String.format("%." + digitsAfterComma + "f", y0));

            double[] d = kramer.compute(
                    new double[]{function1.computeDerivativeX(x0, y0), function1.computeDerivativeY(x0, y0), -function1.compute(x0, y0)},
                    new double[]{function2.computeDerivativeX(x0, y0), function2.computeDerivativeY(x0, y0), -function2.compute(x0, y0)}
            );

            if (Double.isInfinite(d[0]) || Double.isInfinite(d[1]) || Double.isNaN(d[0]) || Double.isNaN(d[1])) {
                x0 += 0.1d;
                y0 += 0.1d;
                continue;
            }

            x0 += d[0];
            y0 += d[1];
            row.add(String.format("%." + digitsAfterComma + "f", x0));
            row.add(String.format("%." + digitsAfterComma + "f", y0));
            row.add(String.format("%." + digitsAfterComma + "f", Math.abs(d[0])));
            row.add(String.format("%." + digitsAfterComma + "f", Math.abs(d[1])));
            data.add(row);

            if (Math.abs(d[0]) <= accuracy && Math.abs(d[1]) <= accuracy)
                break;
        }

        return new Result(headers, data);
    }

    @Override
    public String toString() {
        return "Метод Ньютона";
    }
}
