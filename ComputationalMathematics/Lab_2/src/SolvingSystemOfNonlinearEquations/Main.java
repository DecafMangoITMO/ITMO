package SolvingSystemOfNonlinearEquations;

import SolvingSystemOfNonlinearEquations.functions.*;
import SolvingSystemOfNonlinearEquations.methods.Method;
import SolvingSystemOfNonlinearEquations.methods.NewtonsMethod;
import SolvingSystemOfNonlinearEquations.systems.SystemOfNonlinearEquations;

public class Main {

    private static final SystemOfNonlinearEquations[] systems = new SystemOfNonlinearEquations[] {
            new SystemOfNonlinearEquations(
                    new Function[]{
                            new Function1(),
                            new Function2()
                    }
            ),
            new SystemOfNonlinearEquations(
                    new Function[]{
                            new Function3(),
                            new Function4()
                    }
            )
    };

    public static void main(String[] args) {
        InputReader inputReader = new InputReader();

        System.out.println("Для выхода из программы пропишите exit.");
        System.out.println("Список доступных систем нелинейных уравнений:");

        for (int i = 0; i < systems.length; i++)
            System.out.println((i + 1) + ")\n" + systems[i].toString());

        int systemIndex = inputReader.readIndex("Введите номер системы: ", "Системы под таким номером не сущесвует.", systems.length);
        SystemOfNonlinearEquations system = systems[systemIndex];
        GraphPrinter graphPrinter = new GraphPrinter(systems[systemIndex]);
        graphPrinter.setVisible(true);

        double x0 = inputReader.readDouble("Введите нулевое приближение x0: ");
        double y0 = inputReader.readDouble("Введите нулевое приближение y0: ");
        double accuracy = inputReader.readPositiveDouble("Введите точность: ");
        int digitsAfterComma = inputReader.readPositiveInt("Введите кол-во знаков после запятой: ");

        Method method = new NewtonsMethod();

        TableGenerator tableGenerator = new TableGenerator();
        String resultTable = tableGenerator.generate(method.compute(system, x0, y0, accuracy, digitsAfterComma));
        String result = "Система:\n" + system.toString() + "\nМетод: " + method.toString() + "\n" + resultTable;

        OutputWriter outputWriter = new OutputWriter();
        outputWriter.output(result);

        System.exit(0);
    }
}
