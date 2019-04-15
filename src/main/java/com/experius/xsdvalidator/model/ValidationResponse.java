package com.experius.xsdvalidator.model;

import java.util.List;

public class ValidationResponse {
    private String message;
    private List<String> exceptions;

    public ValidationResponse(String message, List<String> exceptions) {
        this.message = message;
        this.exceptions = exceptions;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getExceptions() {
        return exceptions;
    }
}
