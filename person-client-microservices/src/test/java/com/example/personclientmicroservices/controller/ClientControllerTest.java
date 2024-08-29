package com.example.personclientmicroservices.controller;

import com.example.personclientmicroservices.domain.Client;
import com.example.personclientmicroservices.models.ClientDTO;
import com.example.personclientmicroservices.service.ClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ClientControllerTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    @Test
    void testGetAllClients() {
        // Arrange
        List<ClientDTO> clients = Arrays.asList(new ClientDTO(), new ClientDTO());
        Mockito.when(clientService.getAllClients()).thenReturn(clients);

        // Act
        ResponseEntity<List<ClientDTO>> response = clientController.getAllClients();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clients, response.getBody());
    }

    @Test
    void testGetClientById() {
        // Arrange
        Long id = 1L;
        ClientDTO client = new ClientDTO();
        Mockito.when(clientService.getClientById(id)).thenReturn(java.util.Optional.of(client));

        // Act
        ResponseEntity<ClientDTO> response = clientController.getClientById(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(client, response.getBody());
    }

    @Test
    void testGetClientByIdNotFound() {
        // Arrange
        Long id = 1L;
        Mockito.when(clientService.getClientById(id)).thenReturn(java.util.Optional.empty());

        // Act
        ResponseEntity<ClientDTO> response = clientController.getClientById(id);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testCreateClient() {
        // Arrange
        ClientDTO client = new ClientDTO();
        Mockito.when(clientService.createClient(client)).thenReturn(client);

        // Act
        ResponseEntity<ClientDTO> response = clientController.createClient(client);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(client, response.getBody());
    }

    @Test
    void testUpdateClient() {
        // Arrange
        Long id = 1L;
        ClientDTO client = new ClientDTO();
        client.setId(id);
        Mockito.when(clientService.updateClient(id, client)).thenReturn(client);

        // Act
        ResponseEntity<ClientDTO> response = clientController.updateClient(client);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(client, response.getBody());
    }

    @Test
    void testDeleteClient() {
        // Arrange
        Long id = 1L;

        // Act
        ResponseEntity<Void> response = clientController.deleteClient(id);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
