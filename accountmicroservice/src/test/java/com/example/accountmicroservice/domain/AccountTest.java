package com.example.accountmicroservice.domain;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {

    @Test
    void testAccountCration(){
        String accountNumber = "123456789";
        String accountType = "saving";
        Double initialBalance = 1000.0;
        Boolean status = true;
        Long clientId = 1L;

        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setAccountType(accountType);
        account.setInitialBalance(initialBalance);
        account.setStatus(status);
        account.setClientId(clientId);

        assertEquals(accountNumber, account.getAccountNumber());
        assertEquals(accountType, account.getAccountType());
        assertEquals(initialBalance, account.getInitialBalance());
        assertEquals(status, account.getStatus());
        assertEquals(clientId, account.getClientId());

    }

    @Test
    void testAccountEquality(){
        String accountNumber = "123456789";
        String accountType = "saving";
        Double initialBalance = 1000.0;
        Boolean status = true;
        Long clientId = 1L;

        Account account1 = new Account();
        account1.setAccountNumber(accountNumber);
        account1.setAccountType(accountType);
        account1.setInitialBalance(initialBalance);
        account1.setStatus(status);
        account1.setClientId(clientId);

        Account account2 = new Account();
        account2.setAccountNumber(accountNumber);
        account2.setAccountType(accountType);
        account2.setInitialBalance(initialBalance);
        account2.setStatus(status);
        account2.setClientId(clientId);

        assertEquals(account1, account2);
    }
}
