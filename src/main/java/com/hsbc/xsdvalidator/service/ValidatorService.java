package com.hsbc.xsdvalidator.service;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface ValidatorService {
    List<String> validateXML(String schemaDefinition, InputStream xmlFile) throws SAXException, IOException;
}
