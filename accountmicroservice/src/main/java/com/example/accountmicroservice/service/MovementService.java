package com.example.accountmicroservice.service;

import com.example.accountmicroservice.component.AccountConverter;
import com.example.accountmicroservice.component.MovementConverter;
import com.example.accountmicroservice.domain.Account;
import com.example.accountmicroservice.domain.Movement;
import com.example.accountmicroservice.exception.AccountNotFoundException;
import com.example.accountmicroservice.exception.MovementNotFoundException;
import com.example.accountmicroservice.models.AccountDTO;
import com.example.accountmicroservice.models.MovementDTO;
import com.example.accountmicroservice.models.MovementType;
import com.example.accountmicroservice.repository.MovementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class MovementService {
    private final MovementRepository movementRepository;
    private final MovementConverter movementConverter;
    private final MessageSource messageSource;
    private final  AccountService accountService;
    private final AccountConverter accountConverter;

    @Autowired
    public MovementService(MovementRepository movementRepository, MovementConverter movementConverter, MessageSource messageSource, AccountService accountService, AccountConverter accountConverter) {
        this.movementRepository = movementRepository;
        this.movementConverter = movementConverter;
        this.messageSource = messageSource;
        this.accountService = accountService;
        this.accountConverter = accountConverter;
    }

    public List<MovementDTO> getAllMovements() {
        List<Movement> movements = movementRepository.findAll();
        return movementConverter.fromListEntityToDTOList(movements);
    }

    public MovementDTO getMovementById(Long id) {
        Optional<Movement> movement = movementRepository.findById(id);
        return movement.map(movementConverter::toDTO).orElseThrow(() -> new AccountNotFoundException(
                messageSource.getMessage("movement.not.found", new Object[]{id}, Locale.getDefault())));
    }

    public MovementDTO createMovement(MovementDTO movementDTO) {

        String movementType = Optional.ofNullable(movementDTO.getMovementType()).orElseThrow(() -> new IllegalArgumentException(
                messageSource.getMessage("movement.type.provided", null, Locale.getDefault())));
        Double amount = Optional.ofNullable(movementDTO.getAmount()).orElseThrow(() -> new IllegalArgumentException(
                messageSource.getMessage("movement.value.provided", null, Locale.getDefault())));

        String accountNumber = Optional.ofNullable(movementDTO.getAccountNumber()).orElseThrow(() -> new IllegalArgumentException(
                messageSource.getMessage("movement.account.number.provided", null, Locale.getDefault())));

        Optional.ofNullable(movementDTO.getCreatedAt()).orElseThrow(() -> new IllegalArgumentException(
                messageSource.getMessage("movement.account.date.provide", null, Locale.getDefault())));



        AccountDTO accountDTO = this.accountService.getByAccountNumber(accountNumber);
        Double accountBalance = this.accountService.getAccountBalance(accountNumber);
        Double value = this.getValue(movementType, amount);


        if(!this.validateBalance(accountBalance, value, movementType))
            throw new IllegalArgumentException(messageSource.getMessage("movement.balance.invalid", null, Locale.getDefault()));

        Movement movement = movementConverter.fromDTO(movementDTO);
        movement.setBalance(accountBalance + value);
        movement.setAccount(accountConverter.fromDTO(accountDTO));

        return movementConverter.toDTO(movementRepository.save(movement));
    }

    private Double getValue(String movementType, Double value) {
        if (movementType.equals(MovementType.DEPOSIT.toString())) {
            return value;
        } else if (movementType.equals(MovementType.WITHDRAWAL.toString())) {
            return -value;
        } else {
            throw new IllegalArgumentException(messageSource.getMessage("movement.type.invalid", null, Locale.getDefault()));
        }
    }

    private Boolean validateBalance(Double accountBalance, Double value, String movementType) {
        if (movementType.equals(MovementType.WITHDRAWAL.toString())) {
            return accountBalance + value >= 0;
        }
        return true;
    }



    public MovementDTO updateMovement(Long id, MovementDTO movementDTO) {
        if (movementRepository.existsById(id)) {
            Movement movement = movementConverter.fromDTO(movementDTO);
            movement.setId(id);
            return movementConverter.toDTO(movementRepository.save(movement));
        } else {
            throw new MovementNotFoundException(messageSource.getMessage("movement.not.found", new Object[]{id}, Locale.getDefault()));
        }
    }

    public void deleteMovement(Long id) {
        if (movementRepository.existsById(id)) {
            movementRepository.deleteById(id);
        } else {
                throw new MovementNotFoundException(messageSource.getMessage("movement.not.found", new Object[]{id}, Locale.getDefault()));
        }
    }
}
