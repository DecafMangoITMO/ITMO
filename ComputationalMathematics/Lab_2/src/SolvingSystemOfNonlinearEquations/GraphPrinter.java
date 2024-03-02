package SolvingSystemOfNonlinearEquations;

import SolvingSystemOfNonlinearEquations.functions.Function;
import SolvingSystemOfNonlinearEquations.systems.SystemOfNonlinearEquations;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class GraphPrinter extends JFrame {

    private static final int WIDTH = (int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.5);
    private static final int HEIGHT = (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.5);

    public GraphPrinter(SystemOfNonlinearEquations system) {
        setTitle("График функции");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setSize(WIDTH, HEIGHT);
        setLocation((int) (WIDTH * 0.5), (int) (HEIGHT * 0.5));
        add(new Graph(WIDTH, HEIGHT, system));
    }

    private class Graph extends Canvas {

        final int width;
        final int height;
        final SystemOfNonlinearEquations system;

        Graph(int width, int height, SystemOfNonlinearEquations system) {
            this.width = width;
            this.height = height;
            this.system = system;
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

            for (Function function : system.getFunctions()) {
                List<List<Double>> points = function.computePoints();
                List<Integer> xPoints = new ArrayList<>();
                List<Integer> yPoints = new ArrayList<>();

                for (int i = 0; i < points.get(0).size(); i++) {
                    xPoints.add((int) (points.get(0).get(i) * 40 + widthCenter));
                    yPoints.add((int) -(points.get(1).get(i) * 40) + heightCenter);
                }

                for (int i = 0; i < xPoints.size() - 1; i++)
                    g.drawLine(xPoints.get(i), yPoints.get(i), xPoints.get(i + 1), yPoints.get(i + 1));
            }
        }
    }

}
