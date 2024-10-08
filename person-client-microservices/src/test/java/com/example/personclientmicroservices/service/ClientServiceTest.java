package com.example.personclientmicroservices.service;

import com.example.personclientmicroservices.component.ClientConverter;
import com.example.personclientmicroservices.domain.Client;
import com.example.personclientmicroservices.exception.ClientNotFoundException;
import com.example.personclientmicroservices.models.ClientDTO;
import com.example.personclientmicroservices.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientConverter clientConverter;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private ClientService clientService;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllClients() {
        // Arrange
        Client client1 = new Client();
        Client client2 = new Client();
        ClientDTO clientDTO1 = new ClientDTO();
        ClientDTO clientDTO2 = new ClientDTO();
        Mockito.when(clientRepository.findAll()).thenReturn(Arrays.asList(client1, client2));
        Mockito.when(clientConverter.fromListEntityToDTOList(Arrays.asList(client1, client2))).thenReturn(Arrays.asList(clientDTO1, clientDTO2));


        // Act
        List<ClientDTO> clients = clientService.getAllClients();

        // Assert
        assertNotNull(clients);
        assertEquals(2, clients.size());
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    void testGetClientById() {
        // Arrange
        Long clientId = 1L;
        Client client = new Client();
        client.setGender("Male");
        client.setName("John");
        client.setPassword("password");

        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setGender(client.getGender());
        clientDTO.setName(client.getName());

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(clientConverter.toDTO(client)).thenReturn(clientDTO);

        // Act
        ClientDTO result = clientService.getClientById(clientId);

        // Assert

        assertEquals(clientDTO, result);
        assertNull(result.getPassword());
        verify(clientRepository, times(1)).findById(clientId);
    }

    @Test
    void TestClientByName() {
        // Arrange
        Long clientId = 1L;
        Client client = new Client();
        client.setGender("Male");
        client.setName("John");
        client.setPassword("password");

        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setGender(client.getGender());
        clientDTO.setName(client.getName());

        when(clientRepository.findByName(client.getName())).thenReturn(Optional.of(client));
        when(clientConverter.toDTO(client)).thenReturn(clientDTO);

        // Act
        ClientDTO result = clientService.getClientByName(client.getName());

        // Assert
        assertEquals(clientDTO, result);
        assertNull(result.getPassword());
        verify(clientRepository, times(1)).findByName(client.getName());
    }

    @Test
    void TestClientByName_NotFound() {
        // Arrange
        String clientName = "John";
        when(clientRepository.findByName(clientName)).thenReturn(Optional.empty());
        when(messageSource.getMessage("client.not.found.by.name", new Object[]{clientName}, Locale.getDefault())).thenReturn("Client not found by name");

        // Act & Assert
        ClientNotFoundException exception = assertThrows(ClientNotFoundException.class, () -> {
            clientService.getClientByName(clientName);
        });
        assertEquals("Client not found by name", exception.getMessage());
        verify(clientRepository, times(1)).findByName(clientName);
    }


    @Test
    void testCreateClient() {
        // Arrange

        String encodedPassword = "encodedPassword";

        Client client = new Client();
        client.setGender("Male");
        client.setName("John");
        client.setId(1L);

        ClientDTO requestClientDTO = new ClientDTO();
        requestClientDTO.setGender(client.getGender());
        requestClientDTO.setName(client.getName());

        ClientDTO responseClientDTO = new ClientDTO();
        responseClientDTO.setId(client.getId());
        responseClientDTO.setGender(client.getGender());
        responseClientDTO.setName(client.getName());

        when(clientRepository.save(client)).thenReturn(client);
        when(clientConverter.toDTO(client)).thenReturn(responseClientDTO);
        when(clientConverter.fromDTO(requestClientDTO)).thenReturn(client);
        when(passwordEncoder.encode(requestClientDTO.getPassword())).thenReturn(encodedPassword);

        // Act
        ClientDTO result = clientService.createClient(requestClientDTO);

        // Assert
        assertNotNull(result);
        assertEquals(responseClientDTO, result);
        assertNull(result.getPassword());
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void testUpdateClient() {
        // Arrange
        Long clientId = 1L;

        Client client = new Client();
        client.setGender("Male");
        client.setName("John");
        client.setId(1L);

        ClientDTO requestClientDTO = new ClientDTO();
        requestClientDTO.setGender(client.getGender());
        requestClientDTO.setName(client.getName());

        ClientDTO responseClientDTO = new ClientDTO();
        responseClientDTO.setId(client.getId());
        responseClientDTO.setGender(client.getGender());
        responseClientDTO.setName(client.getName());


        client.setId(clientId);
        when(clientRepository.existsById(clientId)).thenReturn(true);
        when(clientRepository.save(client)).thenReturn(client);
        when(clientConverter.toDTO(client)).thenReturn(responseClientDTO);
        when(clientConverter.fromDTO(requestClientDTO)).thenReturn(client);

        // Act
        ClientDTO result = clientService.updateClient(clientId, requestClientDTO);

        // Assert
        assertNotNull(result);
        assertEquals(responseClientDTO, result);
        assertNull(result.getPassword());
        verify(clientRepository, times(1)).existsById(clientId);
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void testUpdateClient_NotFound() {
        // Arrange
        Long clientId = 1L;
        ClientDTO clientDTO = new ClientDTO();
        Client client = new Client();
        when(clientRepository.existsById(clientId)).thenReturn(false);
        when(clientConverter.fromDTO(clientDTO)).thenReturn(client);
        when(messageSource.getMessage("client.not.found", new Object[]{clientId}, Locale.getDefault())).thenReturn("Client not found");

        // Act & Assert
        ClientNotFoundException exception = assertThrows(ClientNotFoundException.class, () -> {
            clientService.updateClient(clientId, clientDTO);
        });
        assertEquals("Client not found", exception.getMessage());
        verify(clientRepository, times(1)).existsById(clientId);
        verify(clientRepository, times(0)).save(client);
    }

    @Test
    void testDeleteClient() {
        // Arrange
        Long clientId = 1L;
        when(clientRepository.existsById(clientId)).thenReturn(true);
        doNothing().when(clientRepository).deleteById(clientId);

        // Act
        clientService.deleteClient(clientId);

        // Assert
        verify(clientRepository, times(1)).existsById(clientId);
        verify(clientRepository, times(1)).deleteById(clientId);
    }

    @Test
    void testDeleteClient_NotFound() {
        // Arrange
        Long clientId = 1L;
        when(clientRepository.existsById(clientId)).thenReturn(false);
        when(messageSource.getMessage("client.not.found", new Object[]{clientId}, Locale.getDefault())).thenReturn("Client not found");

        // Act & Assert
        ClientNotFoundException exception = assertThrows(ClientNotFoundException.class, () -> {
            clientService.deleteClient(clientId);
        });
        assertEquals("Client not found", exception.getMessage());
        verify(clientRepository, times(1)).existsById(clientId);
        verify(clientRepository, times(0)).deleteById(clientId);
    }
}
