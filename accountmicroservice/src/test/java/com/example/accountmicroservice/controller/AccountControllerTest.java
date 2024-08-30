package com.example.accountmicroservice.controller;

import com.example.accountmicroservice.models.AccountDTO;
import com.example.accountmicroservice.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    @Test
    void testGetAllAccounts() {
        // Arrange
        List<AccountDTO> accounts = Arrays.asList(new AccountDTO(), new AccountDTO());
        Mockito.when(accountService.getAllAccounts()).thenReturn(accounts);

        // Act

        ResponseEntity<List<AccountDTO>> response = accountController.getAllAccounts();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(accounts, response.getBody());

    }

    @Test
    void testGetAccountById() {
        // Arrange
        Long id = 1L;
        AccountDTO account = new AccountDTO();
        Mockito.when(accountService.getAccountById(id)).thenReturn(account);

        // Act

        ResponseEntity<AccountDTO> response = accountController.getAccountById(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(account, response.getBody());

    }

    @Test
    void testCreateAccount() {
        // Arrange
        AccountDTO account = new AccountDTO();
        Mockito.when(accountService.createAccount(account)).thenReturn(account);

        // Act
        ResponseEntity<AccountDTO> response = accountController.createAccount(account);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(account, response.getBody());
    }

    @Test
    void testUpdateAccount() {
        // Arrange
        AccountDTO account = new AccountDTO();
        Mockito.when(accountService.updateAccount(account.getId(), account)).thenReturn(account);

        // Act
        ResponseEntity<AccountDTO> response = accountController.updateAccount(account);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(account, response.getBody());
    }

    @Test
    void testDeleteAccount() {
        // Arrange
        Long id = 1L;
        Mockito.doNothing().when(accountService).deleteAccount(id);

        // Act
        ResponseEntity<Void> response = accountController.deleteAccount(id);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }



}
