import functions.*;
import methods.*;
import util.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    private static final Function[] functions = {
            new Function1(),
            new Function2(),
            new Function3(),
            new Function4(),
            new Function5(),
            new Function6()
    };

    private static final Method[] methods = {
            new MethodOfLeftRectangles(),
            new MethodOfRightRectangles(),
            new MethodOfCenterRectangles(),
            new MethodOfTrapezoids(),
            new MethodOfSimpson()
    };

    public static void main(String[] args) {
        System.out.println("""
                        ,--.           ,--.    ,----. \s
                        |  |    ,--,--.|  |-.  '.-.  |\s
                        |  |   ' ,-.  || .-. '   .' < \s
                        |  '--.\\ '-'  || `-' | /'-'  |\s
                        `-----' `--`--' `---'  `----'\s
                Программа по вычислению определенных интегралов.
                    Для выхода из программы напишите exit.
                """);

        System.out.println("Список доступных функций:");
        for (Function function : functions)
            System.out.println("--> " + function);

        System.out.println(" ");
        InputReader inputReader = new InputReader();
        Function function = functions[inputReader.readIndex("Введите номер функции: ", "Функции под таким номером не существует.", functions.length)];

        DomainChecker domainChecker = new DomainChecker();
        double a, b;
        while (true) {
            a = inputReader.readDouble("Введите левую границу интервала: ");
            b = inputReader.readDouble("Введите правую границу интервала: ");
            if (a < b) {
                if (domainChecker.checkDomain(function, a, b))
                    break;
                else
                    System.out.println("Указанный интервал не входит в область определения функции.");
            } else
                System.out.println("Левая граница должна быть меньше правой.");
        }

        ConvergenceChecker convergenceChecker = new ConvergenceChecker();
        List<double[]> integrationIntervals = convergenceChecker.checkConvergence(function, a, b);
        if (integrationIntervals.isEmpty()) {
            System.out.println(" ");
            System.out.println("Данный интеграл не сходится.");
            return;
        }

        double accuracy;
        int digitsAfterComma;
        while (true) {
            accuracy = inputReader.readPositiveDouble("Введите точность: ");
            digitsAfterComma = String.valueOf(accuracy - (double)((long) accuracy) + 1).length() - 2;
            if (inputReader.readYesNo("Учтите, что при большой точности и длинном интервале интегрирования подсчет может выполняться достаточно долго.\nВы хотите оставить текущее значение точности?"))
                break;
        }

        List<String> headers = List.of("Метод", "Результат", "Кол-во разбиений");
        List<List<String>> data = new ArrayList<>();

        List<String> row;
        for (Method method : methods) {
            row = new ArrayList<>();
            double result = 0;
            long partition = 0;
            for (double[] integrationInterval : integrationIntervals) {
                Result res = method.compute(function, integrationInterval[0], integrationInterval[1], accuracy);
                result += res.getResult();
                partition += res.getPartition();
            }
            row.add(method.toString());
            row.add(String.format("%." + digitsAfterComma + "f", result));
            row.add(String.format("%d", partition));
            data.add(row);
        }

        System.out.println(" ");
        TableGenerator tableGenerator = new TableGenerator();
        StringBuilder builder = new StringBuilder();
        builder
                .append("Функция: ").append(function.toString()).append("\n")
                .append("Интервал: [").append(a).append("; ").append(b).append("]\n")
                .append("Точность: ").append(String.format("%f", accuracy)).append("\n")
                .append(tableGenerator.generate(headers, data));
        System.out.println(builder.toString());
    }

}
