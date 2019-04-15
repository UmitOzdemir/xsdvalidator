package com.experius.xsdvalidator.controller;

import com.experius.xsdvalidator.model.ValidationResponse;
import com.experius.xsdvalidator.service.ValidatorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXParseException;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/hsbc")
public class UploadController {

    private ValidatorService validatorService;

    @Value("${xsd.file.pain.001.001.03}")
    private String pain001Xsd;

    public UploadController(ValidatorService validatorService) {
        this.validatorService = validatorService;
    }

    @PostMapping(value = "/upload/{xsdType}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ValidationResponse> uploadFile(@PathVariable String xsdType, @RequestParam("file") MultipartFile uploadedFile) {

        if (uploadedFile.isEmpty()) {
            return new ResponseEntity<ValidationResponse>(new ValidationResponse("Please attached a file!", Collections.emptyList()), HttpStatus.BAD_REQUEST);
        }

        try {
            List<String> errors = validatorService.validateXML(getXsdFile(xsdType), uploadedFile.getInputStream());

            if (!errors.isEmpty()) {
                return new ResponseEntity<ValidationResponse>(new ValidationResponse("File is invalid!", errors), HttpStatus.BAD_REQUEST);
            }
        } catch (SAXParseException e) {
            return new ResponseEntity<ValidationResponse>(new ValidationResponse("Fatal error!", Arrays.asList(e.getMessage())), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<ValidationResponse>(new ValidationResponse("Unknown error!", Arrays.asList(e.getMessage())), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<ValidationResponse>(new ValidationResponse("Payment File uploaded!", Collections.emptyList()), HttpStatus.OK);
    }

    private String getXsdFile(String xsdType) throws FileNotFoundException {
        if ("pain001".equalsIgnoreCase(xsdType)) {
            return pain001Xsd;
        } else {
            throw new FileNotFoundException();

        }

    }

}