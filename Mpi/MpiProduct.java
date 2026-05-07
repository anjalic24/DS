// export MPJ_HOME=$PWD
// export PATH=$MPJ_HOME/bin:$PATH
// javac -cp $MPJ_HOME/lib/mpj.jar PS2_1.java
// mpjrun.sh -np 4 PS2_1

import mpi.MPI;
import java.util.Arrays;

public class PS2_2 {
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

        // 🔁 LOCAL PRODUCT (changed from sum)
        int localProduct = 1;
        for (int i = 0; i < unitSize; i++) {
            localProduct *= receiveBuffer[i];
        }

        // Printing each process data
        for (int i = 0; i < size; i++) {
            MPI.COMM_WORLD.Barrier();
            if (rank == i) {
                System.out.println("Process " + rank
                        + " chunk = " + Arrays.toString(receiveBuffer)
                        + " local product = " + localProduct
                );
            }
        }

        // 🔁 USING GATHER
        int[] send = { localProduct };
        int[] gatheredProducts = new int[size];

        MPI.COMM_WORLD.Gather(send, 0, 1, MPI.INT,
                gatheredProducts, 0, 1, MPI.INT,
                root);

        // Root computes final product
        if (rank == root) {
            int finalProduct = 1;
            for (int i = 0; i < size; i++) {
                finalProduct *= gatheredProducts[i];
            }

            System.out.println("Gathered Products: " + Arrays.toString(gatheredProducts));
            System.out.println("Final Product = " + finalProduct);
        }

        MPI.Finalize();
    }
}