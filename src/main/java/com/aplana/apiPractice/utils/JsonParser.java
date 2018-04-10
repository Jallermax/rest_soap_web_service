package com.aplana.apiPractice.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonParser {

    public static <T> T parseJson(String jsonStr, Class<T> clazz) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(jsonStr, clazz);
        } catch (Exception e) {
            throw e;
        }
    }

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
