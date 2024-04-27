package util;

import functions.Function;
import methods.Method;
import org.w3c.dom.ls.LSOutput;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class GraphPrinter extends JFrame {

    private static final int WIDTH = (int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.5);
    private static final int HEIGHT = (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.5);

    public GraphPrinter(double[] x, double[] y, Method lagrange, Method newtonOfDividedDifferences, Method newtonOfFiniteDifferences) {
        setTitle("График функции");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setSize(WIDTH, HEIGHT);
        setLocation((int) (WIDTH * 0.5), (int) (HEIGHT * 0.5));
        getContentPane().setBackground(Color.BLACK);
        add(new Graph(WIDTH, HEIGHT, x, y, null, lagrange, newtonOfDividedDifferences, newtonOfFiniteDifferences));
    }

    public GraphPrinter(double[] x, double[] y, Function function, Method lagrange, Method newtonOfDividedDifferences, Method newtonOfFiniteDifferences) {
        setTitle("График функции");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setSize(WIDTH, HEIGHT);
        setLocation((int) (WIDTH * 0.5), (int) (HEIGHT * 0.5));
        getContentPane().setBackground(Color.BLACK);
        add(new Graph(WIDTH, HEIGHT, x, y, function, lagrange, newtonOfDividedDifferences, null));
    }


    private class Graph extends Canvas {

        final int width;
        final int height;
        final double[] x;
        final double[] y;
        final Function function;
        final Method lagrange;
        final Method newtonOfDividedDifferences;
        final Method newtonOfFiniteDifferences;

        Graph(int width, int height, double[] x, double[] y, Function function, Method lagrange, Method newtonOfDividedDifferences, Method newtonOfFiniteDifferences) {
            this.width = width;
            this.height = height;
            this.x = x;
            this.y = y;
            this.function = function;
            this.lagrange = lagrange;
            this.newtonOfDividedDifferences = newtonOfDividedDifferences;
            this.newtonOfFiniteDifferences = newtonOfFiniteDifferences;
        }

        @Override
        public void paint(Graphics g) {
            int widthCenter = (int) (width * 0.5);
            int heightCenter = (int) (height * 0.5);

            g.setColor(Color.WHITE);
            g.drawString("0", widthCenter - 15, heightCenter + 15);
            g.drawLine(widthCenter, 0, widthCenter, height);
            g.drawLine(0, heightCenter, width, heightCenter);

            for (int i = 1; i < width / 40; i++) {
                g.drawLine(widthCenter - i * 40, heightCenter + 5, widthCenter - i * 40, heightCenter - 5);
                g.drawString(String.valueOf(-i), widthCenter - i * 40 - 10, heightCenter + 20);
            }
            for (int i = 1; i < width / 40; i++) {
                g.drawLine(widthCenter + i * 40, heightCenter + 5, widthCenter + i * 40, heightCenter - 5);
                g.drawString(String.valueOf(i), widthCenter + i * 40 - 5, heightCenter + 20);
            }
            for (int i = 1; i < height / 40; i++) {
                g.drawLine(widthCenter - 5, heightCenter + i * 40, widthCenter + 5, heightCenter + i * 40);
                g.drawString(String.valueOf(-i), widthCenter - 25, heightCenter + i * 40 + 5);
            }
            for (int i = 1; i < height / 40; i++) {
                g.drawLine(widthCenter - 5, heightCenter - i * 40, widthCenter + 5, heightCenter - i * 40);
                g.drawString(String.valueOf(i), widthCenter - 25, heightCenter - i * 40 + 5);
            }

            if (function != null)
                printFunction(g, function, widthCenter, heightCenter);
            g.setColor(Color.ORANGE);
            paintMethod(g, lagrange, widthCenter, heightCenter);
            g.setColor(Color.GREEN);
            paintMethod(g, newtonOfDividedDifferences, widthCenter, heightCenter);
            if (newtonOfFiniteDifferences != null) {
                g.setColor(Color.BLUE);
                paintMethod(g, newtonOfFiniteDifferences, widthCenter, heightCenter);
            }
            g.setColor(Color.CYAN);
            paintNodes(g, x, y, widthCenter, heightCenter);

            if (function != null)
                System.out.println("Функция -- белая линия");
            System.out.println("Многочлен Лагранжа -- оранжевая линия");
            System.out.println("Многочлен Ньютона с разделенными разностями -- зеленая линия");
            if (newtonOfFiniteDifferences != null)
                System.out.println("Многочлен Ньютона с конечными разностями -- синяя линия");
            System.out.println("Узлы интерполяции -- круги цвети Тиффани");
        }

        private void printFunction(Graphics g, Function function, int widthCenter, int heightCenter) {
            List<Integer> xPoints = new ArrayList<>();
            List<Integer> yPoints = new ArrayList<>();

            double value = -10;
            double limit = 10;
            while (value < limit)  {
                xPoints.add((int) (value * 40 + widthCenter));
                yPoints.add((int) (-function.calculate(value) * 40 + heightCenter));
                value += 0.01d;
            }

            for (int i = 0; i < xPoints.size() - 1; i++)
                g.drawLine(xPoints.get(i), yPoints.get(i), xPoints.get(i + 1), yPoints.get(i + 1));
        }

        private void paintMethod(Graphics g, Method method, int widthCenter, int heightCenter) {
            List<Integer> xPoints = new ArrayList<>();
            List<Integer> yPoints = new ArrayList<>();

            double value = -10;
            double limit = 10;
            while (value < limit)  {
                xPoints.add((int) (value * 40 + widthCenter));
                yPoints.add((int) (-method.calculate(value) * 40 + heightCenter));
                value += 0.01d;
            }

            for (int i = 0; i < xPoints.size() - 1; i++)
                g.drawLine(xPoints.get(i), yPoints.get(i), xPoints.get(i + 1), yPoints.get(i + 1));
        }

        private void paintNodes(Graphics g, double[] x, double[] y, int widthCenter, int heightCenter) {
            for (int i = 0; i < x.length; i++) {
                g.drawOval((int) (x[i] * 40 + widthCenter) - 3, (int) (-y[i] * 40 + heightCenter) - 3, 6, 6);
            }
        }
    }

}