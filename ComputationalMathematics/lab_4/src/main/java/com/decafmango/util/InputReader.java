package com.decafmango.util;

import java.io.File;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class InputReader {

    public List<double[]> readPointsFromTerminal() {
        System.out.println("""
                Введите 8-12 точек в формате:

                x1 y1
                x2 y2
                 ...
                xn yn
                
                Для прекращения ввода нажмите Enter.
                Для завершения работы программы введите exit.
                """);
        List<double[]> points = new ArrayList<>();
        while (true) {
            try {
                Scanner scanner = new Scanner(System.in);
                String buffer = scanner.nextLine();
                if (buffer.equals("exit"))
                    System.exit(0);
                if (buffer.isBlank()) {
                    if (points.size() < 8) {
                        System.out.println("Требуется ввести, как минимум, 8 точек.\n" + 
                                            "Введите еще " + (8 - points.size()) + ".");
                        continue;
                    }
                    break;
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
                Double x = Double.parseDouble(parts[0]);
                Double y = Double.parseDouble(parts[1]);
                points.add(new double[] {x, y});
                if (points.size() == 12) 
                    break;
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println("Координата должна быть числом.");
            }
        }
        return points;
    }

    public List<double[]> readPointsFromFile(File file) {    
        List<String> lines = new ArrayList<>();
        List<double[]> points = new ArrayList<>();
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
            if (lines.size() < 8 || lines.size() > 12) {
                System.out.println("Данная программа принимает 8-12 точек.");
                System.exit(0);
            }
        } catch (Exception exception) {}

        try {
            for (String line : lines) {
                String[] parts = line.split(" ");
                if (parts.length != 2) {
                    System.out.println("Любая точка должна содержать по две координаты.");
                    continue;
                }
                parts[0] = parts[0].replace(",", ".");
                parts[1] = parts[1].replace(",", ".");
                Double x = Double.parseDouble(parts[0]);
                Double y = Double.parseDouble(parts[1]);
                points.add(new double[] {x, y});
            }
        } catch (NumberFormatException e) {
            System.out.println("Любая координата должна быть числом.");
            System.exit(0);
        }
        return points;
    }

}
