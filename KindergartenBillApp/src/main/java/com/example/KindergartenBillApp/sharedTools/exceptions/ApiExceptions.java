package com.example.KindergartenBillApp.sharedTools.exceptions;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiExceptions extends RuntimeException {

    private final HttpStatus status;

    public ApiExceptions(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

}
