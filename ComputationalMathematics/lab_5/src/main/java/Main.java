import functions.Function;
import functions.Function1;
import functions.Function2;
import methods.Lagrange;
import methods.Method;
import methods.NewtonOfDividedDifferences;
import methods.NewtonOfFiniteDifferences;
import util.GraphPrinter;
import util.InputReader;
import util.Points;

public class Main {

    public static void main(String[] args) {
        System.out.println("""
                             ._     __,
                              |\\,../'\\
                            ,'. .     `.
                           .--         '`.
                          ( `' ,          ;
                          ,`--' _,       ,'\\
                         ,`.____            `.
                        /              `,    |
                       '                \\,   '
                       |                /   /`,
                       `,  .           ,` ./  |
                       ' `.  ,'        |;,'   ,@
                 ______|     |      _________,_____jv______
                        `.   `.   ,'
                         ,'_,','_,
                         `'   `'
                """);
        switch (requestMode()) {
            case 1:
                handleTerminalInput();
                return;
            case 2:
                handleFileInput();
                return;
            case 3:
                handleFunctionInput();
        }
    }

    static int requestMode() {
        InputReader inputReader = new InputReader();
        return inputReader.readIndex("""
                Введите способ ввода точек:
                \t1. Ввод из терминала
                \t2. Ввод из файла
                \t3. Ввод через функцию
                >>\s""", "Способа под таким номер не сущесвует.", 3);
    }

    static double checkFinite(double[] x) {
        if (x.length == 1)
            return 0;

        double h = x[1] - x[0];
        for (int i = 1; i < x.length - 1; i++) {
            if (x[i + 1] - x[i] != h)
                return -1;
        }

        return h;
    }

    static void handleTerminalInput() {
        InputReader inputReader = new InputReader();
        Points points = inputReader.readPointsFromTerminal();
        calculateMethods(points, null);
    }

    static void handleFileInput() {
        InputReader inputReader = new InputReader();
        Points points = inputReader.readPointsFromFile(inputReader.readFilename());
        calculateMethods(points, null);
    }

    static void handleFunctionInput() {
        Function[] functions = {
                new Function1(),
                new Function2()
        };
        System.out.println("Выберите функцию: ");
        for (int i = 0; i < functions.length; i++)
            System.out.println("\t" + (i + 1) + ". " + functions[i]);
        InputReader inputReader = new InputReader();
        Function function = functions[inputReader.readIndex("Введите номер: ", "Функции под таким номером не существует.", functions.length) - 1];

        double left, right;

        while (true) {
            left = inputReader.readDouble("Введите левую границу интервала: ");
            if (!(left > function.getLeftLimit() && left < function.getRightLimit())) {
                System.out.println("Указанная граница не входит в область определения функции.");
                continue;
            }
            right = inputReader.readDouble("Введите правую границу интервала: ");
            if (!(right > function.getLeftLimit() && right < function.getRightLimit())) {
                System.out.println("Указанная граница не входит в область определения функции.");
                continue;
            }
            if (right <= left) {
                System.out.println("Правая граница должна быть правее левой границы.");
                continue;
            }
            break;
        }

        int n = inputReader.readPositiveInt("Введите кол-во точек: ");

        double[] x = new double[n];
        double[] y = new double[n];

        double h = (right - left) / n;
        for (int i = 0; i < n; i++) {
            x[i] = left + h * i;
            y[i] = function.calculate(left + h * i);
        }

        calculateMethods(new Points(x, y), function);
    }

    static void calculateMethods(Points points, Function function) {
        Method lagrange = new Lagrange(points.getX(), points.getY());
        Method newtonOfDividedDifferences = new NewtonOfDividedDifferences(lagrange.getX(), lagrange.getY());
        Method newtonOfFiniteDifferences = null;
        double h = checkFinite(points.getX());

        if (h != -1) {
            newtonOfFiniteDifferences = new NewtonOfFiniteDifferences(points.getX(), points.getY(), h);
            System.out.println(((NewtonOfFiniteDifferences) newtonOfFiniteDifferences).getFiniteDifferencesTable());
        }

        GraphPrinter graphPrinter;
        if (function == null)
            graphPrinter = new GraphPrinter(points.getX(), points.getY(), lagrange, newtonOfDividedDifferences, newtonOfFiniteDifferences);
        else
            graphPrinter = new GraphPrinter(points.getX(), points.getY(), function, lagrange, newtonOfDividedDifferences, newtonOfFiniteDifferences);

        graphPrinter.setVisible(true);
    }

}
