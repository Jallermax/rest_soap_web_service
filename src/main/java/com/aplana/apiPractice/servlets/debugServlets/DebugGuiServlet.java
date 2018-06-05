package com.aplana.apiPractice.servlets.debugServlets;

import com.aplana.apiPractice.ProfileManager;
import com.aplana.apiPractice.SessionManager;
import com.aplana.apiPractice.templater.PageGenerator;
import com.aplana.apiPractice.utils.JsonParser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

public class DebugGuiServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(PageGenerator.updatePage(request, response, ""));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = PageGenerator.createPageVariablesMap(request);

        String message = request.getParameter("message");

        response.setContentType("text/html;charset=utf-8");

        if (message == null || message.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
        }
        pageVariables.put("message", message == null ? "" : message);

        if ("getProfileList".equals(message)) {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().println(JsonParser.createJson(ProfileManager.getSessionManagerSet(), true));

//            String profiles = "";
//            for (SessionManager session : ProfileManager.getSessionManagerSet()) {
//                profiles += "SessionId:" + session.getSessionId() + ", creationDate:" + session.getCreationDate().toString() + "\n\t" +
//                        ProfileManager.getInstance(session.getSessionId()).getProfiles().values().stream().map(profile -> JsonParser.createJson(profile, true)).collect(Collectors.toList());
//            }
//            response.getWriter().println(profiles);

//            response.getWriter().println(PageGenerator.updatePage(request, response,profiles));
        } else {
            response.getWriter().println(PageGenerator.updatePage(request, response, message == null ? "" : message));
        }
    }
}
