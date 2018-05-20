package com.aplana.apiPractice;

import com.aplana.apiPractice.accounts.AccountService;
import com.aplana.apiPractice.accounts.UserProfile;
import com.aplana.apiPractice.servlets.ProfileServlet;
import com.aplana.apiPractice.servlets.TaskValidationServlet;
import com.aplana.apiPractice.servlets.debugServlets.DebugGuiServlet;
import com.aplana.apiPractice.servlets.debugServlets.SessionsServlet;
import com.aplana.apiPractice.servlets.debugServlets.SignUpServlet;
import com.aplana.apiPractice.utils.ConfigReader;
import com.aplana.apiPractice.utils.Ipify;
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
        ConfigReader config = ConfigReader.getInstance();
        LOG.info(Ipify.getPublicIp());

        startWSDLServer(config.getWsdlIp(), config.getWsdlPort());
        startRestfulServer(Integer.parseInt(config.getRestPort()), config.getTemplateFolder());
    }

    private static void startRestfulServer(int port, String resourceBase) throws Exception {

        AccountService accountService = new AccountService();

        accountService.addNewUser(new UserProfile("admin", "admin"));
        accountService.addNewUser(new UserProfile("test", "test"));

        Server server = new Server(port);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setResourceBase(resourceBase);

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});

        server.setHandler(handlers);

        context.addServlet(new ServletHolder(new ProfileServlet()), "/rest/profiles");
        context.addServlet(new ServletHolder(new TaskValidationServlet()), "/rest/task_key");
        // DEBUG servlets
        context.addServlet(new ServletHolder(new DebugGuiServlet()), "/gui");
        context.addServlet(new ServletHolder(new SessionsServlet(accountService)), "/auth");
        context.addServlet(new ServletHolder(new SignUpServlet(accountService)), "/registration");

        server.start();
        server.join();
    }

    private static void startWSDLServer(String ip, String port) {
        String endpointAdress = "http://" + ip + ":" + port + "/wss/profiles";
        Endpoint.publish(endpointAdress, new SoapWSImpl());
        LOG.info("WSDL endpoint started at: " + endpointAdress);
    }

    public static Map<String, String> readConfig() throws IOException {
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
