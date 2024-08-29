package com.example.personclientmicroservices.service;

import com.example.personclientmicroservices.component.ClientConverter;
import com.example.personclientmicroservices.domain.Client;
import com.example.personclientmicroservices.exception.ClientNotFoundException;
import com.example.personclientmicroservices.models.ClientDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import com.example.personclientmicroservices.repository.ClientRepository;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientConverter clientConverter;
    private final MessageSource messageSource;

    @Autowired
    public ClientService(ClientRepository clientRepository, ClientConverter clientConverter, MessageSource messageSource) {
        this.clientRepository = clientRepository;
        this.clientConverter = clientConverter;
        this.messageSource = messageSource;
    }

    public List<ClientDTO> getAllClients() {
        List<Client> clients = clientRepository.findAll();
        return clientConverter.fromListEntityToDTOList(clients);
    }

    public ClientDTO getClientById(Long id) {
        Optional<Client> client = clientRepository.findById(id);
        return client.map(clientConverter::toDTO).orElseThrow(() -> new ClientNotFoundException(
                messageSource.getMessage("client.not.found", new Object[]{id}, Locale.getDefault())));
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
            throw new ClientNotFoundException(messageSource.getMessage("client.not.found", new Object[]{id}, Locale.getDefault()));
        }
    }

    public void deleteClient(Long id) {
        if (clientRepository.existsById(id)) {
            clientRepository.deleteById(id);
        } else {
            throw new ClientNotFoundException(messageSource.getMessage("client.not.found", new Object[]{id}, Locale.getDefault()));
        }
    }


}
