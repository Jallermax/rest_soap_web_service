package com.aplana.apiPractice.exceptions;

public class DataValidation extends Exception {

    public DataValidation() {
        super();
    }

    public DataValidation(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable getCause() {
        return super.getCause();
    }
}
