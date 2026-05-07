// javac *.java
// rmiregistry
// terminal 1 - java Server
// terminal 2 - java Client

import java.rmi.*;
import java.util.Scanner;

public class Client{
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        try{
            String serverURL = "rmi://localhost/Server";
            ServerIntf serInt = (ServerIntf) Naming.lookup(serverURL);

            // change the things to input and output according to the problem statement

            System.out.println("Enter num1: ");
            double num1 = sc.nextDouble();

            System.out.println("Enter num2: ");
            double num2 = sc.nextDouble();

            System.out.println("Answer from Server: " + serInt.Addition(num1, num2));
        }
        catch(Exception e){
            System.out.println("Exception at Server: " + e.getMessage());
        }
    }
}