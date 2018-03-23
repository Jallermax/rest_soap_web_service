package main;

import main.accounts.AccountService;
import main.accounts.UserProfile;
import main.servlets.RequestsServlet;
import main.servlets.SessionsServlet;
import main.servlets.SignUpServlet;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.xml.ws.Endpoint;
import java.io.*;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws Exception {
        Map<String, String> cfg = readConfig();

        startWSDLServer(Integer.parseInt(cfg.get("servers.wsdl.port")));
        startRestfulServer(Integer.parseInt(cfg.get("servers.rest.port")));
    }

    private static void startRestfulServer(int port) throws Exception {

        AccountService accountService = new AccountService();

        accountService.addNewUser(new UserProfile("admin"));
        accountService.addNewUser(new UserProfile("test"));

        Server server = new Server(port);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

//        ResourceHandler resource_handler = new ResourceHandler();
//        resource_handler.setResourceBase("templates");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{context});

        server.setHandler(handlers);

        context.addServlet(new ServletHolder(new RequestsServlet()), "");
        context.addServlet(new ServletHolder(new SessionsServlet(accountService)), "/signin");
        context.addServlet(new ServletHolder(new SignUpServlet(accountService)), "/signup");

        server.start();
        server.join();
    }

    private static void startWSDLServer(int port) {
        Endpoint.publish("http://localhost:" + port + "/wss/hello", new SoapWSImpl());
    }

    private static Map<String, String> readConfig() throws IOException {
        Properties serverCfg = new Properties();

        InputStreamReader reader = new InputStreamReader(new FileInputStream("config.properties"), "UTF-8");
        serverCfg.load(reader);
        Map<String, String> mapCfg = serverCfg.entrySet().stream()
                .collect(Collectors.toMap(e -> String.valueOf(e.getKey()), e -> String.valueOf(e.getValue())));
        System.out.println("Config loaded:");
        mapCfg.forEach((key, value) -> System.out.println(key + " = " + value));
        return mapCfg;
    }
}
