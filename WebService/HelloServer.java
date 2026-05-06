import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;

public class HelloServer {

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        server.createContext("/hello", new HelloHandler());

        server.setExecutor(null);
        server.start();

        // 👇 PRINT LINK IN TERMINAL
        System.out.println("Server started at port 8000");
        System.out.println("Open this in browser:");
        System.out.println("http://localhost:8000/hello");
    }

    static class HelloHandler implements HttpHandler {

        public void handle(HttpExchange exchange) throws IOException {

            String query = exchange.getRequestURI().getQuery();
            String response;

            // If name is given → show greeting
            if (query != null && query.contains("name=")) {

                String name = query.split("=")[1];

                response =
                        "<html><body>" +
                        "<h1>Hello " + name + "</h1>" +
                        "<br><a href='/hello'>Go Back</a>" +
                        "</body></html>";

                exchange.getResponseHeaders().add("Content-Type", "text/html");

                exchange.sendResponseHeaders(200, response.getBytes().length);

            } else {

                // Show input form
                response =
                        "<html>" +
                        "<body>" +
                        "<h2>Enter Your Name</h2>" +
                        "<form action='/hello' method='get'>" +
                        "Name: <input type='text' name='name'>" +
                        "<input type='submit' value='Submit'>" +
                        "</form>" +
                        "</body>" +
                        "</html>";

                exchange.getResponseHeaders().add("Content-Type", "text/html");

                exchange.sendResponseHeaders(200, response.getBytes().length);
            }

            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}