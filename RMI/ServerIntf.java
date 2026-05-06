import java.rmi.*;

public interface ServerIntf extends Remote{
    public double Addition(double num1, double num2) throws RemoteException;
}