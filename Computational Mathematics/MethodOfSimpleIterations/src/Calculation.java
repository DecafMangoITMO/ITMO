import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Calculation {

    // метод для вычисления определителя матрицы
    public double getDeterminant(List<List<Double>> matrix) {
        int n = matrix.size();
        double det = 1d;
        List<List<Double>> mat = cloneMatrix(matrix);


        for (int i = 0; i < n; i++) {
            int max = i;

            for (int j = i + 1; j < n; j++) {
                if (Math.abs(mat.get(j).get(i)) > Math.abs(mat.get(max).get(i)))
                    max = j;
            }

            if (mat.get(max).get(i) == 0d)
                return 0d;

            if (max != i) {
                List<Double> buffer = new ArrayList<>(mat.get(i));
                mat.set(i, new ArrayList<>(mat.get(max)));
                mat.set(max, buffer);
                det *= -1d;
            }

            double pivot = mat.get(i).get(i);
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
        List<List<Integer>> rowPositions = new ArrayList<>();
        for (int i = 0; i < A.size(); i++)
            rowPositions.add(new ArrayList<>());

        for (int i = 0; i < A.size(); i++) {
            List<Double> row = A.get(i);

            double max = Math.abs(row.get(0));
            List<Integer> positions = new ArrayList<>();
            positions.add(0);
            double sum = 0;

            for (int j = 1; j < row.size(); j++) {
                double current = Math.abs(row.get(j));
                if (current > max) {
                    sum += max * positions.size();
                    max = current;
                    positions.clear();
                    positions.add(j);
                } else if (current == max) {
                    positions.add(j);
                } else {
                    sum += current;
                }
            }

            if (max < sum + max * (positions.size() - 1)) {
                System.out.print("""
                        Не удалось привести исходную матрицу к матрице с диагональным преобладанием.
                        Предупреждение: при продолжении решения конечный ответ может не сойтись.""");
                InputReader inputReader = new InputReader();
                data.setIterations(inputReader.readPositiveInt("Введите кол-во итераций: "));
                return;
            }

            for (int position : positions)
                rowPositions.get(position).add(i);
        }


        int[] permutation = new int[A.size()];
        List<Integer> placedRows = new ArrayList<>();
        Arrays.fill(permutation, -1);


        for (int i = 0; i < A.size(); i++) {
            List<Integer> rowPosition = rowPositions.get(i);
            if (rowPosition.size() == 1) {
                permutation[i] = rowPosition.get(0);
                placedRows.add(rowPosition.get(0));
            }
        }


        for (int i = 0; i < A.size(); i++) {
            List<Integer> rowPosition = rowPositions.get(i);
            if (rowPosition.size() == 2) {
                if (!placedRows.contains(rowPosition.get(0))) {
                    permutation[i] = rowPosition.get(0);
                    placedRows.add(rowPosition.get(0));
                } else if (!placedRows.contains(rowPosition.get(1))) {
                    permutation[i] = rowPosition.get(1);
                    placedRows.add(rowPosition.get(1));
                } else {
                    System.out.print("""
                            Не удалось привести исходную матрицу к матрице с диагональным преобладанием.
                            Предупреждение: при продолжении решения конечный ответ может не сойтись.""");
                    InputReader inputReader = new InputReader();
                    data.setIterations(inputReader.readPositiveInt("Введите кол-во итераций: "));
                    return;
                }
            }
        }


        List<List<Double>> new_A = new ArrayList<>();
        List<Double> new_B = new ArrayList<>();

        for (int i : permutation) {
            if (i == -1) {
                System.out.print("""
                            Не удалось привести исходную матрицу к матрице с диагональным преобладанием.
                            Предупреждение: при продолжении решения конечный ответ может не сойтись.""");
                InputReader inputReader = new InputReader();
                data.setIterations(inputReader.readPositiveInt("Введите кол-во итераций: "));
                return;
            }
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

        InputReader inputReader = new InputReader();
        if (data.getIterations() == -1)
            data.setIterations(inputReader.readPositiveInt("Введите максимальное кол-во итераций: "));

        while (true) {

            if (iterationCounter != 0 && iterationCounter == data.getIterations()) {
                int c = inputReader.readPositiveInt("Введите кол-во символов после запятой: ");
                System.out.println("Было проведено " + iterationCounter + " итераций. Но ответ так и не найден.");
                for (int i = 0; i < n; i++) {
                    System.out.println("x" + (i + 1) + "=" + String.format("%." + c + "f", newApproximation[i]) + "; Отклонение составляет: " + String.format("%." + c + "f", Math.abs(newApproximation[i] - previousApproximation[i])));
                }
                break;
            }

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
                int c = inputReader.readPositiveInt("Введите кол-во символов после запятой: ");
                System.out.println("Было проведено " + iterationCounter + " итераций.");
                for (int i = 0; i < n; i++) {
                    System.out.println("x" + (i + 1) + "=" + String.format("%." + c + "f", newApproximation[i]) + "; Отклонение составляет: " + String.format("%." + c + "f", Math.abs(newApproximation[i] - previousApproximation[i])));
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