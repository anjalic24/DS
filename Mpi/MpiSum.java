// export MPJ_HOME=$PWD
// export PATH=$MPJ_HOME/bin:$PATH
// javac -cp $MPJ_HOME/lib/mpj.jar PS2_1.java
// mpjrun.sh -np 4 PS2_1

import mpi.MPI;
import java.util.Arrays;

public class PS2_1 {
    public static void main(String[] args) throws Exception {
        MPI.Init(args);

        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        final int unitSize = 5;
        final int root = 0;
        final int totalElements = unitSize * size;

        int[] sendBuffer = new int[totalElements];
        int[] receiveBuffer = new int[unitSize];

        if (rank == root) {
            for (int i = 0; i < totalElements; i++) {
                sendBuffer[i] = i + 1;
            }
            System.out.println("Root Array: " + Arrays.toString(sendBuffer));
        }

        MPI.COMM_WORLD.Scatter(sendBuffer, 0, unitSize, MPI.INT,
                receiveBuffer, 0, unitSize, MPI.INT, root);

        int localSum = 0;
        for (int i = 0; i < unitSize; i++) {
            localSum += receiveBuffer[i];
        }

        for (int i = 0; i < size; i++) {
            MPI.COMM_WORLD.Barrier();
            if (rank == i) {
                System.out.println("Process " + rank
                        + " chunk = " + Arrays.toString(receiveBuffer)
                        + " local sum = " + localSum
                );
            }
        }

        int[] reduceSend = { localSum };
        int[] gatheredSums = new int[size];   

        MPI.COMM_WORLD.Gather(reduceSend, 0, 1, MPI.INT,
                gatheredSums, 0, 1, MPI.INT,
                root);

        if (rank == root) {
            int finalSum = 0;
            for (int i = 0; i < size; i++) {
                finalSum += gatheredSums[i];
            }
            System.out.println("Gathered Sums: " + Arrays.toString(gatheredSums));
            System.out.println("Final Sum = " + finalSum);
        }

        MPI.Finalize();
    }
}