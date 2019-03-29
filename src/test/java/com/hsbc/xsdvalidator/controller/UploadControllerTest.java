package com.hsbc.xsdvalidator.controller;

import com.hsbc.xsdvalidator.service.ValidatorService;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.xml.sax.SAXParseException;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UploadController.class)
public class UploadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ValidatorService validatorService;

    private static final String UPLOAD_URL = "/hsbc/upload/Pain001";

    @Test
    public void testValidateServiceWhenEmptyFileShouldReturnError() throws Exception {

        MockMultipartFile mockMultipartFile = new MockMultipartFile("user-file", null,
                "text/plain", "".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart(UPLOAD_URL)
                .file("file", mockMultipartFile.getBytes())
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isBadRequest()).andExpect(content().string(CoreMatchers.containsString("Please attached a file!")));
    }

    @Test
    public void testValidateServiceWhenValidFileShouldReturnOk() throws Exception {
        when(validatorService.validateXML(anyString(), any())).thenReturn(Collections.EMPTY_LIST);
        MockMultipartFile mockMultipartFile = new MockMultipartFile("user-file", null,
                "text/plain", "dummy".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart(UPLOAD_URL, "file")
                .file("file", mockMultipartFile.getBytes())
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk()).andExpect(content().string(CoreMatchers.containsString("Payment File uploaded!")));

    }

    @Test
    public void testValidateServiceWhenInvalidFileShouldReturnErrors() throws Exception {
        when(validatorService.validateXML(anyString(), any())).thenReturn(Arrays.asList("Error in xml!"));

        MockMultipartFile mockMultipartFile = new MockMultipartFile("user-file", null,
                "text/plain", "dummy".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart(UPLOAD_URL, "file")
                .file("file", mockMultipartFile.getBytes())
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isBadRequest()).andExpect(content().string(CoreMatchers.containsString("File is invalid!")));
    }

    @Test
    public void testValidateServiceWhenInvalidFileShouldReturnFatalError() throws Exception {
        when(validatorService.validateXML(anyString(), any())).thenThrow(new SAXParseException("", "", "", 1, 1));

        MockMultipartFile mockMultipartFile = new MockMultipartFile("user-file", null,
                "text/plain", "dummy".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart(UPLOAD_URL, "file")
                .file("file", mockMultipartFile.getBytes())
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isBadRequest()).andExpect(content().string(CoreMatchers.containsString("Fatal error!")));
    }

    @Test
    public void testValidateServiceWhenInvalidFileShouldReturnUnknownError() throws Exception {
        when(validatorService.validateXML(anyString(), any())).thenThrow(new NullPointerException());

        MockMultipartFile mockMultipartFile = new MockMultipartFile("user-file", null,
                "text/plain", "dummy".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart(UPLOAD_URL, "file")
                .file("file", mockMultipartFile.getBytes())
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isInternalServerError()).andExpect(content().string(CoreMatchers.containsString("Unknown error!")));
    }


}