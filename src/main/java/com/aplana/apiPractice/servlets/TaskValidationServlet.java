package com.aplana.apiPractice.servlets;

import com.aplana.apiPractice.ProfileManager;
import com.aplana.apiPractice.TaskValidation;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TaskValidationServlet extends HttpServlet {

    @Override
    /** Получение инструкции для получения ответа на задание*/
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //TODO task doc
        resp.setContentType("text/html");
        resp.getWriter().println("To get answer key for task, you should add valid profile.");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String response = new TaskValidation(TaskValidation.ServiceType.REST).checkValid(ProfileManager.getInstance(req.getRemoteAddr()).getProfiles().values());
        resp.setContentType("text/html");
        resp.getWriter().println(response);
    }
}
