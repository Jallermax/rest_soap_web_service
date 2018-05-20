package com.aplana.apiPractice.utils;

public class Helpers {


    public static String logException(Throwable e, boolean printStackTrace) {
        return printStackTrace ? e.toString() : e.getCause() != null
                ? e.getCause().toString()
                : e.getMessage();
    }
}
