package com.paulhroth.selenium.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class StaXParser {
	static final String SAUCEPLATFORM = "sauceplatform";
	static final String OS = "os";
	static final String BROWSER = "browser";
	static final String NAME = "name";
	static final String VERSIONS = "versions";

	@SuppressWarnings({ "unchecked", "null" })
	public List<SaucePlatform> readConfig(String configFile) {
		List<SaucePlatform> sauceplatforms = new ArrayList<SaucePlatform>();
		try {
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			InputStream in = new FileInputStream(configFile);
			XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
			SaucePlatform sauceplatform = null;
			Browser browser = null;

			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();

				if (event.isStartElement()) {
					StartElement startElement = event.asStartElement();
					if (startElement.getName().getLocalPart() == (SAUCEPLATFORM)) {
						sauceplatform = new SaucePlatform();
						Iterator<Attribute> attributes = startElement
								.getAttributes();
						while (attributes.hasNext()) {
							Attribute attribute = attributes.next();
							if (attribute.getName().toString().equals(OS)) {
								sauceplatform.setOS(attribute.getValue());
							}

						}
					}
					if (startElement.getName().getLocalPart() == (BROWSER)) {
						browser = new Browser();
						Iterator<Attribute> attributes = startElement
								.getAttributes();
						while (attributes.hasNext()) {
							Attribute attribute = attributes.next();
							if (attribute.getName().toString().equals(NAME)) {
								browser.setName(attribute.getValue());
							}
						}
					}
					if (event.isStartElement()) {
						if (event.asStartElement().getName().getLocalPart()
								.equals(VERSIONS)) {
							event = eventReader.nextEvent();
							browser.setVersions(event.asCharacters().getData());
							continue;
						}
					}
				}
				if (event.isEndElement()) {
					EndElement endElement = event.asEndElement();
					if (endElement.getName().getLocalPart() == (SAUCEPLATFORM)) {
						sauceplatforms.add(sauceplatform);
					}
					if (endElement.getName().getLocalPart() == (BROWSER)) {
						sauceplatform.appendBrowsers(browser);
					}
				}

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		return sauceplatforms;
	}

}