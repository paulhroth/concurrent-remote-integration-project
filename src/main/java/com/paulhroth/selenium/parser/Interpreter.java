package com.paulhroth.selenium.parser;

import java.util.LinkedList;
import java.util.List;

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
}
