package com.aplana.apiPractice.servlets.debugServlets;

import com.aplana.apiPractice.templater.PageGenerator;
import com.google.gson.Gson;
import com.aplana.apiPractice.accounts.AccountService;
import com.aplana.apiPractice.accounts.UserProfile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SignUpServlet extends HttpServlet {
    private final AccountService accountService;

    public SignUpServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    //sign up new user
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String login = request.getParameter("login");
        String pass = request.getParameter("password");

        if (login == null || login.isEmpty()) {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println(PageGenerator.updatePage(request, response, "Login is invalid"));
            return;
        }

        UserProfile profile = accountService.getUserByLogin(login);
        if (profile != null) {
            new SessionsServlet(accountService).doPost(request, response);
            return;
        }

        UserProfile userProfile = new UserProfile(login, pass);
        accountService.addNewUser(userProfile);
        Gson gson = new Gson();
        String json = gson.toJson(userProfile);
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(PageGenerator.updatePage(request, response, json));
    }
}
