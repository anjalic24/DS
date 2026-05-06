import ReverseModule.ReversePOA;

public class ReverseImpl extends ReversePOA {
    public String reverse_string(String name) {
        return "Server Sent: " + new StringBuffer(name).reverse();
    }
}   
