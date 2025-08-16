package com.reports.CultDataReports.exception;


import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String message;
    private final HttpStatus httpStatus;


    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public CustomException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public CustomException(String message, String message1, HttpStatus httpStatus) {
        super(message);
        this.message = message1;
        this.httpStatus = httpStatus;
    }

    public CustomException(String message, Throwable cause, String message1, HttpStatus httpStatus) {
        super(message, cause);
        this.message = message1;
        this.httpStatus = httpStatus;
    }

    public CustomException(Throwable cause, String message, HttpStatus httpStatus) {
        super(cause);
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public CustomException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String message1, HttpStatus httpStatus) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.message = message1;
        this.httpStatus = httpStatus;
    }
}
