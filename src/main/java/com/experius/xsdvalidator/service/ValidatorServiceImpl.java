package com.experius.xsdvalidator.service;

import org.springframework.stereotype.Service;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ValidatorServiceImpl implements ValidatorService {

    @Override
    public List<String> validateXML(String schemaDefinition, InputStream xmlFile) throws SAXException, IOException {

        Schema schema = initializeSchema(schemaDefinition);
        Validator validator = schema.newValidator();
        List<SAXParseException> exceptions = new LinkedList<SAXParseException>();
        setValidatorErrorHandler(validator, exceptions);
        validator.validate(new StreamSource(xmlFile));

        if (exceptions.size() > 0) {
            return exceptions.stream().map(e -> e.toString()).collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    private Schema initializeSchema(String schemaDefinition) throws SAXException {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        return schemaFactory.newSchema(getSchemaDefinitionFile(schemaDefinition));
    }

    private void setValidatorErrorHandler(Validator validator, List<SAXParseException> exceptions) {
        validator.setErrorHandler(new ErrorHandler() {
            @Override
            public void warning(SAXParseException exception) throws SAXException {
                exceptions.add(exception);
            }

            @Override
            public void fatalError(SAXParseException exception) throws SAXException {
                exceptions.add(exception);
            }

            @Override
            public void error(SAXParseException exception) throws SAXException {
                exceptions.add(exception);
            }
        });
    }

    private File getSchemaDefinitionFile(String schemaDefinition) {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(classLoader.getResource(schemaDefinition).getFile());
    }
}
