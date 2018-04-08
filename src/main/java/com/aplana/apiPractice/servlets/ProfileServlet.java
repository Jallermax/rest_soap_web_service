package com.aplana.apiPractice.servlets;

import com.aplana.apiPractice.ProfileManager;
import com.aplana.apiPractice.models.Profile;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static com.aplana.apiPractice.utils.JsonParser.createJson;

public class ProfileServlet extends HttpServlet{

    @Override
    /** Получение выбранного профиля*/
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String profileId = req.getParameter("id");
        boolean prettyJson = Boolean.parseBoolean(req.getParameter("pretty"));

        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);

        String response;
        if (profileId == null || profileId.isEmpty()) {
            ArrayList<Profile> profiles = new ArrayList<>(ProfileManager.getInstance().getProfiles().values());
            response = profiles.isEmpty() ? "Profile list is empty!" : createJson(profiles, prettyJson);
        } else if (profileId.matches("[1-9][0-9]{5}")){
            Profile profile = ProfileManager.getInstance().getProfile(Long.valueOf(profileId));
            response = createJson(profile, prettyJson);
            if (profile == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response = "Profile not found! Id: " + profileId;
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response = "Incorrect id type: \"" + profileId + "\"\nId type should match number format 1xxxxx";
        }
        resp.getWriter().println(response);

    }

    @Override
    /** Обновление существующего профиля*/
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    /** Добавление нового профиля*/
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();
        Profile profile = gson.fromJson(req.getParameter("json"), Profile.class);
        ProfileManager.getInstance().addNewProfile(profile);
    }

    @Override
    /** Удаление профиля*/
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
