package methods;

import diff_functions.DiffFunction;

import java.util.ArrayList;
import java.util.List;

public class Milne extends Method {

    @Override
    public Result calculate(double x0, double y0, double xn, int n, double e, DiffFunction diffFunction) {
        List<Double> x = new ArrayList<>();
        List<Double> y = new ArrayList<>();
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

        double h = (xn - x0) / (n - 1);

        for (int i = 1; i < n; i++) {
            if (i == 1 || i == 2 || i == 3) {
                y.add(calculateRungeKutta(x.get(i - 1), y.get(i - 1), h, diffFunction));
                x.add(x.get(i - 1) + h);
                row = new ArrayList<>();
                row.add(String.format("%d", i));
                row.add(String.format("%f", x.get(i)));
                row.add(String.format("%f", y.get(i)));
                data.add(row);
                continue;
            }

            x.add(x.get(i - 1) + h);
            double approximatedY = y.get(i - 4) + 4 * h / 3 * (2 * diffFunction.f(x.get(i - 3), y.get(i - 3)) - diffFunction.f(x.get(i - 2), y.get(i - 2)) + 2 * 2 * diffFunction.f(x.get(i - 1), y.get(i - 1)));
            double accurateY = diffFunction.y(x.get(i), x0, y0);
            while (true) {
                if (Math.abs(accurateY - approximatedY) <= e)
                    break;
                approximatedY = y.get(i - 2) + h / 3 * (diffFunction.f(x.get(i - 2), y.get(i - 2)) + 4 * diffFunction.f(x.get(i - 1), y.get(i - 1)) + diffFunction.f(x.get(i), approximatedY));
            }
            y.add(approximatedY);

            row = new ArrayList<>();
            row.add(String.format("%d", i));
            row.add(String.format("%f", x.get(i)));
            row.add(String.format("%f", y.get(i)));
            data.add(row);
        }

        return new Result(x, y, headers, data);
    }

    private double calculateRungeKutta(double x, double y, double h, DiffFunction diffFunction) {
        double k1, k2, k3, k4;

        k1 = h * diffFunction.f(x, y);
        k2 = h * diffFunction.f(x + h / 2, y + k1 / 2);
        k3 = h * diffFunction.f(x + h / 2, y + k2 / 2);
        k4 = h * diffFunction.f(x + h, y + k3);

        return y + (k1 + 2 * k2 + 2 * k3 + k4) / 6;
    }

    @Override
    public String toString() {
        return "Метод Милна";
    }
}
