package com.reports.CultDataReports.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidTokenException extends RuntimeException {

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

    public InvalidTokenException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public InvalidTokenException(String message, String message1, HttpStatus httpStatus) {
        super(message);
        this.message = message1;
        this.httpStatus = httpStatus;
    }

    public InvalidTokenException(String message, Throwable cause, String message1, HttpStatus httpStatus) {
        super(message, cause);
        this.message = message1;
        this.httpStatus = httpStatus;
    }

    public InvalidTokenException(Throwable cause, String message, HttpStatus httpStatus) {
        super(cause);
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public InvalidTokenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String message1, HttpStatus httpStatus) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.message = message1;
        this.httpStatus = httpStatus;
    }
}
