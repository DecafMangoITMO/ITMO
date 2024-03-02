public class Main {

    public static void main(String[] args) {
        System.out.println("Для выхода из программы напишите exit.");

        InputReader inputReader = new InputReader();
        Calculation calculation = new Calculation();

        Data data = null;
        switch (args.length) {
            case 0:
                data = inputReader.readFromTerminal();
                break;
            case 1:
                data = inputReader.readFromFile(args[0]);
                break;
            default:
                System.out.println("Программа принимает один аргумент - путь к файлу.");
                System.exit(1);
        }

        if (calculation.getDeterminant(data.getA()) == 0d) {
            System.out.println("""
                    Полученная матрица является вырожденной (определитель равен нулю).
                    Данная программа работает только со совместными и определенными СЛАУ.""");
            System.exit(1);
        }

        calculation.toConvergence(data);


        calculation.iterate(data);

    }
}

