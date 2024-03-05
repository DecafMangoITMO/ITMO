package SolvingNonlinearEquation.methods;

import SolvingNonlinearEquation.functions.Function;

import java.util.ArrayList;
import java.util.List;

public class SimpleIterationMethod implements Method {

    @Override
    public Result compute(Function function, double left, double right, double accuracy, int digitsAfterComma) {
        double x = function.computeEquivalent(left, left, right);

        List<String> headers = List.of("№ итерации", "x_k", "x_k+1", "f(x_k+1)", "|f(x_k+1)|");
        List<List<String>> data = new ArrayList<>();

        List<String> row;
        int counter = 0;
        while (true) {
            row = new ArrayList<>();
            counter++;
            row.add(String.format("%d", counter));
            row.add(String.format("%." + digitsAfterComma + "f", x));
            x = function.computeEquivalent(x, left, right);
            row.add(String.format("%." + digitsAfterComma + "f", x));
            row.add(String.format("%." + digitsAfterComma + "f", function.compute(x)));
            row.add(String.format("%." + digitsAfterComma + "f", Math.abs(function.compute(x))));
            data.add(row);

            if (Math.abs(function.compute(x)) <= accuracy)
                break;
        }

        return new Result(headers, data);
    }

    @Override
    public String toString() {
        return "Метод простой итерации";
    }
}
