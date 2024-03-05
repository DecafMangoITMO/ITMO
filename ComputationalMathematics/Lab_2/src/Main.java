public class Main {

    public static void main(String[] args) {

        System.out.println("""
                Для выхода из программы пропишите exit.
                Выберите режим:
                1) Решение нелинейного уравнения
                2) Решение системы нелинейных уравнений""");

        InputReader inputReader = new InputReader();
        int index = inputReader.readIndex("Введите номер режима: ", "Режима под таким номером не существует.", 2);
        System.out.println(" ");

        switch (index) {
            case 0:
                SolvingNonlinearEquation.Main.main(args);
                break;
            case 1:
                SolvingSystemOfNonlinearEquations.Main.main(args);
        }
    }
}
