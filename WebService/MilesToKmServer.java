import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;

public class MilesToKmServer {

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        server.createContext("/convert", new KmHandler());

        server.setExecutor(null);
        server.start();

        System.out.println("Server started at http://localhost:8000");
        System.out.println("Open: http://localhost:8000/convert");
    }

    static class KmHandler implements HttpHandler {

        public void handle(HttpExchange exchange) throws IOException {

            String query = exchange.getRequestURI().getQuery();
            String response;

            // If no input → show UI form
            if (query == null) {

                response =
                    "<html>" +
                    "<body>" +
                    "<h2>Miles to Kilometer Converter</h2>" +
                    "<form action='/convert' method='get'>" +

                    "Miles: <input type='text' name='miles'><br><br>" +

                    "<input type='submit' value='Convert'>" +
                    "</form>" +
                    "</body>" +
                    "</html>";

            } else {

                double miles = 0;

                String[] params = query.split("&");

                for (String p : params) {
                    String[] kv = p.split("=");
                    if (kv[0].equals("miles")) {
                        miles = Double.parseDouble(kv[1]);
                    }
                }

                double km = miles * 1.60934;

                response =
                    "<html><body>" +
                    "<h2>Kilometers = " + km + "</h2>" +
                    "<a href='/convert'>Back</a>" +
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