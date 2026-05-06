import ReverseModule.*;
import org.omg.CosNaming.*;
import org.omg.PortableServer.*;
import org.omg.CORBA.*;

public class Server {
    public static void main(String[] args) throws Exception {
        //Initialize ORB
        ORB orb = ORB.init(args, null);

        //Activate POA
        POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
        rootPOA.the_POAManager().activate();

        //Create Servernt Object
        ReverseImpl rvr = new ReverseImpl();

        //Convert Object Reference to CORBA Format
        Reverse h_ref = ReverseHelper.narrow(rootPOA.servant_to_reference(rvr));

        //Get Naming Service
        NamingContextExt ncRef = NamingContextExtHelper.narrow(orb.resolve_initial_references("NameService"));
        ncRef.rebind(ncRef.to_name("Reverse"), h_ref);

        System.out.println("Server Ready");
        orb.run();
    }
}
