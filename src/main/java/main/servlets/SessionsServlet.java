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
import java.util.Map;

import static main.templater.PageGenerator.createPageVariablesMap;

/**
 * @author v.chibrikov
 *         <p>
 *         Пример кода для курса на https://stepic.org/
 *         <p>
 *         Описание курса и лицензия: https://github.com/vitaly-chibrikov/stepic_java_webserver
 */
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
