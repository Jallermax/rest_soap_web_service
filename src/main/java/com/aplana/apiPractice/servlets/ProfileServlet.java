package com.aplana.apiPractice.servlets;

import com.aplana.apiPractice.ProfileManager;
import com.aplana.apiPractice.exceptions.DataValidation;
import com.aplana.apiPractice.exceptions.ElementExistException;
import com.aplana.apiPractice.models.AddProfileRq;
import com.aplana.apiPractice.models.Profile;
import com.aplana.apiPractice.utils.JsonParser;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static com.aplana.apiPractice.ProfileManager.idTypeRegex;
import static com.aplana.apiPractice.utils.Helpers.logException;
import static com.aplana.apiPractice.utils.JsonParser.createJson;

public class ProfileServlet extends HttpServlet{

    private static Logger LOG = Log.getLogger(ProfileServlet.class);

    private final static String TEXT_HTML = "text/html";
    private final static String TEXT_XML = "text/xml";
    private final static String APP_XML = "application/xml";
    private final static String APP_JSON = "application/json";
    private final static String defaultContentType = APP_JSON;

    @Override
    /** Получение выбранного профиля или списка*/
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(chooseContentType(req.getParameter("contentType")) + ";charset=utf-8");
        String profileId = req.getParameter("id");
        boolean prettyJson = Boolean.parseBoolean(req.getParameter("pretty"));

        String response;
        if (profileId == null || profileId.isEmpty()) {
            ArrayList<Profile> profiles = new ArrayList<>(ProfileManager.getInstance().getProfiles().values());
            response = profiles.isEmpty() ? "Profile list is empty!" : createJson(profiles, prettyJson);
        } else if (profileId.matches(idTypeRegex)){
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
        resp.setContentType(chooseContentType(req.getParameter("contentType")) + ";charset=utf-8");

        String rqJson = getJsonContent(req);

        AddProfileRq profileRq;
        try {
            profileRq = JsonParser.parseJson(rqJson, AddProfileRq.class);
        } catch (Exception e) {
            //TODO Should I set text/html for fails?
            resp.setContentType(TEXT_HTML);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print("Error parsing JSON: " + e.getCause());
            return;
        }
        LOG.debug("JSON: " + JsonParser.createJson(rqJson, true));
        try {
            profileRq.validate();
        } catch (DataValidation e) {
            resp.setContentType(TEXT_HTML);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(logException(e, true));
            return;
        }
        Profile profile = new Profile(profileRq);
        try {
            ProfileManager.getInstance().addNewProfile(profile);
        } catch (ElementExistException e) {
            resp.setContentType(TEXT_HTML);
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            resp.getWriter().print(Arrays.toString(e.getStackTrace()));
            return;
        }
        resp.getWriter().print("Added new profile:\n" + JsonParser.createJson(ProfileManager.getInstance().getProfile(profile.getId()), true));
    }

    @Override
    /** Удаление профиля*/
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(chooseContentType(req.getParameter("contentType")) + ";charset=utf-8");

        String responseMsg;
        String profileId = req.getParameter("id");
        if (profileId != null && profileId.matches(idTypeRegex)) {
            final Profile removedProfile = ProfileManager.getInstance().removeProfile(Long.valueOf(profileId));
            if (removedProfile == null) {
                resp.setContentType(TEXT_HTML);
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                responseMsg = "Profile with id: " + profileId + " not found!";
            } else {
                responseMsg = "Profile with id: " + removedProfile.getId() + "(" + removedProfile.getFullName() + ") was removed";
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseMsg = "Incorrect id type: \"" + profileId + "\"\nId type should match number format 1xxxxx";
        }
        resp.getWriter().print(responseMsg);
    }

    private static String chooseContentType(String param) {
        if ("1".equals(param) || TEXT_HTML.equals(param)) {
            return TEXT_HTML;

        } else if ("2".equals(param) || TEXT_XML.equals(param)) {
            return TEXT_XML;

        } else if ("3".equals(param) || APP_JSON.equals(param)) {
            return APP_JSON;

        } else if ("4".equals(param) || APP_XML.equals(param)) {
            return APP_XML;

        } else {
            return defaultContentType;
        }
    }

    private static String getJsonContent(HttpServletRequest req) {
        String rqJson = req.getParameter("json");

        if (APP_JSON.equals(req.getContentType())) {
            StringBuffer jb = new StringBuffer();
            String line;
            try {
                BufferedReader reader = req.getReader();
                while ((line = reader.readLine()) != null)
                    jb.append(line);
                rqJson = jb.toString();
            } catch (Exception e) {
                LOG.debug("Error reading request content: ", e);
            }
        }
        return rqJson;
    }
}
