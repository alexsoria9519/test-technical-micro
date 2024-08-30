package com.example.accountmicroservice.service;

import com.example.accountmicroservice.component.AccountConverter;
import com.example.accountmicroservice.domain.Account;
import com.example.accountmicroservice.domain.Movement;
import com.example.accountmicroservice.exception.AccountNotFoundException;
import com.example.accountmicroservice.models.AccountDTO;
import com.example.accountmicroservice.models.ClientResponse;
import com.example.accountmicroservice.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountConverter accountConverter;
    private final MessageSource messageSource;
    private final ClientHttpService clientHttpService;

    @Autowired
    public AccountService(AccountRepository accountRepository, AccountConverter accountConverter, MessageSource messageSource, RestTemplate restTemplate, ClientHttpService clientHttpService) {
        this.accountRepository = accountRepository;
        this.accountConverter = accountConverter;
        this.messageSource = messageSource;
        this.clientHttpService = clientHttpService;
    }

    public List<AccountDTO> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accountConverter.fromListEntityToDTOList(accounts);
    }

    public AccountDTO getAccountById(Long id) {
        Optional<Account> account = accountRepository.findById(id);
        return account.map(accountConverter::toDTO).orElseThrow(() -> new AccountNotFoundException(
                messageSource.getMessage("account.not.found", new Object[]{id}, Locale.getDefault())));
    }

    public List<AccountDTO> findByClientId(Long clientId){
        List<Account> accounts = accountRepository.findByClientId(clientId);
        return accountConverter.fromListEntityToDTOList(accounts);
    }

    public AccountDTO getByAccountNumber(String accounNumber){
        Optional<Account> account = accountRepository.findByAccountNumber(accounNumber);
        return account.map(accountConverter::toDTO).orElseThrow(() -> new AccountNotFoundException(
                messageSource.getMessage("account.not.found.by.number", new Object[]{accounNumber}, Locale.getDefault())));
    }

    public AccountDTO createAccount(AccountDTO accountDTO){
        String clientName = accountDTO.getClientName();
        if (clientName == null) {
            throw new IllegalArgumentException(messageSource.getMessage("client.name.provided", null, Locale.getDefault()));
        }
        ClientResponse clientResponse = clientHttpService.getClientByName(accountDTO.getClientName());
        accountDTO.setClientId(clientResponse.getId());
        Account account = accountConverter.fromDTO(accountDTO);
        AccountDTO response = accountConverter.toDTO(accountRepository.save(account));
        response.setClientName(clientResponse.getName());
        return response;
    }

    public AccountDTO updateAccount(Long id, AccountDTO accountDTO) {
        if (accountRepository.existsById(id)) {
            Account account = accountConverter.fromDTO(accountDTO);
            account.setId(id);
            return accountConverter.toDTO(accountRepository.save(account));
        } else {
            throw new AccountNotFoundException(messageSource.getMessage("account.not.found", new Object[]{id}, Locale.getDefault()));
        }
    }

    public void deleteAccount(Long id) {
        if (accountRepository.existsById(id)) {
            accountRepository.deleteById(id);
        } else {
                throw new AccountNotFoundException(messageSource.getMessage("account.not.found", new Object[]{id}, Locale.getDefault()));
        }
    }

    public Double getAccountBalance(String accountNumber){
        Optional<Account> account = accountRepository.findByAccountNumber(accountNumber);

        if(account.isEmpty()){
            throw new AccountNotFoundException(messageSource.getMessage("account.not.found.by.number", new Object[]{accountNumber}, Locale.getDefault()));
        }

        List<Movement> movements = account.get().getMovements();
        Double balance = account.get().getInitialBalance();

        for(Movement movement : movements){
            balance += movement.getAmount();
        }

        return balance;
    }


}
