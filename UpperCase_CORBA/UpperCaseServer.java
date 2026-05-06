import StringApp.*;
import org.omg.CORBA.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;

public class UpperCaseServer extends UpperCasePOA {

    public String toUpperCase(String str) {
        return str.toUpperCase();
    }

    public static void main(String[] args) {

        try {
            ORB orb = ORB.init(args, null);

            UpperCaseServer obj = new UpperCaseServer();

            // IMPORTANT: DO NOT use orb.connect(obj)

            org.omg.CORBA.Object ref =
                orb.resolve_initial_references("RootPOA");

            org.omg.PortableServer.POA rootPOA =
                org.omg.PortableServer.POAHelper.narrow(ref);

            rootPOA.the_POAManager().activate();

            org.omg.CORBA.Object objRef = rootPOA.servant_to_reference(obj);

            UpperCase href = UpperCaseHelper.narrow(objRef);

            org.omg.CORBA.Object nameServiceObj =
                orb.resolve_initial_references("NameService");

            NamingContextExt ncRef =
                NamingContextExtHelper.narrow(nameServiceObj);

            String name = "UpperCase";
            ncRef.rebind(ncRef.to_name(name), href);

            System.out.println("Server Ready...");
            orb.run();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}