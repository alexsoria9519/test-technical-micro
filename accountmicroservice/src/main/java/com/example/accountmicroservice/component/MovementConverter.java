package com.example.accountmicroservice.component;

import com.example.accountmicroservice.domain.Movement;
import com.example.accountmicroservice.models.MovementDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MovementConverter {

    public MovementDTO toDTO(Movement movement) {
        if (movement == null) {
            return null;
        }

        MovementDTO dto = new MovementDTO();
        dto.setId(movement.getId());
        dto.setMovementType(movement.getMovementType());
        dto.setAmount(movement.getAmount());
        dto.setBalance(movement.getBalance());

        if(movement.getAccount() != null) {
            dto.setAccountId(movement.getAccount().getId());
        }

        return dto;
    }

    public Movement fromDTO(MovementDTO movementDTO) {
        if (movementDTO == null) {
            return null;
        }

        Movement movement = new Movement();
        movement.setId(movementDTO.getId());
        movement.setMovementType(movementDTO.getMovementType());
        movement.setAmount(movementDTO.getAmount());
        movement.setBalance(movementDTO.getBalance());

        if(movementDTO.getAccount() != null) {
            movement.setAccount(new AccountConverter().fromDTO(movementDTO.getAccount()));
        }

        return movement;
    }

    public List<MovementDTO> fromListEntityToDTOList(List<Movement> movements) {
        return movements.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<Movement> fromListDTOToEntityList(List<MovementDTO> movementDTOS) {
        return movementDTOS.stream().map(this::fromDTO).collect(Collectors.toList());
    }

}
