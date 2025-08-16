package com.reports.CultDataReports.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String message;

    private Object object;

    public BadRequestException(String message) {
        this.message = message;
    }

}
