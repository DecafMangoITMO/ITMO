import functions.*;
import methods.*;
import util.InputReader;
import util.Result;
import util.TableGenerator;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final Function[] functions = {
            new Function1(),
            new Function2(),
            new Function3(),
            new Function4(),
            new Function5()
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

        double a, b;
        while (true) {
            a = inputReader.readDouble("Введите левую границу интервала: ");
            b = inputReader.readDouble("Введите правую границу интервала: ");
            if (a < b)
                break;
            System.out.println("Левая граница должна быть меньше правой.");
        }

        double accuracy;
        int digitsAfterComma;
        while (true) {
            accuracy = inputReader.readPositiveDouble("Введите точность: ");
            digitsAfterComma = String.valueOf(accuracy - (double)((long) accuracy) + 1).length() - 2;
            if (digitsAfterComma < 6)
                break;
            if (!inputReader.readYesNo("Заявленная точность слишком большая - программа может долго выполняться.\nВы хотите изменить точность?"))
                break;
        }

        List<String> headers = List.of("Метод", "Результат", "Кол-во разбиений");
        List<List<String>> data = new ArrayList<>();

        List<String> row;
        for (Method method : methods) {
            row = new ArrayList<>();
            Result result = method.compute(function, a, b, accuracy);
            row.add(method.toString());
            row.add(String.format("%." + digitsAfterComma + "f", result.getResult()));
            row.add(String.format("%d", result.getPartition()));
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
