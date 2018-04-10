package com.yih.chasm.config;


import com.yih.chasm.net.EndPoint;
import lombok.Data;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Data
public class Config {

    private String propFileName;

    private List<EndPoint> endPoints = new ArrayList<>();

    private Integer port;

    public Config() {
        propFileName = "config.properties";
    }

    public Config(String name) {
        propFileName = name;
    }

    public void read() {
        InputStream inputStream = null;
        try {
            Properties prop = new Properties();

            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            // get the property value and print it out
            String endpoint = prop.getProperty("endpoints");
            String[] endpoints = endpoint.split(",");

            for (String ep : endpoints) {
                String host = ep.split(":")[0];
                String port = ep.split(":")[1];
                endPoints.add(new EndPoint(host, Integer.parseInt(port)));
            }

            String port = prop.getProperty("port");
            this.port = Integer.parseInt(port);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}