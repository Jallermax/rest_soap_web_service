package com.aplana.apiPractice;

import com.aplana.apiPractice.accounts.AccountService;
import com.aplana.apiPractice.accounts.UserProfile;
import com.aplana.apiPractice.servlets.ProfileServlet;
import com.aplana.apiPractice.servlets.debugServlets.RequestsServlet;
import com.aplana.apiPractice.servlets.debugServlets.SessionsServlet;
import com.aplana.apiPractice.servlets.debugServlets.SignUpServlet;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import javax.xml.ws.Endpoint;
import java.io.*;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class Main {

    private static final Logger LOG = Log.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        LOG.setDebugEnabled(true);
        Map<String, String> cfg = readConfig();

        startWSDLServer(cfg.get("servers.wsdl.ip"), cfg.get("servers.wsdl.port"));
        startRestfulServer(Integer.parseInt(cfg.get("servers.rest.port")));
    }

    private static void startRestfulServer(int port) throws Exception {

        AccountService accountService = new AccountService();

        accountService.addNewUser(new UserProfile("admin"));
        accountService.addNewUser(new UserProfile("test"));

        Server server = new Server(port);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setResourceBase("templates");
//        resource_handler.setWelcomeFiles(new String[]{"index.html"});//.setResourceBase("templates");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});

        server.setHandler(handlers);

        // DEBUG servlets
        context.addServlet(new ServletHolder(new RequestsServlet()), "/gui");
        context.addServlet(new ServletHolder(new SessionsServlet(accountService)), "/auth");
        context.addServlet(new ServletHolder(new SignUpServlet(accountService)), "/registration");

        context.addServlet(new ServletHolder(new ProfileServlet()), "/rest/profiles");

        server.start();
        server.join();
    }

    private static void startWSDLServer(String ip, String port) {
        String endpointAdress = "http://" + ip + ":" + port + "/wss/profiles";
        Endpoint.publish(endpointAdress, new SoapWSImpl());
        LOG.info("WSDL endpoint started at: " + endpointAdress);
        String endpoint2 = "http://" + ip + ":" + port + "/wss/provider";
        Endpoint.publish(endpoint2, new SoapProvider());
    }

    private static Map<String, String> readConfig() throws IOException {
        Properties serverCfg = new Properties();

        InputStreamReader reader = new InputStreamReader(new FileInputStream("config.properties"), "UTF-8");
        serverCfg.load(reader);
        Map<String, String> mapCfg = serverCfg.entrySet().stream()
                .collect(Collectors.toMap(e -> String.valueOf(e.getKey()), e -> String.valueOf(e.getValue())));
        LOG.info("Config loaded:");
        mapCfg.forEach((key, value) -> LOG.info(key + " = " + value));
        return mapCfg;
    }
}
