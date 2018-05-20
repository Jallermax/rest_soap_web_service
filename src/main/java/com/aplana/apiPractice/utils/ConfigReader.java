package com.aplana.apiPractice.utils;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class ConfigReader {

    private static final Logger LOG = Log.getLogger(ConfigReader.class);
    private static volatile ConfigReader ourInstance = new ConfigReader();

    public static ConfigReader getInstance() {
        return ourInstance;
    }

    private Map<String, String> cfgMap;

    {
        try {
            cfgMap = readConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String wsdlIp;
    private String wsdlPort;
    private String restPort;
    private String templateFolder;


    private ConfigReader() {
        wsdlIp = System.getProperty("servers.wsdl.ip", getCfgMap().getOrDefault("servers.wsdl.ip", "0.0.0.0"));
        wsdlPort = System.getProperty("servers.wsdl.port", getCfgMap().getOrDefault("servers.wsdl.port", "9088"));
        restPort = System.getProperty("servers.rest.port", getCfgMap().getOrDefault("servers.rest.port", "9089"));
        templateFolder = System.getProperty("templateFolder", getCfgMap().getOrDefault("templateFolder", "templates"));
    }



    public Map<String, String> readConfig() throws IOException {
        Properties serverCfg = new Properties();

        InputStreamReader reader = new InputStreamReader(new FileInputStream("config.properties"), "UTF-8");
        serverCfg.load(reader);
        Map<String, String> mapCfg = serverCfg.entrySet().stream()
                .collect(Collectors.toMap(e -> String.valueOf(e.getKey()), e -> String.valueOf(e.getValue())));
        LOG.info("Config loaded:");
        mapCfg.forEach((key, value) -> LOG.info(key + " = " + value));
        return mapCfg;
    }

    public Map<String, String> getCfgMap() {
        return cfgMap;
    }

    public String getWsdlIp() {
        return wsdlIp;
    }

    public String getWsdlPort() {
        return wsdlPort;
    }

    public String getRestPort() {
        return restPort;
    }

    public String getTemplateFolder() {
        return templateFolder;
    }
}
