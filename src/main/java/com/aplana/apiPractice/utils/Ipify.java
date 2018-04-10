package com.aplana.apiPractice.utils;

public class Ipify {

    public static String getPublicIp() {
        try (java.util.Scanner s = new java.util.Scanner(new java.net.URL("https://api.ipify.org").openStream(), "UTF-8").useDelimiter("\\A")) {
            String ip = s.next();
//            System.out.println("My current IP address is " + ip);
            return ip;
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
