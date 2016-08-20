import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Basic Web Server listening on port 8000
 * Handles concurrent request by using CachedThreadPool
 *
 * Server has 2 url mappings
 * - http://localhost:8000/
 *      - This url supports POST method for incrementing the counter
 * - http://localhost:8000/index
 *      - This url supports GET method for the bonus page with increment counter button
 */
public class CounterServer {

    public static void main(String[] args) throws IOException {

        InetSocketAddress addr = new InetSocketAddress(8000);
        HttpServer server = HttpServer.create(addr, 0);

        server.createContext("/", new CounterHandler());
        server.createContext("/index", new IndexHandler());
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
        System.out.println("Web Server started and listening at http://localhost:8000" );

    }
}

/**
 * CounterHandler class is to handle requests at "/" path
 * Using an AtomicInteger to ensure a threadsafe counter
 */
class CounterHandler implements HttpHandler {

    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        Headers responseHeaders = httpExchange.getResponseHeaders();
        responseHeaders.set("Content-Type", "text/plain");

        String requestMethod = httpExchange.getRequestMethod();

        if (requestMethod.equalsIgnoreCase("POST")) {

            httpExchange.sendResponseHeaders(200, 0);
            OutputStream responseBody = httpExchange.getResponseBody();
            responseBody.write(String.valueOf(counter.getAndAdd(1)).getBytes());

        }
        else {
            httpExchange.sendResponseHeaders(405, 0);
        }

        httpExchange.close();
    }
}

/**
 * IndexHandler class is to handle requests at "/index" path
 * It loads index.html content into HttpResponse
 */
class IndexHandler implements HttpHandler {

    private String indexPage;

    public IndexHandler() throws FileNotFoundException {

        indexPage = new Scanner(new File("index.html")).useDelimiter("\\Z").next();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        Headers responseHeaders = httpExchange.getResponseHeaders();
        responseHeaders.set("Content-Type", "text/html");

        String requestMethod = httpExchange.getRequestMethod();

        if (requestMethod.equalsIgnoreCase("GET")) {

            httpExchange.sendResponseHeaders(200, 0);
            OutputStream responseBody = httpExchange.getResponseBody();
            responseBody.write(indexPage.getBytes());
        }
        else {
            httpExchange.sendResponseHeaders(405, 0);
        }

        httpExchange.close();
    }
}