package methods;

import functions.Function;
import util.Result;

public class MethodOfCenterRectangles extends Method {

    @Override
    public Result compute(Function function, double a, double b, double accuracy) {
        long partition1 = START_PARTITION;
        long partition2 = START_PARTITION * 2;

        double res1, res2;
        res1 = computeRes(function, a, b, partition1);
        res2 = computeRes(function, a, b, partition2);
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

    double computeRes(Function function, double a, double b, long partition) {
        double x, h, res;
        res = 0;
        h = (b - a) / partition;
        for (int i = 0; i < partition; i++) {
            x = a + h * i;
            res += h * function.compute(x + h / 2);
        }
        return res;
    }

    @Override
    public String toString() {
        return "Метод центральных прямоугольников";
    }
}
