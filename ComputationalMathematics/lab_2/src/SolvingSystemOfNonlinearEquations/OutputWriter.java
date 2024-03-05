package SolvingSystemOfNonlinearEquations;


import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

public class OutputWriter {

    public void output(String text) {
        InputReader inputReader = new InputReader();
        System.out.println("""
                Выберите способ вывода результата:
                1) Терминал
                2) Файл""");
        int outputTypeIndex = inputReader.readIndex("Введите номер способа: ", "Способа под таким номером не существует.", 2);
        switch (outputTypeIndex) {
            case 0:
                System.out.println(text);
                break;
            case 1:
                while (true) {
                    String filename = inputReader.readFilename("Введите название файла: ");
                    File file = new File(filename);
                    try (Writer writer = new FileWriter(file)) {
                        writer.write(text);
                        return;
                    } catch (Exception e) {
                        System.out.println("При выводе произошла ошибка. Попробуйте снова.");
                    }
                }
        }
    }


}
