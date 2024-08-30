package com.example.personclientmicroservices.controller;

import com.example.personclientmicroservices.models.ClientDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.personclientmicroservices.service.ClientService;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
public class ClientController {
    private final ClientService clientService;
    private final MessageSource messageSource;

    @Autowired
    public ClientController(ClientService clientService, MessageSource messageSource) {
        this.clientService = clientService;
        this.messageSource = messageSource;
    }

    @GetMapping
    public ResponseEntity<List<ClientDTO>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable Long id) {
        ClientDTO client = clientService.getClientById(id);
        return ResponseEntity.ok(client);
    }

    @GetMapping("/find/name")
    public ResponseEntity<ClientDTO> getClientByName(@RequestParam String name) {
        String clientName = Optional.ofNullable(name).orElseThrow(() -> new IllegalArgumentException(
                messageSource.getMessage("client.name.provided", null, Locale.getDefault())));
        ClientDTO client = clientService.getClientByName(clientName);
        return ResponseEntity.ok(client);
    }

    @PostMapping
    public ResponseEntity<ClientDTO> createClient(@RequestBody ClientDTO client) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.createClient(client));
    }

    @PutMapping
    public ResponseEntity<ClientDTO> updateClient(@RequestBody ClientDTO client) {
        return ResponseEntity.ok(clientService.updateClient(client.getId(), client));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}
