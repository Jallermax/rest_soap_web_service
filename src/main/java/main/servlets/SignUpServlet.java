package main.servlets;

import com.google.gson.Gson;
import main.accounts.AccountService;
import main.accounts.UserProfile;
import main.templater.PageGenerator;

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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String login = request.getParameter("login");
        String pass = request.getParameter("password");

        if (login == null) {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println(PageGenerator.updatePage(request, "Login is invalid"));
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        UserProfile profile = accountService.getUserByLogin(login);
        if (profile != null) {
            new SessionsServlet(accountService).doPost(request, response);
            return;
        }

        UserProfile userProfile = new UserProfile(login);
        accountService.addNewUser(userProfile);
        Gson gson = new Gson();
        String json = gson.toJson(userProfile);
        response.setContentType("text/html;charset=utf-8");
//        response.getWriter().println(json);
        response.getWriter().println(PageGenerator.updatePage(request, json));
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
