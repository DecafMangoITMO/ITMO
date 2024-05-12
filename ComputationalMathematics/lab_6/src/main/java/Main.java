import diff_functions.DiffFunction;
import diff_functions.DiffFunction1;
import diff_functions.DiffFunction2;
import diff_functions.DiffFunction3;
import methods.*;
import util.GraphPrinter;
import util.InputReader;
import util.TableGenerator;

public class Main {

    private static final DiffFunction[] diffFunctions = {
            new DiffFunction1(),
            new DiffFunction2(),
            new DiffFunction3()
    };

    private static final Method[] methods = {
            new Euler(),
            new RungeKutta4(),
            new Milne()
    };

    public static void main(String[] args) {
        InputReader inputReader = new InputReader();

        System.out.println("Список доступных дифференциальных уравнений:");
        for (int i = 0; i < diffFunctions.length; i++)
            System.out.println("\t" + (i + 1) + ") " + diffFunctions[i]);
        DiffFunction diffFunction = diffFunctions[inputReader.readIndex("Выберите дифференциальное уравнение: ", "Дифференциального уравнения с таким номером не существует.", diffFunctions.length)];

        System.out.println("Список доступных методов: ");
        for (int i = 0; i < methods.length; i++)
            System.out.println("\t" + (i + 1) + ") " + methods[i]);
        Method method = methods[inputReader.readIndex("Выберите метод: ", "Метода с таким номером не существует.", methods.length)];

        double x0, y0, xn;
        while (true) {
            x0 = inputReader.readDouble("Введите начальное условие для x0: ");
            y0 = inputReader.readDouble("Введите начальное условие для y0: ");
            xn = inputReader.readDouble("Введите xn: ");
            if (x0 < xn)
                break;
            System.out.println("xn должен быть больше x0.");
        }

        int n;
        while (true) {
            n = inputReader.readPositiveInt("Введите начальное количество узлов: ");
            if (n >= 2)
                break;
            System.out.println("Количество узлов должно быть не меньше двух.");
        }

        double e = inputReader.readPositiveDouble("Введите точность: ");

        Result result = method.calculate(x0, y0, xn, n, e, diffFunction);
        TableGenerator tableGenerator = new TableGenerator();
        System.out.println(tableGenerator.generate(result.getHeaders(), result.getData()));
        GraphPrinter graphPrinter = new GraphPrinter(x0, y0, diffFunction, result);
        graphPrinter.setVisible(true);
    }

}
