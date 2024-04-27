package util;

import java.io.File;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class InputReader {

    public int readIndex(String message, String notFoundMessage, int length) {
        while (true) {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.print(message);
                String buffer = scanner.next();
                if (buffer.equals("exit"))
                    System.exit(0);
                int value = Integer.parseInt(buffer);
                if (value <= 0) {
                    System.out.println("Требуется ввести положительное число.");
                    continue;
                }
                if (value - 1 >= length) {
                    System.out.println(notFoundMessage);
                    continue;
                }
                return value;
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println("Требуется ввести целое число.");
            }
        }
    }

    public File readFilename() {
        while (true) {
            System.out.print("Введите путь до файла:\n>> ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.next();
            if (input.equals("exit"))
                System.exit(0);
            File file = new File(input);
            if (file.exists())
                return file;
            System.out.println("Указанный файл не найден, попробуйте еще раз.");
        }

    }

    public double readDouble(String message) {
        while (true) {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.print(message);
                String buffer = scanner.next();
                if (buffer.equals("exit"))
                    System.exit(0);
                buffer = buffer.replaceAll(",", ".");
                return Double.parseDouble(buffer);
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println("Требуется ввести число.");
            }
        }
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

    public Points readPointsFromTerminal() {
        System.out.println("""
                Введите 8-12 точек в формате:

                x1 y1
                x2 y2
                 ...
                xn yn
                
                Для прекращения ввода нажмите Enter.
                Для завершения работы программы введите exit.
                """);
        List<Double> x = new ArrayList<>();
        List<Double> y = new ArrayList<>();
        while (true) {
            try {
                Scanner scanner = new Scanner(System.in);
                String buffer = scanner.nextLine();
                if (buffer.equals("exit"))
                    System.exit(0);
                if (buffer.isBlank()) {
                    if (!x.isEmpty())
                        break;
                    System.out.println("Требуется ввести хотя бы одну точку.");
                    continue;
                }
                buffer = buffer.strip();
                while (buffer.contains("  "))
                    buffer = buffer.replaceAll("  ", " ");
                String[] parts = buffer.split(" ");
                if (parts.length != 2) {
                    System.out.println("Требуется ввести две координаты точки.");
                    continue;
                }
                parts[0] = parts[0].replace(",", ".");
                parts[1] = parts[1].replace(",", ".");
                x.add(Double.parseDouble(parts[0]));
                y.add(Double.parseDouble(parts[1]));
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println("Координата должна быть числом.");
            }
        }

        double[] resX = new double[x.size()];
        double[] resY = new double[y.size()];

        for (int i = 0; i < x.size(); i++) {
            resX[i] = x.get(i);
            resY[i] = y.get(i);
        }

        return new Points(resX, resY);
    }

    public Points readPointsFromFile(File file) {
        List<String> lines = new ArrayList<>();
        List<Double> x = new ArrayList<>();
        List<Double> y = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(file);
            String buffer;
            while (scanner.hasNextLine()) {
                buffer = scanner.nextLine();
                if (buffer.isBlank())
                    continue;
                buffer = buffer.strip();
                while (buffer.contains("  "))
                    buffer = buffer.replaceAll("  ", " ");
                lines.add(buffer);
            }
        } catch (Exception exception) {}

        if (lines.isEmpty()) {
            System.out.println("Требуется ввести хотя бы одну точку.");
            System.exit(0);
        }

        try {
            for (String line : lines) {
                String[] parts = line.split(" ");
                if (parts.length != 2) {
                    System.out.println("Любая точка должна содержать по две координаты.");
                    continue;
                }
                parts[0] = parts[0].replace(",", ".");
                parts[1] = parts[1].replace(",", ".");
                x.add(Double.parseDouble(parts[0]));
                y.add(Double.parseDouble(parts[1]));
            }
        } catch (NumberFormatException e) {
            System.out.println("Любая координата должна быть числом.");
            System.exit(0);
        }

        double[] resX = new double[x.size()];
        double[] resY = new double[y.size()];

        for (int i = 0; i < x.size(); i++) {
            resX[i] = x.get(i);
            resY[i] = y.get(i);
        }

        return new Points(resX, resY);
    }



}
