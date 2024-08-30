package com.example.accountmicroservice.exception;

public class MovementNotFoundException extends RuntimeException{
    public MovementNotFoundException(String message) {
        super(message);
    }
}
