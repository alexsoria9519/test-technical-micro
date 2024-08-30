package com.example.accountmicroservice.service;
import com.example.accountmicroservice.component.AccountConverter;
import com.example.accountmicroservice.domain.Account;
import com.example.accountmicroservice.exception.AccountNotFoundException;
import com.example.accountmicroservice.models.AccountDTO;
import com.example.accountmicroservice.models.ClientResponse;
import com.example.accountmicroservice.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.context.MessageSource;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountConverter accountConverter;

    @Mock
    private ClientHttpService clientHttpService;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private AccountService accountService;





    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllAccounts() {
        // Arrange

        Account account1 = new Account();
        Account account2 = new Account();
        AccountDTO accountDTO1 = new AccountDTO();
        AccountDTO accountDTO2 = new AccountDTO();

        Mockito.when(accountRepository.findAll()).thenReturn(Arrays.asList(account1, account2));
        Mockito.when(accountConverter.fromListEntityToDTOList(Arrays.asList(account1, account2))).thenReturn(Arrays.asList(accountDTO1, accountDTO2));

        // Act

        List<AccountDTO> accounts = accountService.getAllAccounts();

        // Assert
        assertNotNull(accounts);
        assertEquals(2, accounts.size());
        verify(accountRepository, times(1)).findAll();

    }

    @Test
    void testGetAccountById() {
        // Arrange
        Long id = 1L;
        String accountNumber = "123456";
        String accountType = "SAVINGS";
        Boolean status = true;
        Long clientId = 1L;
        Double initialBalance = 100.0;

        Account account = new Account();
        account.setId(id);
        account.setInitialBalance(initialBalance);
        account.setAccountNumber(accountNumber);
        account.setAccountType(accountType);
        account.setStatus(status);
        account.setClientId(clientId);

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(id);
        accountDTO.setInitialBalance(initialBalance);
        accountDTO.setAccountNumber(accountNumber);
        accountDTO.setAccountType(accountType);
        accountDTO.setStatus(status);
        accountDTO.setClientId(clientId);

        Mockito.when(accountRepository.findById(id)).thenReturn(java.util.Optional.of(account));
        Mockito.when(accountConverter.toDTO(account)).thenReturn(accountDTO);

        // Act

        AccountDTO result = accountService.getAccountById(id);

        // Assert

        assertEquals(accountDTO, result);
        verify(accountRepository, times(1)).findById(id);

    }

    @Test
    void testCreateAccount(){
        // Arrange
        Long id = 1L;
        String accountNumber = "123456";
        String accountType = "SAVINGS";
        Boolean status = true;
        Long clientId = 1L;
        Double initialBalance = 100.0;
        String clientName = "John";

        Account account = new Account();
        account.setId(id);
        account.setInitialBalance(initialBalance);
        account.setAccountNumber(accountNumber);
        account.setAccountType(accountType);
        account.setStatus(status);
        account.setClientId(clientId);

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(id);
        accountDTO.setInitialBalance(initialBalance);
        accountDTO.setAccountNumber(accountNumber);
        accountDTO.setAccountType(accountType);
        accountDTO.setStatus(status);
        accountDTO.setClientId(clientId);
        accountDTO.setClientName(clientName);

        ClientResponse clientResponse = new ClientResponse();
        clientResponse.setId(clientId);
        clientResponse.setName(clientName);


        Mockito.when(clientHttpService.getClientByName(clientName)).thenReturn(clientResponse);
        Mockito.when(accountConverter.fromDTO(accountDTO)).thenReturn(account);
        Mockito.when(accountConverter.toDTO(account)).thenReturn(accountDTO);
        Mockito.when(accountRepository.save(account)).thenReturn(account);


        // Act

        AccountDTO result = accountService.createAccount(accountDTO);

        // Assert

        assertNotNull(result);
        assertEquals(accountDTO, result);
        verify(clientHttpService, times(1)).getClientByName(clientName);
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void testCreateAccountWithoutClientName(){
        Long id = 1L;
        String accountNumber = "123456";
        String accountType = "SAVINGS";
        Boolean status = true;
        Long clientId = 1L;
        Double initialBalance = 100.0;

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(id);
        accountDTO.setInitialBalance(initialBalance);
        accountDTO.setAccountNumber(accountNumber);
        accountDTO.setAccountType(accountType);
        accountDTO.setStatus(status);
        accountDTO.setClientId(clientId);
        accountDTO.setClientName(null);

        Mockito.when(messageSource.getMessage("client.name.provided", null, Locale.getDefault())).thenReturn("Client name not provided");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.createAccount(accountDTO);
        });

        assertEquals("Client name not provided", exception.getMessage());

    }


    @Test
    void TestUpdateAccount(){
        // Arrange
        Long id = 1L;
        String accountNumber = "123456";
        String accountType = "SAVINGS";
        Boolean status = true;
        Long clientId = 1L;
        Double initialBalance = 100.0;


        Account account = new Account();
        account.setId(id);
        account.setInitialBalance(initialBalance);
        account.setAccountNumber(accountNumber);
        account.setAccountType(accountType);
        account.setStatus(status);
        account.setClientId(clientId);

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(id);
        accountDTO.setInitialBalance(initialBalance);
        accountDTO.setAccountNumber(accountNumber);
        accountDTO.setAccountType(accountType);
        accountDTO.setStatus(status);
        accountDTO.setClientId(clientId);

        Mockito.when(accountRepository.existsById(id)).thenReturn(true);
        Mockito.when(accountConverter.fromDTO(accountDTO)).thenReturn(account);
        Mockito.when(accountConverter.toDTO(account)).thenReturn(accountDTO);
        Mockito.when(accountRepository.save(account)).thenReturn(account);

        // Act

        AccountDTO result = accountService.updateAccount(id, accountDTO);

        // Assert

        assertNotNull(result);
        assertEquals(accountDTO, result);
        verify(accountRepository, times(1)).existsById(id);
        verify(accountRepository, times(1)).save(account);

    }

    @Test
    void TestUpdateAccountNotFound() {
        // Arrange
        Long id = 1L;
        AccountDTO accountDTO = new AccountDTO();
        Account account = new Account();

        Mockito.when(accountRepository.existsById(id)).thenReturn(false);
        Mockito.when(messageSource.getMessage("account.not.found", new Object[]{id}, Locale.getDefault())).thenReturn("Account not found");

        // Act & Assert

        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> {
            accountService.updateAccount(id, accountDTO);
        });

        assertEquals("Account not found", exception.getMessage());
        verify(accountRepository, times(1)).existsById(id);
        verify(accountRepository, times(0)).save(account);
    }

    @Test
    void TestDeleteAccount(){
        // Arrange
        Long id = 1L;

        Mockito.when(accountRepository.existsById(id)).thenReturn(true);
        Mockito.doNothing().when(accountRepository).deleteById(id);

        // Act

        accountService.deleteAccount(id);

        // Assert

        verify(accountRepository, times(1)).existsById(id);
        verify(accountRepository, times(1)).deleteById(id);
    }

    @Test
    void TestDeleteAccountNotFound(){
        // Arrange
        Long id = 1L;

        Mockito.when(accountRepository.existsById(id)).thenReturn(false);
        Mockito.when(messageSource.getMessage("account.not.found", new Object[]{id}, Locale.getDefault())).thenReturn("Account not found");

        // Act & Assert

        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> {
            accountService.deleteAccount(id);
        });

        assertEquals("Account not found", exception.getMessage());
        verify(accountRepository, times(1)).existsById(id);
        verify(accountRepository, times(0)).deleteById(id);
    }

}
