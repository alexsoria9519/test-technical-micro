package com.example.accountmicroservice.models;

import lombok.Data;

@Data
public class ClientResponse {
    Long id;
    Integer age;
    String name;
    String gender;
    String identification;
    String address;
    String phone;
    String password;
    Boolean status;
}
