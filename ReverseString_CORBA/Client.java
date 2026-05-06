import ReverseModule.*;
import org.omg.CORBA.*;
import org.omg.CosNaming.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws Exception {
        ORB orb = ORB.init(args, null);
        NamingContextExt ncRef = NamingContextExtHelper.narrow(orb.resolve_initial_references("NameService"));
        Reverse reverseObj = ReverseHelper.narrow(ncRef.resolve_str("Reverse"));

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter String: ");
        String str = scanner.nextLine();
        System.out.println(reverseObj.reverse_string(str));
    }
}
