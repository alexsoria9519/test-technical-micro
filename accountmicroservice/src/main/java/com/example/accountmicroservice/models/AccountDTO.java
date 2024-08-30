package com.example.accountmicroservice.models;
import lombok.Data;

import java.util.List;

@Data
public class AccountDTO {
    private Long id;
    private String accountType;
    private String accountNumber;
    private Double initialBalance;
    private Long clientId;
    private Boolean status;
    private String clientName;
    private List<MovementDTO> movements;


}
