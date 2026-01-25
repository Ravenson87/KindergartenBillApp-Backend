package com.example.KindergartenBillApp.sharedTools.exceptions;

public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }

    // Kada imam gresku da moze da mi vrati poruku,
    // ali i orginalni razlog sta je greska
    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }
}
