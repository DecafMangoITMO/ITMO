package com.decafmango.util;

import javax.swing.*;

import com.decafmango.approximation.Approximation;
import com.decafmango.approximation.ExponentialApproximation;
import com.decafmango.approximation.LinearApproximation;
import com.decafmango.approximation.PowerApproximation;
import com.decafmango.approximation.QuadraticApproximation;

import java.awt.Toolkit;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class ApproximationPrinter extends JFrame {
    

    private static final int WIDTH = (int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.8);
    private static final int HEIGHT = (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.8);

    public ApproximationPrinter(List<double[]> points, Approximation linearApproximation, Approximation quadraticApproximation, Approximation powerApproximation, Approximation exponentialApproximation, Approximation logarithmicApproximation) {
        setTitle("График функции");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setSize(WIDTH, HEIGHT);
        setLocation((int) (WIDTH * 0.5), (int) (HEIGHT * 0.5));
        getContentPane().setBackground(Color.BLACK);
        add(new Graph(WIDTH, HEIGHT, points, linearApproximation, quadraticApproximation, powerApproximation, exponentialApproximation, logarithmicApproximation));
    }

    private class Graph extends Canvas {

        final int width;
        final int height;
        final List<double[]> points;
        final Approximation linearApproximation;
        final Approximation quadraticApproximation;
        final Approximation powerApproximation;
        final Approximation exponentialApproximation;
        final Approximation logarithmicApproximation;


        Graph(int width, int height, List<double[]> points, Approximation linearApproximation, Approximation quadraticApproximation, Approximation powerApproximation, Approximation exponentialApproximation, Approximation logarithmicApproximation) {
            this.width = width;
            this.height = height;
            this.points = points;
            this.linearApproximation = linearApproximation;
            this.quadraticApproximation = quadraticApproximation;
            this.powerApproximation = powerApproximation;
            this.exponentialApproximation = exponentialApproximation;
            this.logarithmicApproximation = logarithmicApproximation;
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

            g.setColor(Color.CYAN);
            for (double[] point : points)
                g.drawOval(widthCenter + (int) Math.round(point[0] * 40) - 2, heightCenter - (int) Math.round(point[1] * 40) - 2, 4, 4);

            if (linearApproximation.isCorrect())
                paintApproximation(g, widthCenter, heightCenter, Color.RED, linearApproximation);
            if (quadraticApproximation.isCorrect())
                paintApproximation(g, widthCenter, heightCenter, Color.YELLOW, quadraticApproximation);
            if (powerApproximation.isCorrect())
                paintApproximation(g, widthCenter, heightCenter, Color.GREEN, powerApproximation);
            if (exponentialApproximation.isCorrect())
                paintApproximation(g, widthCenter, heightCenter, Color.BLUE, exponentialApproximation);
            if (logarithmicApproximation.isCorrect())
                paintApproximation(g, widthCenter, heightCenter, Color.PINK, logarithmicApproximation);
        }

        private void paintApproximation(Graphics g, int widthCenter, int heightCenter, Color color, Approximation approximation) {
            List<Integer> xPoints = new ArrayList<>();
            List<Integer> yPoints = new ArrayList<>();

            double x, limit;

            if (approximation instanceof LinearApproximation || approximation instanceof QuadraticApproximation || approximation instanceof ExponentialApproximation) {
                x = -100;
                limit = 100;
            } else {
                x = 0.1d;
                limit = 100;
            }

            while(x < limit) {
                xPoints.add((int) (x * 40 + widthCenter));
                yPoints.add((int) -(approximation.calculateValue(x) * 40) + heightCenter);
                x += 0.1d;
            }

            g.setColor(color);
            for (int i = 0; i < xPoints.size() - 1; i++)
                g.drawLine(xPoints.get(i), yPoints.get(i), xPoints.get(i + 1), yPoints.get(i + 1));
        }
    }
}
