package util;

import java.util.InputMismatchException;
import java.util.Scanner;

public class InputReader {

    public int readIndex(String message, String notFoundMessage, int length) {
        while (true) {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.print(message);
                String buffer = scanner.next();
                if (buffer.equals("exit"))
                    System.exit(0);
                int value = Integer.parseInt(buffer);
                if (value <= 0) {
                    System.out.println("Требуется ввести положительное число.");
                    continue;
                }
                if (value - 1 >= length) {
                    System.out.println(notFoundMessage);
                    continue;
                }
                return value - 1;
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println("Требуется ввести целое число.");
            }
        }
    }

    public double readDouble(String message) {
        while (true) {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.print(message);
                String buffer = scanner.next();
                if (buffer.equals("exit"))
                    System.exit(0);
                buffer = buffer.replaceAll(",", ".");
                return Double.parseDouble(buffer);
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println("Требуется ввести число.");
            }
        }
    }

    public double readPositiveDouble(String message) {
        while (true) {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.print(message);
                String buffer = scanner.next();
                if (buffer.equals("exit"))
                    System.exit(0);
                buffer = buffer.replaceAll(",", ".");
                double value = Double.parseDouble(buffer);
                if (value >= 0)
                    return value;
                System.out.println("Требуется ввести положительное число.");
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println("Требуется ввести число.");
            }
        }
    }

    public boolean readYesNo(String message) {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println(message);
            System.out.print("Введите да/нет: ");
            String buffer = scanner.nextLine();
            if (buffer.equals("да"))
                return true;
            else if (buffer.equals("нет"))
                return false;
            else
                System.out.println("Требуется ввести да/нет.");
        }
    }

}
