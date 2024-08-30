package com.example.accountmicroservice.models;

import lombok.Data;

@Data
public class MovementDTO {
    private Long id;
    private String movementType;
    private Double amount;
    private Double balance;
    private Long accountId;
    private AccountDTO account;
}
