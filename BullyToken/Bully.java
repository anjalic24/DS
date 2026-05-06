import java.util.*;

public class Bully {

    static boolean[] alive;
    static int n;

    static void election(int p) {
        System.out.println("\nProcess " + p + " starts ELECTION");

        boolean higherExists = false;

        for (int i = p + 1; i < n; i++) {
            if (alive[i]) {
                System.out.println(p + " -> " + i + " : ELECTION");
                System.out.println(i + " -> " + p + " : OK");

                higherExists = true;

                // higher process takes over election
                election(i);
                return;
            }
        }

        // if no higher process responds
        if (!higherExists) {
            System.out.println("\nCoordinator (Leader) = " + p);

            for (int i = 0; i < n; i++) {
                if (i != p && alive[i]) {
                    System.out.println(p + " -> " + i + " : COORDINATOR");
                }
            }
        }
    }

    static void showStatus() {
        System.out.print("Status: ");
        for (int i = 0; i < n; i++) {
            System.out.print(i + (alive[i] ? "(UP) " : "(DOWN) "));
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter total number of processes: ");
        n = sc.nextInt();

        alive = new boolean[n];
        Arrays.fill(alive, true);

        int choice;

        System.out.println("\n========= MENU =========");
        System.out.println("1. UP a process");
        System.out.println("2. DOWN a process");
        System.out.println("3. ELECT leader (Bully Algorithm)");
        System.out.println("4. SHOW STATUS");
        System.out.println("5. EXIT");

        do {
            System.out.print("\nEnter choice: ");
            choice = sc.nextInt();

            switch (choice) {

                case 1:
                    System.out.print("Process to UP: ");
                    alive[sc.nextInt()] = true;
                    break;

                case 2:
                    System.out.print("Process to DOWN: ");
                    alive[sc.nextInt()] = false;
                    break;

                case 3:
                    System.out.print("Start election from: ");
                    int p = sc.nextInt();

                    if (!alive[p]) {
                        System.out.println("Process is DOWN");
                    } else {
                        election(p);
                    }
                    break;

                case 4:
                    showStatus();
                    break;

                case 5:
                    System.out.println("Exit");
                    break;

                default:
                    System.out.println("Invalid choice");
            }

        } while (choice != 5);

        sc.close();
    }
}