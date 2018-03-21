package main;

import main.accounts.AccountService;
import main.accounts.UserProfile;
import main.servlets.RequestsServlet;
import main.servlets.SessionsServlet;
import main.servlets.SignUpServlet;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception {
        AccountService accountService = new AccountService();

        accountService.addNewUser(new UserProfile("admin"));
        accountService.addNewUser(new UserProfile("test"));

        Server server = new Server(26565);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setResourceBase("L2.1 Authorization" + File.separator + "public_html");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});

        server.setHandler(handlers);

        context.addServlet(new ServletHolder(new RequestsServlet()), "/*");
        context.addServlet(new ServletHolder(new SessionsServlet(accountService)), "/signin");
        context.addServlet(new ServletHolder(new SignUpServlet(accountService)), "/signup");

        server.start();
        server.join();
    }
}
