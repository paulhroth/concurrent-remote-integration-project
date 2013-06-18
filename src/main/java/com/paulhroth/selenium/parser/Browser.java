package com.paulhroth.selenium.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Browser {
	private String name;
	private List<String> versions = new ArrayList<String>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getVersions() {
		return versions;
	}

	public void setVersions(String versions) {
		this.versions = Arrays.asList(versions.split(" "));
	}

	@Override
	public String toString() {
		return "[name=" + getName() + ", versions=" + versions + "]";
	}
}
