package com.example.accountmicroservice.component;

import com.example.accountmicroservice.domain.Account;
import com.example.accountmicroservice.models.AccountDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AccountConverter {

    public AccountDTO toDTO(Account account) {
        if (account == null) {
            return null;
        }

        AccountDTO dto = new AccountDTO();
        dto.setId(account.getId());
        dto.setAccountType(account.getAccountType());
        dto.setAccountNumber(account.getAccountNumber());
        dto.setInitialBalance(account.getInitialBalance());
        dto.setClientId(account.getClientId());
        dto.setStatus(account.getStatus());

        if(account.getMovements() != null) {
            dto.setMovements(new MovementConverter().fromListEntityToDTOList(account.getMovements()));
        }
        return dto;
    }

    public Account fromDTO(AccountDTO accountDTO) {
        if (accountDTO == null) {
            return null;
        }

        Account account = new Account();
        account.setId(accountDTO.getId());
        account.setAccountNumber(accountDTO.getAccountNumber());
        account.setAccountType(accountDTO.getAccountType());
        account.setInitialBalance(accountDTO.getInitialBalance());
        account.setClientId(accountDTO.getClientId());
        account.setStatus(accountDTO.getStatus());

        if(accountDTO.getMovements() != null) {
            account.setMovements(new MovementConverter().fromListDTOToEntityList(accountDTO.getMovements()));
        }

        return account;
    }

    public List<AccountDTO> fromListEntityToDTOList(List<Account> accounts) {
        return accounts.stream().map(this::toDTO).collect(Collectors.toList());
    }

}
