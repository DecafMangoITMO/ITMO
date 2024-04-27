package methods;

import java.util.Arrays;

public class NewtonOfDividedDifferences extends Method {

    public NewtonOfDividedDifferences(double[] x, double[] y) {
        super(x, y, x.length);
    }

    @Override
    public double calculate(double value) {
        double res = 0;

        for (int i = 0; i < getN(); i++) {
            double part = 1;

            int[] indexes = new int[i + 1];
            for (int j = 0; j < i + 1; j++) {
                indexes[j] = j;

                if (j != i)
                    part *= value - getX()[j];
            }

            part *= f(indexes);
            res += part;
        }

        return res;
    }

    private double f(int... indexes) {
        if (indexes.length == 1)
            return getY()[indexes[0]];
        return (f(Arrays.copyOfRange(indexes, 1, indexes.length)) - f(Arrays.copyOfRange(indexes, 0, indexes.length - 1))) / (getX()[indexes[indexes.length - 1]] - getX()[indexes[0]]);
    }
}
