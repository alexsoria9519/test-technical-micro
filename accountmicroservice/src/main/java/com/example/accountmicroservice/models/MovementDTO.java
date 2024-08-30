package com.example.accountmicroservice.models;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MovementDTO {
    private Long id;
    private String movementType;
    private Double amount;
    private Double balance;
    private Long accountId;
    private String accountNumber;
    private String movementDetail;
    private AccountDTO account;
    private LocalDate createdAt;
    private Boolean status;
}
