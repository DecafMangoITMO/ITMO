package SolvingNonlinearEquation.methods;

import SolvingNonlinearEquation.functions.Function;

import java.util.ArrayList;
import java.util.List;

public class NewtonsMethod implements Method {

    @Override
    public Result compute(Function function, double left, double right, double accuracy, int digitsAfterComma) {
        double x, f, d;

        if (function.compute(left) * function.computeSecondDerivative(left) > 0)
            x = left;
        else
            x = right;

        List<String> headers = List.of("№ итерации", "x_k", "f(x_k)", "f'(x_k)", "x_k+1", "|f(x_k+1)|");
        List<List<String>> data = new ArrayList<>();

        List<String> row;
        int counter = 0;
        while (true) {
            row = new ArrayList<>();
            counter++;
            row.add(String.format("%d", counter));
            row.add(String.format("%." + digitsAfterComma + "f", x));
            f = function.compute(x);
            row.add(String.format("%." + digitsAfterComma + "f", f));
            d = function.computeDerivative(x);
            row.add(String.format("%." + digitsAfterComma + "f", d));
            x = x - f / d;
            row.add(String.format("%." + digitsAfterComma + "f", x));
            data.add(row);
            row.add(String.format("%." + digitsAfterComma + "f", Math.abs(function.compute(x))));

            if (Math.abs(function.compute(x)) <= accuracy)
                break;
        }

        return new Result(headers, data);
    }

    @Override
    public String toString() {
        return "Метод Ньютона";
    }
}
