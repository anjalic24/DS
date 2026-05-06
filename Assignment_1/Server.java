import java.rmi.*;

public class Server{
    public static void main(String[] args){
        try{
            ServerImpl simp = new ServerImpl(); //Stub
            Naming.rebind("Server", simp);
        }
        catch(Exception e){
            System.out.println("Exception at Server: " + e.getMessage());
        }
    }
}