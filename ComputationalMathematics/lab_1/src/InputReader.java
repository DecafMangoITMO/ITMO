import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class InputReader {

    public Data readFromTerminal() {
        int n = readPositiveInt("Введите размерность матрицы: ");

        double accuracy = readPositiveDouble("Введите точность: ");

        System.out.println("""
                Введите коэффициенты  построчно.
                Например, если ваш имеет вид:
                a11 a12 | b1
                a21 a22 | b2
                Ввод будет следующим:
                a11 a12 b1
                a21 a22 b2""");
        List<List<Double>> A = new ArrayList<>();
        List<Double> B = new ArrayList<>();
        String buffer;
        Scanner scanner = new Scanner(System.in);
        repeat:
        while (true) {
            for (int i = 0; i < n; i++) {
                List<Double> row = new ArrayList<>();
                for (int j = 0; j < n + 1; j++) {
                    try {
                        buffer = scanner.next();
                        buffer = buffer.replaceAll(",", ".");
                        if (j == n)
                            B.add(Double.parseDouble(buffer));
                        else
                            row.add(Double.parseDouble(buffer));
                    } catch (InputMismatchException | NumberFormatException e) {
                        System.out.println("При вводе " + (i * n + j + 2) + " элемента произошла ошибка. Требуется ввести число.");
                        scanner = new Scanner(System.in);
                        A.clear();
                        B.clear();
                        continue repeat;
                    }
                }
                A.add(row);
            }
            break;
        }

        return new Data(A, B, accuracy);
    }

    public Data readFromFile(String filename) {
        File file = new File(filename);

        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("Указанного файла не существует.");
            System.exit(1);
        }

        int n = 0;
        try {
            n = scanner.nextInt();
            if (n <= 0) {
                System.out.println("Требуется положительная размерность матрицы.");
                System.exit(1);
            }
        } catch (InputMismatchException | NumberFormatException e) {
            System.out.println("При чтении размерности матрицы произошла ошибка. Требуется целое число");
            System.exit(1);
        }

        String buffer;

        double accuracy = 0;
        try {
            buffer = scanner.next();
            buffer = buffer.replaceAll(",", ".");
            accuracy = Double.parseDouble(buffer);
            if (accuracy <= 0) {
                System.out.println("Требуется положительная точность.");
                System.exit(1);
            }
        } catch (InputMismatchException | NumberFormatException e) {
            System.out.println("При вводе размерности матрицы произошла ошибка. Требуется ввести целое число.");
            System.exit(1);
        }

        List<List<Double>> A = new ArrayList<>();
        List<Double> B = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            List<Double> row = new ArrayList<>();
            for (int j = 0; j < n + 1; j++) {
                try {
                    buffer = scanner.next();
                    buffer = buffer.replaceAll(",", ".");
                    if (j == n)
                        B.add(Double.parseDouble(buffer));
                    else
                        row.add(Double.parseDouble(buffer));
                } catch (InputMismatchException | NumberFormatException e) {
                    System.out.println("При вводе " + (i * n + j + 2) + " элемента произошла ошибка. Требуется ввести число.");
                    System.exit(1);
                } catch (NoSuchElementException e) {
                    System.out.println("Вы ввели недостаточно элементов. Ошибка произошла при чтении " + (i * n + j + 2) + " элемента.");
                    System.exit(1);
                }
            }
            A.add(row);
        }

        return new Data(A, B, accuracy);
    }

    public int readPositiveInt(String message) {
        while (true) {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.print(message);
                String buffer = scanner.next();
                if (buffer.equals("exit"))
                    System.exit(0);
                int value = Integer.parseInt(buffer);
                if (value > 0)
                    return value;
                System.out.println("Требуется ввести положительное число.");
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println("Требуется ввести целое число.");
            }
        }
    }

    public double readPositiveDouble(String message) {
        while (true) {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.print(message);
                String buffer = scanner.next();
                if (buffer.equals("exit"))
                    System.exit(0);
                buffer = buffer.replaceAll(",", ".");
                double value = Double.parseDouble(buffer);
                if (value > 0)
                    return value;
                System.out.println("Требуется ввести положительное число.");
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println("Требуется ввести число.");
            }
        }
    }

}