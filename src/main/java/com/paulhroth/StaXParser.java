package com.paulhroth;

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

import com.paulhroth.Browser;

public class StaXParser {
  static final String SAUCEPLATFORM = "sauceplatform";
  static final String OS = "os";
  static final String BROWSER = "browser";
  static final String NAME = "name";
  static final String VERSIONS = "versions";
  static final String CURRENT = "current";
  static final String INTERACTIVE = "interactive";

  @SuppressWarnings({ "unchecked", "null" })
  public List<SaucePlatform> readConfig(String configFile) {
    List<SaucePlatform> sauceplatforms = new ArrayList<SaucePlatform>();
    try {
      // First create a new XMLInputFactory
      XMLInputFactory inputFactory = XMLInputFactory.newInstance();
      // Setup a new eventReader
      InputStream in = new FileInputStream(configFile);
      XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
      // Read the XML document
      SaucePlatform sauceplatform = null;
      Browser browser = null;

      while (eventReader.hasNext()) {
        XMLEvent event = eventReader.nextEvent();

        if (event.isStartElement()) {
          StartElement startElement = event.asStartElement();
          // If we have a sauceplatform element we create a new item
          if (startElement.getName().getLocalPart() == (SAUCEPLATFORM)) {
            sauceplatform = new SaucePlatform();
            // We read the attributes from this tag and add the os
            // attribute to our object
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
              // We read the attributes from this tag and add the name 
              // attribute to our object
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
        // If we reach the end of an item element we add it to the list
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