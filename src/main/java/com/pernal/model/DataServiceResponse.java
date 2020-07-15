package com.pernal.model;

import org.springframework.http.HttpStatus;

public class DataServiceResponse {

    private Data body;
    private String message;
    private HttpStatus status;

    public DataServiceResponse(Data body, String message, HttpStatus status) {
        this.body = body;
        this.message = message;
        this.status = status;
    }

    public static DataServiceResponse createResponse(Data body, HttpStatus status, String message) {
        return new DataServiceResponse(body, message, status);
    }

    public static DataServiceResponse emptyBodyResponse(HttpStatus status, String message) {
        return new DataServiceResponse(null, message, status);
    }

    public Data getBody() {
        return body;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
