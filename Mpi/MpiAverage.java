// export MPJ_HOME=$PWD
// export PATH=$MPJ_HOME/bin:$PATH
// javac -cp $MPJ_HOME/lib/mpj.jar PS2_1.java
// mpjrun.sh -np 4 PS2_1

import mpi.MPI;
import java.util.Arrays;

public class PS2_3 {
    public static void main(String[] args) throws Exception {
        MPI.Init(args);

        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        final int unitSize = 3;
        final int totalElements = unitSize * size;
        final int root = 0;
        
        int[] sendBuffer = new int[totalElements];
        int[] receiveBuffer = new int[unitSize];

        if (rank == root) {
            for (int i = 0; i < totalElements; i++) {
                sendBuffer[i] = i + 1;
            }
            System.out.println("Root Array = " + Arrays.toString(sendBuffer));
        }

        MPI.COMM_WORLD.Scatter(sendBuffer, 0, unitSize, MPI.INT,
                receiveBuffer, 0, unitSize, MPI.INT, root);

        int localSum = 0;
        for (int x : receiveBuffer) localSum += x;

        // Correct average
        double localAvg = (double) localSum / unitSize;

        // Print each process
        for (int i = 0; i < size; i++) {
            MPI.COMM_WORLD.Barrier();
            if (rank == i) {
                System.out.println("Process " + rank
                        + " chunk = " + Arrays.toString(receiveBuffer)
                        + " local average = " + localAvg
                );
            }
        }

        // USING GATHER
        double[] send = { localAvg };
        double[] gatheredAvgs = new double[size];

        MPI.COMM_WORLD.Gather(send, 0, 1, MPI.DOUBLE,
                gatheredAvgs, 0, 1, MPI.DOUBLE,
                root);

        // Root calculates final average
        if (rank == root) {
            double total = 0;
            for (int i = 0; i < size; i++) {
                total += gatheredAvgs[i];
            }

            double finalAvg = total / size;

            System.out.println("Gathered Averages = " + Arrays.toString(gatheredAvgs));
            System.out.println("Final Average = " + finalAvg);
        }

        MPI.Finalize();
    }
}