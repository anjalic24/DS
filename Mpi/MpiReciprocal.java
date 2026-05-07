// export MPJ_HOME=$PWD
// export PATH=$MPJ_HOME/bin:$PATH
// javac -cp $MPJ_HOME/lib/mpj.jar PS2_1.java
// mpjrun.sh -np 4 PS2_1

import mpi.MPI;
import java.util.Arrays;

public class PS2_4 {
    public static void main(String[] args) {
        MPI.Init(args);

        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        final int unitSize = 5;
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

        // Scatter
        MPI.COMM_WORLD.Scatter(sendBuffer, 0, unitSize, MPI.INT,
                receiveBuffer, 0, unitSize, MPI.INT, root);

        // Compute reciprocal
        double[] localReciprocal = new double[unitSize];
        for (int i = 0; i < unitSize; i++) {
            localReciprocal[i] = 1.0 / receiveBuffer[i];
        }

        // Print each process
        for (int i = 0; i < size; i++) {
            MPI.COMM_WORLD.Barrier();
            if (rank == i) {
                System.out.println("Process " + rank
                        + " chunk = " + Arrays.toString(receiveBuffer)
                        + " reciprocal = " + Arrays.toString(localReciprocal)
                );
            }
        }

        // Gather all reciprocals
        double[] finalReciprocal = new double[totalElements];

        MPI.COMM_WORLD.Gather(localReciprocal, 0, unitSize, MPI.DOUBLE,
                finalReciprocal, 0, unitSize, MPI.DOUBLE,
                root);

        // Final output
        if (rank == root) {
            System.out.println("Final Reciprocal Array = " + Arrays.toString(finalReciprocal));
        }

        MPI.Finalize();
    }
}