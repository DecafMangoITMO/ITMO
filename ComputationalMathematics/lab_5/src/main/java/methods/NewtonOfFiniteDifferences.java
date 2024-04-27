package methods;

import util.TableGenerator;

import java.util.ArrayList;
import java.util.List;

public class NewtonOfFiniteDifferences extends Method {

    private final double h;

    public NewtonOfFiniteDifferences(double[] x, double[] y, double h) {
        super(x, y, x.length);

        this.h = h;
    }

    @Override
    public double calculate(double value) {
        double res = 0;

        for (int k = 0; k < getN(); k++) {
            double part = a(k);

            for (int j = 0; j < k; j++) {
                part *= value - getX()[j];
            }

            res += part;
        }

        return res;
    }

    public String getFiniteDifferencesTable() {
        List<String> headers = new ArrayList<>();
        headers.add("№");
        headers.add("xi");
        headers.add("yi");
        for (int i = 0; i < getN() - 1; i++)
            headers.add("∆" + (i == 0 ? "" : i + 1) + "yi");

        List<List<String>> data = new ArrayList<>();
        List<String> row;
        for (int i = 0; i < getN(); i++) {
            row = new ArrayList<>();
            row.add(String.valueOf(i));
            row.add(String.valueOf(getX()[i]));
            for (int j = 0; j < getN(); j++) {
                if (j < (getN() - i))
                    row.add(String.valueOf(f(i, j)));
                else
                    row.add(" ");
            }
            data.add(row);
        }

        TableGenerator tableGenerator = new TableGenerator();
        return tableGenerator.generate(headers, data);
    }

    private double a(int k) {
        return f(0, k) / (fact(k) * Math.pow(h, k));
    }

    private double f(int i, int k) {
        if (k == 0) return getY()[i];
        return f(i + 1, k - 1) - f(i, k - 1);
    }

    private int fact(int k) {
        if (k == 0) return 1;

        int res = 1;

        for (int i = 1; i <= k; i++)
            res *= i;

        return res;
    }
}
