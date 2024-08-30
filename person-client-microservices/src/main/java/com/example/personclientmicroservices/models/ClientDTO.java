package com.example.personclientmicroservices.models;

import lombok.Data;

@Data
public class ClientDTO {
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
