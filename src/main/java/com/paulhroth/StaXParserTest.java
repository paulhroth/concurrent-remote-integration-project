package com.paulhroth;

import java.util.List;

import com.paulhroth.SaucePlatform;
import com.paulhroth.StaXParser;

public class StaXParserTest {
  public static void main(String args[]) {
    StaXParser read = new StaXParser();
    List<SaucePlatform> readConfig = read.readConfig("src/main/java/com/paulhroth/config.xml");
    for (SaucePlatform sauceplatform : readConfig) {
      System.out.println(sauceplatform);
    }
  }
} 