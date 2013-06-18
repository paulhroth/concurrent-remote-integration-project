package com.paulhroth.selenium.parser;

import java.util.List;

import com.paulhroth.selenium.parser.SaucePlatform;
import com.paulhroth.selenium.parser.StaXParser;

public class StaXParserTest {
	public static void main(String args[]) {
		StaXParser read = new StaXParser();
		List<SaucePlatform> readConfig = read
				.readConfig("src/main/config/config.xml");
		for (SaucePlatform sauceplatform : readConfig) {
			System.out.println(sauceplatform);
		}
	}
}