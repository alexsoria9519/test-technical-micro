package com.example.personclientmicroservices.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "person")
@Inheritance(strategy = InheritanceType.JOINED)
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer age;
    private String name;
    private String gender;
    private String identification;
    private String address;
    private String phone;
}
