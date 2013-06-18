package com.paulhroth.selenium.parser;

import java.util.ArrayList;
import java.util.List;

public class SaucePlatform {
	private String os;
	private List<Browser> browsers = new ArrayList<Browser>();

	public String getOS() {
		return os;
	}

	public void setOS(String os) {
		this.os = os;
	}

	public List<Browser> getBrowsers() {
		return browsers;
	}

	public void setBrowsers(List<Browser> browsers) {
		this.browsers = browsers;
	}

	public void appendBrowsers(Browser browser) {
		this.browsers.add(browser);
	}

	@Override
	public String toString() {
		return "SaucePlatform [os=" + os + ", browsers=" + browsers + "]";
	}
}
