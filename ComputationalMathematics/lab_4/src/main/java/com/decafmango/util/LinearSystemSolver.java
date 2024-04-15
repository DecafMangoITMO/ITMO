package com.decafmango.util;

import org.apache.commons.math3.linear.*;;

public class LinearSystemSolver {

    public static double[] solve(double[][] a, double[] b) {
        try {
            RealMatrix coefficients = new Array2DRowRealMatrix(a, false);
            DecompositionSolver solver = new LUDecomposition(coefficients).getSolver();
            RealVector constants = new ArrayRealVector(b, false);
            RealVector solution = solver.solve(constants);
            return solution.toArray();
        } catch (SingularMatrixException e) {
            return new double[] {};
        }
    }

}