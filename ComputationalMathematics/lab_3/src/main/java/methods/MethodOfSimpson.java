package methods;

import functions.Function;
import util.Result;

public class MethodOfSimpson extends Method {

    @Override
    public Result compute(Function function, double a, double b, double accuracy) {
        long partition1 = START_PARTITION;
        long partition2 = START_PARTITION * 2;

        double res1 = computeRes(function, a, b, partition1);
        double res2 = computeRes(function, a, b, partition2);
        while (true) {
            res2 = computeRes(function, a, b, partition2);

            if (Math.abs(res2 - res1) < accuracy)
                break;

            partition1 = partition2;
            partition2 *= 2;
            res1 = res2;
        }

        return new Result(res2, partition2);
    }

    @Override
    double computeRes(Function function, double a, double b, long partition) {
        double x, res, h;
        res = 0;
        h = (b - a) / partition;

        x = a;
        res += function.compute(x);

        for (int i = 1; i < partition; i += 2) {
            x = a + h * i;
            res += 4 * function.compute(x);
        }

        for (int i = 2; i < partition; i += 2) {
            x = a + h * i;
            res += 2 * function.compute(x);
        }

        x = b;
        function.compute(x);

        return h / 3 * res;
    }

    @Override
    public String toString() {
        return "Метод Симпсона";
    }
}
