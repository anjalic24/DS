import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;

public class CalculatorServer {

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        server.createContext("/calc", new CalcHandler());

        server.setExecutor(null);
        server.start();

        System.out.println("Open:");
        System.out.println("http://localhost:8000/calc");
    }

    static class CalcHandler implements HttpHandler {

        public void handle(HttpExchange exchange) throws IOException {

            String query = exchange.getRequestURI().getQuery();

            String response;

            // If form not submitted → show UI
            if (query == null) {

                response =
                    "<html>" +
                    "<body>" +
                    "<h2>Calculator</h2>" +
                    "<form action='/calc' method='get'>" +

                    "A: <input type='text' name='a'><br><br>" +
                    "B: <input type='text' name='b'><br><br>" +

                    "Operation: " +
                    "<select name='op'>" +
                    "<option value='add'>Add</option>" +
                    "<option value='sub'>Subtract</option>" +
                    "<option value='mul'>Multiply</option>" +
                    "<option value='div'>Divide</option>" +
                    "</select><br><br>" +

                    "<input type='submit' value='Calculate'>" +
                    "</form>" +
                    "</body>" +
                    "</html>";

            } else {

                String[] params = query.split("&");

                int a = 0, b = 0;
                String op = "";

                for (String p : params) {
                    String[] kv = p.split("=");

                    if (kv[0].equals("a")) a = Integer.parseInt(kv[1]);
                    if (kv[0].equals("b")) b = Integer.parseInt(kv[1]);
                    if (kv[0].equals("op")) op = kv[1];
                }

                int result = 0;

                switch (op) {
                    case "add": result = a + b; break;
                    case "sub": result = a - b; break;
                    case "mul": result = a * b; break;
                    case "div": result = a / b; break;
                }

                response =
                    "<html><body>" +
                    "<h2>Result = " + result + "</h2>" +
                    "<a href='/calc'>Back</a>" +
                    "</body></html>";
            }

            exchange.getResponseHeaders().add("Content-Type", "text/html");
            exchange.sendResponseHeaders(200, response.getBytes().length);

            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}