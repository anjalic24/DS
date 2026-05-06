import java.rmi.*;
import java.rmi.server.*;
// unicast remote obj is to make remote obj available to other clients

public class ServerImpl extends UnicastRemoteObject implements ServerIntf{
    public ServerImpl() throws RemoteException{

    }
    
    // this function changes according to the problem statement. Refer the Assignment_1.pdf
    // to know what exactly to put inside the function. Just change the retun statement.
    public double Addition(double num1, double num2) throws RemoteException{
        return num1 + num2;
    }

  // public double Addition(double num1, double num2) throws RemoteException{
  //   return num1 + num2 ;
  // }

  // public double Addition(double num1, double num2) throws RemoteException{
  //   return num1 - num2 ;
  // }

  //   public double Addition(double num1, double num2) throws RemoteException{
  //   return num1 * num2 ;
  // }
  
  //   public double Addition(double num1, double num2) throws RemoteException{
  //   return num1 / num2 ;
  // }

  // public double Addition(double num1, double num2) throws RemoteException{
  //   return (double)Math.pow(2, num2);
  // }
  
  // public double Addition(double num1, double num2) throws RemoteException{
  //   return (num1 * 9/5) + 32;
  // }

  // public double Addition(double num1, double num2) throws RemoteException{
  //   return (num1 * 1.60934);
  // }

// return "Hello"+name;

// (str1.compareTo(str2) > 0 ? str1 : str2);

//   String v = str.toLowerCase().chars().filter(c -> "aeiou".indexOf(c) != -1).mapToObj(c -> ""+(char)c).reduce("", (a,b)->a+b);
// return "Vowels: " + v + " | Count: " + v.length();

    // return (n <= 1) ? 1 : n * factorial(n - 1);
}