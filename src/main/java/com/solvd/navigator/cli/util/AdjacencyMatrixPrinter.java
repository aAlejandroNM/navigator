package com.solvd.navigator.cli.util;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AdjacencyMatrixPrinter {

    private static final Logger LOGGER = LogManager.getLogger(AdjacencyMatrixPrinter.class);

    public static void print(double[][] matrix) {
        int n = matrix.length;

        LOGGER.info("=== ADJACENCY / DISTANCE MATRIX ===");

        StringBuilder header = new StringBuilder("     ");
        for (int i = 0; i < n; i++) {
            header.append(String.format("%5d", i));
        }
        LOGGER.info(header.toString());

        for (int i = 0; i < n; i++) {
            StringBuilder row = new StringBuilder(String.format("%5d", i));
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == Double.POSITIVE_INFINITY)
                    row.append(String.format("%5s", "∞"));
                else
                    row.append(String.format("%5.0f", matrix[i][j]));
            }
            LOGGER.info(row.toString());
        }
    }
}

