package com.decafmango;

import java.io.File;
import java.util.List;

import com.decafmango.approximation.Approximation;
import com.decafmango.approximation.ExponentialApproximation;
import com.decafmango.approximation.LinearApproximation;
import com.decafmango.approximation.LogarithmicApproximation;
import com.decafmango.approximation.PowerApproximation;
import com.decafmango.approximation.QuadraticApproximation;
import com.decafmango.util.ApproximationPrinter;
import com.decafmango.util.InputReader;
import com.decafmango.util.OutputWriter;;

public class Main {
    public static void main(String[] args) {
        boolean readFromFile = false, writeToFile = false;
        File source = null, target = null;
        if (args.length == 0) {
            readFromFile = false;
            writeToFile = false;
        } else {
            String flag = args[0];
            switch (flag) {
                case "-r":
                    if (args.length != 2) {
                        System.out.println("После флага чтения должен быть указан только файл для чтения.");
                        System.exit(0);
                    }
                    readFromFile = true;
                    writeToFile = false;
                    source = new File(args[1]);
                    break;
                case "-w":
                    if (args.length != 2) {
                        System.out.println("После флага записи должен быть указан только файл для записи.");
                        System.exit(0);
                    }
                    readFromFile = false;
                    writeToFile = true;
                    target = new File(args[1]);
                    break;
                case "-rw":
                    if (args.length != 3) {
                        System.out.println("После флага чтения и записи должны быть только указаны последовательно файлы для чтения и записи.");
                        System.exit(0);
                    }
                    readFromFile = true;
                    writeToFile = true;
                    source = new File(args[1]);
                    target = new File(args[2]);
                    break;
                default:
                    System.out.println("""
                            Неизвестный флаг.
                            Данная программа может принимать следующие флаги:
                            -> -r (Чтение из указанного файла)
                            -> -w (Запись в указанный файл)
                            -> -rw (Чтение из первого указанного файла и запись во второй указанный файл)
                            """);
                    System.exit(0);
            }
        }

        if (readFromFile) {
            if (!source.exists()) {
                System.out.println("Указанного файла для чтения не существует.");
                System.exit(0);
            }
            if (!source.canRead()) {
                System.out.println("Указанный файл для чтения нельзя прочитать - проверьте права доступа.");
                System.exit(0);
            }
        }
        if (writeToFile) {
            if (target.exists() && !target.canWrite()) {
                System.out.println("В указанный файл для записи нельзя записать - проверьте права доступа.");
                System.exit(0);
            }
        }

        InputReader reader = new InputReader();
        List<double[]> points = null;

        if (readFromFile)
            points = reader.readPointsFromFile(source);
        else
            points = reader.readPointsFromTerminal();

        String res;

        Approximation linearApproximation = new LinearApproximation(points);
        Approximation quadraticApproximation = new QuadraticApproximation(points);
        Approximation powerApproximation = new PowerApproximation(points);
        Approximation exponentialApproximation = new ExponentialApproximation(points);
        Approximation logarithmicApproximation = new LogarithmicApproximation(points);

        res = linearApproximation.toString() + "\n\n" + quadraticApproximation.toString() + "\n\n" + powerApproximation.toString() + "\n\n" + exponentialApproximation.toString() + "\n\n" + logarithmicApproximation.toString() + "\n\n";

        double maxDeterminationCoefficient = 0;
        boolean maxDeterminationCoefficientIsFound = false;
        if (linearApproximation.isCorrect()) {
            maxDeterminationCoefficient = linearApproximation.calculateDeterminationCoefficient();
            maxDeterminationCoefficientIsFound = true;
        }
        if (maxDeterminationCoefficientIsFound && quadraticApproximation.isCorrect() && quadraticApproximation.calculateDeterminationCoefficient() > maxDeterminationCoefficient)
            maxDeterminationCoefficient = quadraticApproximation.calculateDeterminationCoefficient();
        if (!maxDeterminationCoefficientIsFound && quadraticApproximation.isCorrect()) {
            maxDeterminationCoefficient = quadraticApproximation.calculateDeterminationCoefficient();
            maxDeterminationCoefficientIsFound = true;
        }
        if (maxDeterminationCoefficientIsFound && powerApproximation.isCorrect() && powerApproximation.calculateDeterminationCoefficient() > maxDeterminationCoefficient)
            maxDeterminationCoefficient = powerApproximation.calculateDeterminationCoefficient();
        if (!maxDeterminationCoefficientIsFound && powerApproximation.isCorrect()) {
            maxDeterminationCoefficient = powerApproximation.calculateDeterminationCoefficient();
            maxDeterminationCoefficientIsFound = true;
        }
        if (maxDeterminationCoefficientIsFound && exponentialApproximation.isCorrect() && exponentialApproximation.calculateDeterminationCoefficient() > maxDeterminationCoefficient)
            maxDeterminationCoefficient = exponentialApproximation.calculateDeterminationCoefficient();
        if (!maxDeterminationCoefficientIsFound && exponentialApproximation.isCorrect()) {
            maxDeterminationCoefficient = exponentialApproximation.calculateDeterminationCoefficient();
            maxDeterminationCoefficientIsFound = true;
        }
        if (maxDeterminationCoefficientIsFound && logarithmicApproximation.isCorrect() && logarithmicApproximation.calculateDeterminationCoefficient() > maxDeterminationCoefficient)
            maxDeterminationCoefficient = logarithmicApproximation.calculateDeterminationCoefficient();
        if (!maxDeterminationCoefficientIsFound && logarithmicApproximation.isCorrect())
            maxDeterminationCoefficient = logarithmicApproximation.calculateDeterminationCoefficient();


        res += "Наиболее точной аппроксимацией обладают модели:\n";
        if (linearApproximation.isCorrect() && linearApproximation.calculateDeterminationCoefficient() == maxDeterminationCoefficient)
            res += "-> " + linearApproximation.getName() + "\n";
        if (quadraticApproximation.isCorrect() && quadraticApproximation.calculateDeterminationCoefficient() == maxDeterminationCoefficient)
            res += "-> " + quadraticApproximation.getName() + "\n";
        if (powerApproximation.isCorrect() && powerApproximation.calculateDeterminationCoefficient() == maxDeterminationCoefficient)
            res += "-> " + powerApproximation.getName() + "\n";
        if (exponentialApproximation.isCorrect() && exponentialApproximation.calculateDeterminationCoefficient() == maxDeterminationCoefficient)
            res += "-> " + exponentialApproximation.getName() + "\n";
        if (logarithmicApproximation.isCorrect() && logarithmicApproximation.calculateDeterminationCoefficient() == maxDeterminationCoefficient)
            res += "-> " + logarithmicApproximation.getName() + "\n";

        OutputWriter writer = new OutputWriter();
        if (writeToFile)
            writer.writeToFile(res, target);
        else
            writer.writeToTerminal(res);

        if (linearApproximation.isCorrect())
            System.out.println("Красная линия - линейная функция");
        if (quadraticApproximation.isCorrect())
            System.out.println("Желтая линия - квадратичная функция");
        if (powerApproximation.isCorrect())
            System.out.println("Зеленая линия - степенная функция");
        if (exponentialApproximation.isCorrect())
            System.out.println("Синяя линия - экспоненциальная функция");
        if (logarithmicApproximation.isCorrect())
            System.out.println("Розовая линия - логарифмическая функция");

        ApproximationPrinter printer = new ApproximationPrinter(points, linearApproximation, quadraticApproximation, powerApproximation, exponentialApproximation, logarithmicApproximation);
        printer.setVisible(true);
    }
}