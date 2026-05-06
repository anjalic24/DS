import java.io.*;
import java.net.*;

public class TokenRingMutex {

    private int myPort;
    private String nextHost;
    private int nextPort;
    private volatile boolean hasToken;

    TokenRingMutex(int myPort, String nextHost, int nextPort, boolean hasToken) {
        this.myPort = myPort;
        this.nextHost = nextHost;
        this.nextPort = nextPort;
        this.hasToken = hasToken;
    }

    public void start() throws Exception {

        // Listener thread
        new Thread(() -> {
            try (ServerSocket ss = new ServerSocket(myPort)) {
                System.out.println("Listening on port " + myPort);
                while (true) {
                    Socket s = ss.accept();
                    String msg = new DataInputStream(s.getInputStream()).readUTF();
                    if (msg.equals("TOKEN")) {
                        System.out.println("Token received on port " + myPort);
                        hasToken = true;
                    }
                    s.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // Main loop
        while (true) {
            if (hasToken) {
                // Critical section
                System.out.println(">> ENTERED CS on port " + myPort);
                Thread.sleep(2000);
                System.out.println("<< EXITED CS on port " + myPort);

                // Send token
                hasToken = false;
                try (Socket s = new Socket(nextHost, nextPort)) {
                    new DataOutputStream(s.getOutputStream()).writeUTF("TOKEN");
                    System.out.println("Token sent to " + nextHost + ":" + nextPort + "\n");
                } catch (Exception e) {
                    System.out.println("Could not send token to " + nextPort);
                }
            }
            Thread.sleep(500);
        }
    }

    public static void main(String[] args) throws Exception {
        new TokenRingMutex(
            Integer.parseInt(args[0]),
            args[1],
            Integer.parseInt(args[2]),
            Boolean.parseBoolean(args[3])
        ).start();
    }
}

// terminal 1
// java TokenRingMutex 6002 localhost 6000 false

// terminal 2
// java TokenRingMutex 6001 localhost 6002 false

// terminal 3
// java TokenRingMutex 6000 localhost 6001 true

// flow: 6000 → 6001 → 6002 → 6000


// if you wish to add more terminals 
// java TokenRingMutex 6003 localhost 6000 false  # Terminal 4 first
// java TokenRingMutex 6002 localhost 6003 false  # Terminal 3
// java TokenRingMutex 6001 localhost 6002 false  # Terminal 2
// java TokenRingMutex 6000 localhost 6001 true   # Terminal 1 last

// 6000 → 6001 → 6002 → 6003 → 6000

// import java.io.DataInputStream;
// import java.io.DataOutputStream;
// import java.net.*;

// public class TokenRingMutex {

//     private int myPort;
//     private String nextHost;
//     private int nextPort;
//     private volatile boolean hasToken; // FIXED: volatile for thread visibility

//     TokenRingMutex(int myPort, String nextHost, int nextPort, boolean hasToken) {
//         this.myPort = myPort;
//         this.nextHost = nextHost;
//         this.nextPort = nextPort;
//         this.hasToken = hasToken;
//     }

//     public void startCirculation() throws Exception {

//         // Listener thread — waits for token from previous process
//         Thread listenerThread = new Thread(() -> {
//             try (ServerSocket serverSocket = new ServerSocket(myPort)) {
//                 System.out.println("Listening on port " + myPort + "\n");
//                 while (true) {
//                     Socket socket = serverSocket.accept();
//                     DataInputStream in = new DataInputStream(socket.getInputStream());
//                     String token = in.readUTF();
//                     if (token.equals("TOKEN")) {
//                         System.out.println("Token received from " + socket.getRemoteSocketAddress() + "\n");
//                         hasToken = true;
//                     }
//                     socket.close();
//                 }
//             } catch (Exception e) {
//                 e.printStackTrace();
//             }
//         });

//         listenerThread.setDaemon(true); // FIXED: daemon so it doesn't block JVM exit
//         listenerThread.start();

//         // Main loop — enters critical section when token is held
//         while (true) {
//             if (hasToken) {
//                 enterCriticalSection();
//                 hasToken = false; // FIXED: release before sending to avoid race
//                 sendToken();
//             }
//             Thread.sleep(500); // FIXED: reduced sleep for faster response
//         }
//     }

//     private void enterCriticalSection() throws Exception {
//         System.out.println(">>>>>> ENTERED CRITICAL SECTION on port " + myPort + "\n");
//         System.out.println("------ Simulating work...\n");
//         Thread.sleep(2000); // simulate work
//         System.out.println("<<<<<< EXITED CRITICAL SECTION on port " + myPort + "\n");
//     }

//     private void sendToken() throws Exception {
//         // Retry logic in case next process isn't ready yet
//         int retries = 5;
//         while (retries-- > 0) {
//             try (Socket socket = new Socket(nextHost, nextPort);
//                  DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
//                 out.writeUTF("TOKEN");
//                 System.out.println("Token sent to " + nextHost + ":" + nextPort + "\n");
//                 return;
//             } catch (Exception e) {
//                 System.out.println("Retrying to send token... (" + retries + " left)");
//                 Thread.sleep(1000);
//             }
//         }
//         System.out.println("Failed to send token after retries.");
//     }

//     public static void main(String[] args) throws Exception {
//         if (args.length != 4) {
//             System.out.println("Usage: java TokenRingMutex <myPort> <nextHost> <nextPort> <hasToken>");
//             return;
//         }
//         int myPort = Integer.parseInt(args[0]);
//         String nextHost = args[1];
//         int nextPort = Integer.parseInt(args[2]);
//         boolean hasToken = Boolean.parseBoolean(args[3]);

//         TokenRingMutex node = new TokenRingMutex(myPort, nextHost, nextPort, hasToken);
//         node.startCirculation();
//     }
// }