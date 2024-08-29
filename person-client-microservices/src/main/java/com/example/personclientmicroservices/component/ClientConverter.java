package com.example.personclientmicroservices.component;

import com.example.personclientmicroservices.domain.Client;
import com.example.personclientmicroservices.models.ClientDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ClientConverter {

    private final PasswordConverter passwordConverter;

    @Autowired
    public ClientConverter(PasswordConverter passwordConverter) {
        this.passwordConverter = passwordConverter;
    }

    // Convert Client entity to ClientDTO
    public ClientDTO toDTO(Client client) {
        if (client == null) {
            return null;
        }

        ClientDTO dto = new ClientDTO();
        dto.setId(client.getId());
        dto.setName(client.getName());
        dto.setAge(client.getAge());
        dto.setGender(client.getGender());
        dto.setIdentification(client.getIdentification());
        dto.setAddress(client.getAddress());
        dto.setPhone(client.getPhone());
        // Avoid password for security
        dto.setPassword(null);
        dto.setStatus(client.getStatus());
        return dto;
    }


    // Convert ClientDTO to Client entity
    public Client fromDTO(ClientDTO clientDTO) {
        if (clientDTO == null) {
            return null;
        }

        Client client = new Client();
        client.setId(clientDTO.getId());
        client.setName(clientDTO.getName());
        client.setAge(clientDTO.getAge());
        client.setGender(clientDTO.getGender());
        client.setIdentification(clientDTO.getIdentification());
        client.setAddress(clientDTO.getAddress());
        client.setPhone(clientDTO.getPhone());
        // Encrypt the password before saving it to the database
        String encodedPassword  = passwordConverter.encodePassword(clientDTO.getPassword());
        client.setPassword(encodedPassword);
        client.setStatus(clientDTO.getStatus());
        return client;
    }

    public List<ClientDTO> fromListEntityToDTOList(List<Client> clients) {
        return clients.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
