package com.paulhroth.selenium.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class Interpreter {

    public static LinkedList<String[]> interpret(
            List<SaucePlatform> sauceplatforms) {
        LinkedList<String[]> platformstring = new LinkedList<String[]>();
        for (SaucePlatform sauceplatform : sauceplatforms) {
            for (Browser browser : sauceplatform.getBrowsers()) {
                for (String version : browser.getVersions()) {
                    platformstring.add(new String[] { sauceplatform.getOS(),
                            version, browser.getName() });
                }
            }
        }
        return platformstring;
    }
    
    public static String getLink(String path, String name) {
        String baseurl = "";
        File baseurls = new File(path);
        FileInputStream fis = null;
        Properties properties = new Properties();
        try {
            fis = new FileInputStream(baseurls);
            properties.load(fis);
            baseurl = properties.getProperty(name);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return baseurl;
    }
}
