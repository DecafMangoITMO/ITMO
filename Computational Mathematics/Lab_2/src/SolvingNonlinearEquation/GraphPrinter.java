package SolvingNonlinearEquation;


import SolvingNonlinearEquation.functions.Function;

import javax.swing.*;
import java.awt.Toolkit;
import java.awt.Canvas;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;


public class GraphPrinter extends JFrame {

    private static final int WIDTH = (int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.5);
    private static final int HEIGHT = (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.5);

    public GraphPrinter(Function function) {
        setTitle("График функции");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setSize(WIDTH, HEIGHT);
        setLocation((int) (WIDTH * 0.5), (int) (HEIGHT * 0.5));
        add(new Graph(WIDTH, HEIGHT, function));
    }

    private class Graph extends Canvas {

        final int width;
        final int height;
        final Function function;

        Graph(int width, int height, Function function) {
            this.width = width;
            this.height = height;
            this.function = function;
        }

        @Override
        public void paint(Graphics g) {
            int widthCenter = (int) (width * 0.5);
            int heightCenter = (int) (height * 0.5);

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

            List<Integer> xPoints = new ArrayList<>();
            List<Integer> yPoints = new ArrayList<>();

            double x = -10;
            while(x < 10) {
                xPoints.add((int) (x * 40 + widthCenter));
                yPoints.add((int) -(function.compute(x) * 40) + heightCenter);
                x += 0.1d;
            }

            for (int i = 0; i < xPoints.size() - 1; i++)
                g.drawLine(xPoints.get(i), yPoints.get(i), xPoints.get(i + 1), yPoints.get(i + 1));
        }
    }

}
