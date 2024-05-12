package methods;

import diff_functions.DiffFunction;

import java.util.ArrayList;
import java.util.List;

public class RungeKutta4 extends Method {

    @Override
    public Result calculate(double x0, double y0, double xn, int n, double e, DiffFunction diffFunction) {
        Result result1, result2;

        while (true) {
            result1 = calculateMethod(x0, y0, xn, n, diffFunction);
            result2 = calculateMethod(x0, y0, xn, (n - 1) * 2 + 1, diffFunction);

            if ((Math.abs(result1.getY().get(n - 1) - result2.getY().get((n - 1) * 2))) / 15 <= e)
                return result1;

            n = (n - 1) * 2 + 1;
        }
    }

    private Result calculateMethod(double x0, double y0, double xn, int n, DiffFunction diffFunction) {
        List<Double> x;
        List<Double> y;
        x = new ArrayList<>();
        y = new ArrayList<>();
        x.add(x0);
        y.add(y0);

        List<String> headers = List.of(
                "i",
                "xi",
                "yi"
        );
        List<List<String>> data = new ArrayList<>();
        List<String> row = new ArrayList<>();
        row.add(String.format("%d", 0));
        row.add(String.format("%f", x0));
        row.add(String.format("%f", y0));
        data.add(row);

        double currX = x0, currY = y0, h = (xn - x0) / (n - 1);
        double k1, k2, k3, k4;
        for (long i = 0; i < n - 1; i++) {
            k1 = h * diffFunction.f(currX, currY);
            k2 = h * diffFunction.f(currX + h / 2, currY + k1 / 2);
            k3 = h * diffFunction.f(currX + h / 2, currY + k2 / 2);
            k4 = h * diffFunction.f(currX + h, currY + k3);
            currY = currY + (k1 + 2 * k2 + 2 * k3 + k4) / 6;
            currX += h;
            x.add(currX);
            y.add(currY);
            row = new ArrayList<>();
            row.add(String.format("%d", i + 1));
            row.add(String.format("%f", currX));
            row.add(String.format("%f", currY));
            data.add(row);
        }

        return new Result(x, y, headers, data);
    }

    @Override
    public String toString() {
        return "Метод Рунге-Кутта IV-порядка";
    }
}