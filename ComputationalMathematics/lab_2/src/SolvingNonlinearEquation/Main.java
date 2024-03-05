package SolvingNonlinearEquation;

import SolvingNonlinearEquation.functions.Function;
import SolvingNonlinearEquation.functions.Function1;
import SolvingNonlinearEquation.functions.Function2;
import SolvingNonlinearEquation.functions.Function3;
import SolvingNonlinearEquation.methods.*;

import javax.swing.*;

public class Main {

    private static final Function[] functions = {
            new Function1(),
            new Function2(),
            new Function3()
    };

    private static final Method[] methods = {
            new ChordMethod(),
            new NewtonsMethod(),
            new SimpleIterationMethod()
    };

    public static void main(String[] args) {
        InputReader inputReader = new InputReader();

        System.out.println("Список доступных фукнций:");

        for (int i = 0; i < functions.length; i++)
            System.out.println((i + 1) + ") " + functions[i].toString());

        int functionIndex = inputReader.readIndex("Введите номер функции: ", "Функции под таким номером не существует.", functions.length);

        Function function = functions[functionIndex];

        GraphPrinter graphPrinter = new GraphPrinter(function);
        graphPrinter.setVisible(true);

        double left;
        double right;
        while (true) {
            left = inputReader.readDouble("Введите левую границу интервала: ");
            right = inputReader.readDouble("Введите правую границу интервала: ");
            if (function.checkIsolationInterval(left, right))
                break;
        }

        System.out.println("Список доступных методов: ");
        for (int i = 0; i < methods.length; i++)
            System.out.println((i + 1) + ") " + methods[i].toString());

        int methodIndex = inputReader.readIndex("Введите номер метода: ", "Метода под таким номером не существует.", methods.length);
        Method method = methods[methodIndex];

        double accuracy = inputReader.readPositiveDouble("Введите точность: ");
        int digitsAfterComma = inputReader.readPositiveInt("Введите кол-во знаков после запятой: ");

        Result result = method.compute(function, left, right, accuracy, digitsAfterComma);
        TableGenerator generator = new TableGenerator();
        String tableResult = generator.generate(result);
        String stringBuilder = "Уравнение: " +
                function.toString() +
                "\n" +
                "Метод: " +
                method.toString() +
                "\n" +
                tableResult;
        OutputWriter outputWriter = new OutputWriter();
        outputWriter.output(stringBuilder);

        System.exit(0);
    }
}
