package com.aplana.apiPractice.servlets.debugServlets;

import com.aplana.apiPractice.ProfileManager;
import com.aplana.apiPractice.templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

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
            response.getWriter().println(ProfileManager.getInstance().getProfiles().values().toString());
        } else {
            response.getWriter().println(PageGenerator.updatePage(request, response, message == null ? "" : message));
        }
    }
}
