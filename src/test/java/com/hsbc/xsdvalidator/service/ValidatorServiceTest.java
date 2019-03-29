package com.hsbc.xsdvalidator.service;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.xml.sax.SAXParseException;

import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.*;

public class ValidatorServiceTest {

    private ValidatorService validatorService;
    private String schemaDefinition = "pain.001.001.03.xsd";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() {
        validatorService = new ValidatorServiceImpl();
    }

    @Test
    public void testValidateXMLWhenValidFileShouldReturnEmptyList() throws Exception {
        List<String> result = validatorService.validateXML(schemaDefinition, getSchemaDefinitionFile("pain.001.001.03.valid.xml"));
        assertTrue(result.isEmpty());
    }

    @Test
    public void testValidateXMLWhenValidFileShouldReturnErrorList() throws Exception {
        List<String> result = validatorService.validateXML(schemaDefinition, getSchemaDefinitionFile("pain.001.001.03.invalid.xml"));
        assertFalse(result.isEmpty());
        assertEquals(result.size(), 2);
    }

    @Test
    public void testValidateXMLWhenValidFileShouldReturnFatalError() throws Exception {
        thrown.expect(SAXParseException.class);
        validatorService.validateXML(schemaDefinition, getSchemaDefinitionFile("pain.001.001.03.fatal.xml"));
    }

    private InputStream getSchemaDefinitionFile(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        return classLoader.getResourceAsStream(fileName);
    }

}