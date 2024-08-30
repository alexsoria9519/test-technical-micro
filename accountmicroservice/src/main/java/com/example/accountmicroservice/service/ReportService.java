package com.example.accountmicroservice.service;

import com.example.accountmicroservice.component.AccountConverter;
import com.example.accountmicroservice.component.MovementConverter;
import com.example.accountmicroservice.models.AccountDTO;
import com.example.accountmicroservice.models.ClientResponse;
import com.example.accountmicroservice.models.MovementDTO;
import com.example.accountmicroservice.models.ReportDTO;
import com.example.accountmicroservice.repository.MovementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final MessageSource messageSource;
    private final  AccountService accountService;
    private final ClientHttpService clientHttpService;

    @Autowired
    public ReportService(MessageSource messageSource, AccountService accountService, ClientHttpService clientHttpService) {

        this.messageSource = messageSource;
        this.accountService = accountService;
        this.clientHttpService = clientHttpService;
    }

    public List<ReportDTO> getMovementsByDateClientName(String startDateStr, String endDateStr, String clientName) {

        if (startDateStr == null) {
            throw new IllegalArgumentException(messageSource.getMessage("date.provided", null, Locale.getDefault()));
        }

        if (endDateStr == null) {
            throw new IllegalArgumentException(messageSource.getMessage("date.provided", null, Locale.getDefault()));
        }

        LocalDate startDate = this.convertStringToLocalDate(startDateStr);
        LocalDate endDate = this.convertStringToLocalDate(endDateStr);


        if (clientName == null) {
            throw new IllegalArgumentException(messageSource.getMessage("client.name.provided", null, Locale.getDefault()));
        }
        ClientResponse clientResponse = clientHttpService.getClientByName(clientName);

        if (clientResponse == null) {
            throw new IllegalArgumentException(messageSource.getMessage("client.not.found.by.name", new Object[]{clientName}, Locale.getDefault()));
        }


        List<AccountDTO> accounts = accountService.findByClientId(clientResponse.getId());

        if (accounts.isEmpty()) {
            throw new IllegalArgumentException(messageSource.getMessage("account.not.found.by.client", new Object[]{clientName}, Locale.getDefault()));
        }

        return this.getMovementsByDateClientName(startDate, endDate, accounts, clientResponse);
    }

    private List<ReportDTO> getMovementsByDateClientName(LocalDate startDate, LocalDate endDate, List<AccountDTO> accounts, ClientResponse clientResponse) {
        List<ReportDTO> reports = new ArrayList<>();
        accounts.forEach(account -> {
            List<MovementDTO> movements = this.filterMovementsByDate(account.getMovements(), startDate, endDate);
            movements.forEach(movement -> {
                reports.add(this.convertData(movement, account, clientResponse));
            });
        });

        return  reports;
    }

    public List<MovementDTO> filterMovementsByDate(List<MovementDTO> movements, LocalDate startDate, LocalDate endDate) {
        return movements.stream()
                .filter(movement -> !movement.getCreatedAt().isBefore(startDate) && !movement.getCreatedAt().isAfter(endDate))
                .collect(Collectors.toList());
    }

    private ReportDTO convertData(MovementDTO movementDTO, AccountDTO accountDTO, ClientResponse clientResponse) {
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setFecha(castDate(movementDTO.getCreatedAt()));
        reportDTO.setCliente(clientResponse.getName());
        reportDTO.setNumeroCuenta(accountDTO.getAccountNumber());
        reportDTO.setTipo(accountDTO.getAccountType());
        reportDTO.setSaldoInicial(accountDTO.getInitialBalance());
        reportDTO.setEstado(movementDTO.getStatus());
        reportDTO.setMovimiento(movementDTO.getAmount());
        reportDTO.setSaldoDisponible(movementDTO.getBalance());
        return reportDTO;
    }

    private String castDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }

    private LocalDate convertStringToLocalDate(String dateStr){
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return LocalDate.parse(dateStr, formatter);
        }catch (Exception e){
            throw new IllegalArgumentException(messageSource.getMessage("date.format.invalid", null, Locale.getDefault()));
        }

    }





}
