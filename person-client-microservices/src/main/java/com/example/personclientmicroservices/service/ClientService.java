package com.example.personclientmicroservices.service;

import com.example.personclientmicroservices.component.ClientConverter;
import com.example.personclientmicroservices.domain.Client;
import com.example.personclientmicroservices.models.ClientDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.personclientmicroservices.repository.ClientRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientConverter clientConverter;

    @Autowired
    public ClientService(ClientRepository clientRepository, ClientConverter clientConverter) {
        this.clientRepository = clientRepository;
        this.clientConverter = clientConverter;
    }

    public List<ClientDTO> getAllClients() {
        List<Client> clients = clientRepository.findAll();
        return clientConverter.fromListEntityToDTOList(clients);
    }

    public Optional<ClientDTO> getClientById(Long id) {
        Optional<Client> client = clientRepository.findById(id);
        return client.map(clientConverter::toDTO);
    }

    public ClientDTO createClient(ClientDTO clientDTO) {
        Client client = clientConverter.fromDTO(clientDTO);
        return clientConverter.toDTO(clientRepository.save(client));
    }

    public ClientDTO updateClient(Long id, ClientDTO clientDTO) {
        if (clientRepository.existsById(id)) {
            Client client = clientConverter.fromDTO(clientDTO);
            client.setId(id);
            return clientConverter.toDTO(clientRepository.save(client));
        } else {
            throw new RuntimeException("Client not found");
        }
    }

    public void deleteClient(Long id) {
        if (clientRepository.existsById(id)) {
            clientRepository.deleteById(id);
        } else {
            throw new RuntimeException("Client not found");
        }
    }


}
