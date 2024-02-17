package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Calculation {

    // метод по проверке вырожденности матрицы
    public double getDeterminant(List<List<Double>> matrix) {
        int n = matrix.size();

        switch (n) {
            case 1:
                return matrix.get(0).get(0);
            case 2:
                return matrix.get(0).get(0) * matrix.get(1).get(1) - matrix.get(0).get(1) * matrix.get(1).get(0);
            default:
                double det = 0;
                for (int i = 0; i < n; i++) {
                    List<List<Double>> minor = new ArrayList<>();
                    for (int j = 1; j < n; j++) {
                        List<Double> row = new ArrayList<>();
                        for (int k = 0; k < n; k++) {
                            if (k != i) {
                                row.add(matrix.get(j).get(k));
                            }
                        }
                        minor.add(row);
                    }
                    det += (Math.pow(-1, i)) * matrix.get(0).get(i) * getDeterminant(minor);
                }
                return det;
        }
    }

    // метод для приведения матрицы к матрице с диагональным преобладанием
    public void toConvergence(Data data) {
        List<List<Double>> A = data.getA();
        List<Double> B = data.getB();
        int[] new_rows_positions = new int[A.size()];
        Arrays.fill(new_rows_positions, -1);

        for (int i = 0; i < A.size(); i++) {
            List<Double> row = A.get(i);

            double max = Math.abs(row.get(0));
            int position = 0;
            double sum = 0;

            for (int j = 1; j < row.size(); j++) {
                double current = Math.abs(row.get(j));
                if (current > max) {
                    sum += max;
                    max = current;
                    position = j;
                } else if (current == max) {
                } else {
                    sum += current;
                }
            }

            if (max < sum || new_rows_positions[position] != -1) {
                System.out.print("""
                        Не удалось привести исходную матрицу к матрице с диагональным преобладанием.
                        Предупреждение: при продолжении решения конечный ответ может не сойтись.""");
                InputReader inputReader = new InputReader();
                data.setIterations(inputReader.readPositiveInt("Введите кол-во итераций: "));
                return;
            }

            new_rows_positions[position] = i;
        }

        List<List<Double>> new_A = new ArrayList<>();
        List<Double> new_B = new ArrayList<>();
        for (int i : new_rows_positions) {
            new_A.add(A.get(i));
            new_B.add(B.get(i));
        }

        data.setA(new_A);
        data.setB(new_B);
    }

    // метод поиска решения с заявленной точностью
    public void iterate(Data data) {
        List<List<Double>> A = data.getA();
        List<Double> B = data.getB();
        int n = data.getA().size();
        double[] previousApproximation = new double[n];
        for (int i = 0; i < n; i++)
            previousApproximation[i] = data.getB().get(i);

        double[] newApproximation = new double[n];
        int iterationCounter = 0;
        while (true) {

            for (int i = 0; i < n; i++) {
                double newValue = B.get(i) / A.get(i).get(i);
                for (int j = 0; j < n; j++) {
                    if (i != j) {
                        newValue -= (A.get(i).get(j) / A.get(i).get(i)) * previousApproximation[j];
                        if (Double.isNaN(newValue) || Double.isInfinite(newValue)) {
                            System.out.println("Данная СЛАУ не обладает сходящимся решением.");
                            System.exit(1);
                        }
                    }
                }
                newApproximation[i] = newValue;
            }
            if (getMaxDeviation(previousApproximation, newApproximation) <= data.getAccuracy()
                    || (data.getIterations() != -1 && data.getIterations() == iterationCounter)) {
                InputReader inputReader = new InputReader();
                int c = inputReader.readPositiveInt("Введите кол-во символов после запятой: ");
                System.out.println("Было проведено " + iterationCounter + " итераций.");
                for (int i = 0; i < n; i++) {
                    System.out.println("x" + (i + 1) + "=" + String.format("%." + c + "f" , newApproximation[i]) + "; Отклонение составляет: " + String.format("%." + c + "f", Math.abs(newApproximation[i] - previousApproximation[i])));
                }
                break;
            }
            for (int i = 0; i < n; i++)
                previousApproximation[i] = newApproximation[i];
            iterationCounter++;
        }
    }

    // метод поиска максимального отклоения следующего приближения
    private double getMaxDeviation(double[] previousApproximation, double[] newApproximation) {
        double maxDeviation = 0;

        for (int i = 0; i < previousApproximation.length; i++) {
            double deviation = Math.abs(previousApproximation[i] - newApproximation[i]);
            if (deviation > maxDeviation)
                maxDeviation = deviation;
        }

        return maxDeviation;
    }
}
