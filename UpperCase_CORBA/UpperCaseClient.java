// idlj -fall UpperCase.idl
// javac *.java
// javac StringApp/*.java
// tnameserv -ORBInitialPort 1050
// terminal 1 - java UpperCaseServer -ORBInitialPort 1050 -ORBInitialHost localhost
// terminal 2 - java UpperCaseClient -ORBInitialPort 1050 -ORBInitialHost localhost

import StringApp.*;
import org.omg.CORBA.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;

public class UpperCaseClient {

    public static void main(String[] args) {

        try {
            ORB orb = ORB.init(args, null);

            org.omg.CORBA.Object objRef =
                orb.resolve_initial_references("NameService");

            NamingContext ncRef = NamingContextHelper.narrow(objRef);

            NameComponent nc = new NameComponent("UpperCase", "");
            NameComponent path[] = {nc};

            UpperCase obj = UpperCaseHelper.narrow(ncRef.resolve(path));

            String input = "hello corba";

            String result = obj.toUpperCase(input);

            System.out.println("Original: " + input);
            System.out.println("Uppercase: " + result);

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}