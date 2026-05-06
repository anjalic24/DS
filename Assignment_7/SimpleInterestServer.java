import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;

public class SimpleInterestServer {

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        server.createContext("/si", new SIHandler());

        server.setExecutor(null);
        server.start();

        System.out.println("Server started at http://localhost:8000");
        System.out.println("Open: http://localhost:8000/si");
    }

    static class SIHandler implements HttpHandler {

        public void handle(HttpExchange exchange) throws IOException {

            String query = exchange.getRequestURI().getQuery();
            String response;

            // If no input → show UI form
            if (query == null) {

                response =
                    "<html>" +
                    "<body>" +
                    "<h2>Simple Interest Calculator</h2>" +
                    "<form action='/si' method='get'>" +

                    "Principal (P): <input type='text' name='p'><br><br>" +
                    "Rate (R): <input type='text' name='r'><br><br>" +
                    "Time (T): <input type='text' name='t'><br><br>" +

                    "<input type='submit' value='Calculate'>" +
                    "</form>" +
                    "</body>" +
                    "</html>";

            } else {

                String[] params = query.split("&");

                double p = 0, r = 0, t = 0;

                for (String param : params) {
                    String[] kv = param.split("=");

                    if (kv[0].equals("p")) p = Double.parseDouble(kv[1]);
                    if (kv[0].equals("r")) r = Double.parseDouble(kv[1]);
                    if (kv[0].equals("t")) t = Double.parseDouble(kv[1]);
                }

                double si = (p * r * t) / 100;

                response =
                    "<html><body>" +
                    "<h2>Simple Interest = " + si + "</h2>" +
                    "<a href='/si'>Back</a>" +
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