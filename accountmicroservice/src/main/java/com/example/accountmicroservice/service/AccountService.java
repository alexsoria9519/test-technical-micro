package com.example.accountmicroservice.service;

import com.example.accountmicroservice.component.AccountConverter;
import com.example.accountmicroservice.domain.Account;
import com.example.accountmicroservice.exception.AccountNotFoundException;
import com.example.accountmicroservice.models.AccountDTO;
import com.example.accountmicroservice.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountConverter accountConverter;
    private final MessageSource messageSource;

    @Autowired
    public AccountService(AccountRepository accountRepository, AccountConverter accountConverter, MessageSource messageSource) {
        this.accountRepository = accountRepository;
        this.accountConverter = accountConverter;
        this.messageSource = messageSource;
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

    public AccountDTO createAccount(AccountDTO accountDTO) {
        Account account = accountConverter.fromDTO(accountDTO);
        return accountConverter.toDTO(accountRepository.save(account));
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


}
