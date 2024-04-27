package methods;

public class Lagrange extends Method {

    public Lagrange(double[] x, double[] y) {
        super(x, y, x.length);
    }

    @Override
    public double calculate(double value) {
        double[] l = new double[getN()];

        for (int i = 0; i < getN(); i++) {
            double li = 1d;

            for (int j = 0; j < getN(); j++) {
                if (i == j)
                    continue;
                li *= value - getX()[j];
            }

            for (int j = 0; j < getN(); j++) {
                if (i == j)
                    continue;
                li /= getX()[i] - getX()[j];
            }

            l[i] = li;
        }

        double res = 0;

        for (int i = 0; i < getN(); i++) {
            res += getY()[i] * l[i];
        }

        return res;
    }
}
