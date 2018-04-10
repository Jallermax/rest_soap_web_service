package com.aplana.apiPractice.templater;

import com.aplana.apiPractice.Main;
import com.aplana.apiPractice.utils.Ipify;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import com.aplana.apiPractice.utils.SafeHashMap;
import org.eclipse.jetty.util.log.Log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class PageGenerator {
    private static String HTML_DIR = "templates";

    static {
        try {
            HTML_DIR = Main.readConfig().getOrDefault("templateFolder", HTML_DIR);
        } catch (IOException e) {
            Log.getLog().debug(e);
        }
    }

    private static PageGenerator pageGenerator;
    private final Configuration cfg;

    public static PageGenerator instance() {
        if (pageGenerator == null)
            pageGenerator = new PageGenerator();
        return pageGenerator;
    }

    public String getPage(String filename, Map<String, Object> data) {
        Writer stream = new StringWriter();
        try {
            Template template = cfg.getTemplate(HTML_DIR + File.separator + filename);
            template.process(data, stream);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
        return stream.toString();
    }

    private PageGenerator() {
        cfg = new Configuration();
    }


    public static String updatePage(HttpServletRequest request, HttpServletResponse response, String message) {

        Map<String, Object> pageVariables = createPageVariablesMap(request);
        pageVariables.put("message", message);
        pageVariables.put("responseCode", String.valueOf(response.getStatus()));
        return PageGenerator.instance().getPage("page.html", pageVariables);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> createPageVariablesMap(HttpServletRequest request) {
        Map<String, Object> pageVariables = new SafeHashMap();
        String fullUrl = request.getMethod() + " " + request.getRequestURL();
        List<String> params = new ArrayList<>();
        request.getParameterMap().forEach((key, value) -> params.add(key + "=" + Arrays.toString(value).substring(1, Arrays.toString(value).length()-1)));
        if (!params.isEmpty()) {
            fullUrl += "?";
            for (String param : params) {
                fullUrl += param + "&";
            }
            fullUrl = fullUrl.substring(0, fullUrl.length()-1);
        }
        pageVariables.put("request", fullUrl + " HTTP/1.1");
        pageVariables.put("method", request.getMethod());
        pageVariables.put("URL", request.getRequestURL().toString());
        pageVariables.put("pathInfo", request.getPathInfo());
        pageVariables.put("sessionId", request.getSession().getId());
        pageVariables.put("parameters", request.getParameterMap().toString());
        pageVariables.put("publicIp", Ipify.getPublicIp());
        return pageVariables;
    }
}
