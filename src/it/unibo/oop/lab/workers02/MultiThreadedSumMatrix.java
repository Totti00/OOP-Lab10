package it.unibo.oop.lab.workers02;

import java.util.ArrayList;
import java.util.List;

public class MultiThreadedSumMatrix implements SumMatrix {

    private final int nthread;

    public MultiThreadedSumMatrix(final int n) {
        this.nthread = n;
    }

    private static final class Worker extends Thread {
        private final double[][] matrix;
        private final int startpos;
        private final int nelem;
        private long res;

        private Worker(final double[][] matrix, final int startpos, final int nelem) {
            super();
            this.matrix = matrix;
            this.startpos = startpos;
            this.nelem = nelem;
        }

        public void run() {
            System.out.println("Working from position " + startpos + " to position " + (startpos + nelem - 1));
            for (int i = startpos; i < matrix.length && i < startpos + nelem; i++) {
                for (final double d : matrix[i]) {
                    this.res += d;
                }
            }
        }

        public long getResult() {
            return this.res;
        }
    }

    public double sum(final double[][] matrix) {
        final int size = matrix.length % this.nthread + matrix.length / this.nthread;

        final List<Worker> workers = new ArrayList<>(this.nthread);
        for (int start = 0; start < matrix.length; start += size) {
            workers.add(new Worker(matrix, start, size));
        }

        for (final Worker w: workers) {
            w.start();
        }

        long sum = 0;
        for (final Worker w: workers) {
            try {
                w.join();
                sum += w.getResult();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
        return sum;
    }
}

