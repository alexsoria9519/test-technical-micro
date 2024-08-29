package com.example.personclientmicroservices.models;

import lombok.Data;

@Data
public class ErrorResponse {
    private String message;
    private int status;
    private String timestamp;
}
