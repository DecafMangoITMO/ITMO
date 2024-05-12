package util;


import diff_functions.DiffFunction;
import methods.Result;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class GraphPrinter extends JFrame {

    private static final int WIDTH = (int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.5);
    private static final int HEIGHT = (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.5);

    public GraphPrinter(double x0, double y0, DiffFunction diffFunction, Result result) {
        setTitle("График функции");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setSize(WIDTH, HEIGHT);
        setLocation((int) (WIDTH * 0.5), (int) (HEIGHT * 0.5));
        getContentPane().setBackground(Color.BLACK);
        add(new Graph(WIDTH, HEIGHT, x0, y0, diffFunction, result));
    }


    private class Graph extends Canvas {

        final int width;
        final int height;
        final double x0;
        final double y0;
        final DiffFunction diffFunction;
        final Result result;

        Graph(int width, int height, double x0, double y0, DiffFunction diffFunction, Result result) {
            this.width = width;
            this.height = height;
            this.x0 = x0;
            this.y0 = y0;
            this.diffFunction = diffFunction;
            this.result = result;
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

            printDiffFunction(g, widthCenter, heightCenter);
            g.setColor(Color.GREEN);
            paintResult(g, widthCenter, heightCenter);

        }

        private void printDiffFunction(Graphics g, int widthCenter, int heightCenter) {
            List<Integer> xPoints = new ArrayList<>();
            List<Integer> yPoints = new ArrayList<>();

            double value = -10;
            double limit = 10;
            while (value < limit) {
                xPoints.add((int) (value * 40 + widthCenter));
                yPoints.add((int) (-diffFunction.y(value, x0, y0) * 40 + heightCenter));
                value += 0.01d;
            }

            for (int i = 0; i < xPoints.size() - 1; i++)
                g.drawLine(xPoints.get(i), yPoints.get(i), xPoints.get(i + 1), yPoints.get(i + 1));
        }

        private void paintResult(Graphics g, int widthCenter, int heightCenter) {
            List<Double> x = result.getX();
            List<Double> y = result.getY();
            for (int i = 0; i < x.size(); i++) {
                g.drawOval((int) (x.get(i) * 40 + widthCenter) - 3, (int) (-y.get(i) * 40 + heightCenter) - 3, 6, 6);
            }
        }
    }

}