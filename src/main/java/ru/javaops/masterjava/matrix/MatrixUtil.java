package ru.javaops.masterjava.matrix;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

    // TODO implement parallel multiplication matrixA*matrixB
    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        List<Callable<Void>> tasks = IntStream.range(0, matrixSize)
                .mapToObj(i -> new Callable<Void>() {
                    private final int[] column = new int[matrixSize];

                    @Override
                    public Void call() {
                        for (int k = 0; k < matrixSize; k++) {
                            column[k] = matrixB[k][i];
                        }
                        for (int j = 0; j < matrixSize; j++) {
                            int[] row = matrixA[j];
                            int sum = 0;
                            for (int k = 0; k < matrixSize; k++) {
                                sum += column[k] * row[k];
                            }
                            matrixC[j][i] = sum;
                        }
                        return null;
                    }
                })
                .collect(Collectors.toList());
        executor.invokeAll(tasks);

        return matrixC;
    }

    // TODO optimize by https://habrahabr.ru/post/114797/
    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        int[] column = new int[matrixSize];
        for (int i = 0; i < matrixSize; i++) {
            for (int k = 0; k < matrixSize; k++) {
                column[k] = matrixB[k][i];
            }
            for (int j = 0; j < matrixSize; j++) {
                int[] row = matrixA[j];
                int sum = 0;
                for (int k = 0; k < matrixSize; k++) {
                    sum += column[k] * row[k];
                }
                matrixC[j][i] = sum;
            }
        }
        return matrixC;
    }

    public static int[][] create(int size) {
        int[][] matrix = new int[size][size];
        Random rn = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = rn.nextInt(10);
            }
        }
        return matrix;
    }

    public static boolean compare(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
