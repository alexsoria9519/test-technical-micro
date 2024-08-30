package com.example.accountmicroservice.service;

import com.example.accountmicroservice.component.MovementConverter;
import com.example.accountmicroservice.domain.Movement;
import com.example.accountmicroservice.exception.AccountNotFoundException;
import com.example.accountmicroservice.exception.MovementNotFoundException;
import com.example.accountmicroservice.models.MovementDTO;
import com.example.accountmicroservice.repository.MovementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class MovementService {
    private final MovementRepository movementRepository;
    private final MovementConverter movementConverter;
    private final MessageSource messageSource;

    @Autowired
    public MovementService(MovementRepository movementRepository, MovementConverter movementConverter, MessageSource messageSource) {
        this.movementRepository = movementRepository;
        this.movementConverter = movementConverter;
        this.messageSource = messageSource;
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
        Movement movement = movementConverter.fromDTO(movementDTO);
        return movementConverter.toDTO(movementRepository.save(movement));
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
