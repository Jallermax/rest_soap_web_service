package com.aplana.apiPractice.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonParser {

    public static String createJson(Object jsonObject, boolean isPretty) {
        String jsonStr;
        if (isPretty) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            jsonStr = gson.toJson(gson.toJsonTree(jsonObject));
        } else {
            Gson gson = new Gson();
            jsonStr = gson.toJson(jsonObject);
        }
        return jsonStr;
    }
}
