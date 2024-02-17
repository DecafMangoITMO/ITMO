import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Calculation {

    // метод для поиска определителя матрицы
    public double getDeterminant(List<List<Double>> matrix) {
        int n = matrix.size();
        double det = 1.0;
        List<List<Double>> mat = cloneMatrix(matrix);

        for (int i = 0; i < n; i++) {
            double pivot = mat.get(i).get(i);
            if (pivot == 0.0) {
                return 0.0;
            }

            det *= pivot;

            for (int j = i + 1; j < n; j++) {
                double factor = mat.get(j).get(i) / pivot;
                for (int k = i; k < n; k++) {
                    double val = mat.get(j).get(k) - factor * mat.get(i).get(k);
                    mat.get(j).set(k, val);
                }
            }
        }

        return det;
    }

    private List<List<Double>> cloneMatrix(List<List<Double>> original) {
        List<List<Double>> clone = new ArrayList<>(original.size());
        for (List<Double> row : original) {
            List<Double> newRow = new ArrayList<>(row);
            clone.add(newRow);
        }
        return clone;
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
