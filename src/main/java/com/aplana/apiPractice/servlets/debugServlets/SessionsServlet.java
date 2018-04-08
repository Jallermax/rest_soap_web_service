package com.aplana.apiPractice.servlets.debugServlets;

import com.aplana.apiPractice.accounts.UserProfile;
import com.aplana.apiPractice.templater.PageGenerator;
import com.google.gson.Gson;
import com.aplana.apiPractice.accounts.AccountService;
import org.eclipse.jetty.server.Request;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SessionsServlet extends HttpServlet {
    private final AccountService accountService;

    public SessionsServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    //get logged user profile
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        doDelete(request, response);
        if (1==1)
        return;
        String sessionId = request.getSession().getId();
        UserProfile profile = accountService.getUserBySessionId(sessionId);
        if (profile == null) {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println(PageGenerator.updatePage(request, response, "Unauthorized"));


        } else {
            Gson gson = new Gson();
            String json = gson.toJson(profile);
            response.setContentType("text/html;charset=utf-8");
//            response.getWriter().println(json);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(PageGenerator.updatePage(request, response, json));

        }
    }

    //sign in
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String pass = request.getParameter("password");

        if (login == null || pass == null || login.isEmpty() || pass.isEmpty()) {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println(PageGenerator.updatePage(request, response, "Login or Password is empty!"));
            return;
        }

        UserProfile profile = accountService.getUserByLogin(login);
        if (profile == null || !profile.getPass().equals(pass)) {
            response.setContentType("text/html;charset=utf-8");

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println(PageGenerator.updatePage(request, response, "Unauthorized"));
            return;
        }

        accountService.addSession(request.getSession().getId(), profile);
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(PageGenerator.updatePage(request, response, "Authorized: " + profile.getLogin()));
    }

    //sign out
    public void doDelete(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        ((Request)request).setMethod("DELETE"); //Dirty hack because of html lack of actions
        String sessionId = request.getSession().getId();
        UserProfile profile = accountService.getUserBySessionId(sessionId);
        if (profile == null) {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println(PageGenerator.updatePage(request, response, "Unauthorized!"));

        } else {
            accountService.deleteSession(sessionId);
            response.setContentType("text/html;charset=utf-8");
//            response.getWriter().println("Goodbye!");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(PageGenerator.updatePage(request, response, "Goodbye, " + profile.getLogin()));

        }

    }
}
