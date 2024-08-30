package com.example.accountmicroservice.service;

import com.example.accountmicroservice.component.MovementConverter;
import com.example.accountmicroservice.domain.Movement;
import com.example.accountmicroservice.exception.MovementNotFoundException;
import com.example.accountmicroservice.models.MovementDTO;
import com.example.accountmicroservice.repository.MovementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MovementServiceTest {

    @Mock
    private MovementRepository movementRepository;

    @Mock
    private MovementConverter movementConverter;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private MovementService movementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void TestGetAllMovements() {
        // Arrange
        Movement movement1 = new Movement();
        Movement movement2 = new Movement();
        MovementDTO movementDTO1 = new MovementDTO();
        MovementDTO movementDTO2 = new MovementDTO();

        Mockito.when(movementRepository.findAll()).thenReturn(Arrays.asList(movement1, movement2));
        Mockito.when(movementConverter.fromListEntityToDTOList(Arrays.asList(movement1, movement2))).thenReturn(Arrays.asList(movementDTO1, movementDTO2));


        // Act

        List<MovementDTO> movements = movementService.getAllMovements();

        // Assert

        assertNotNull(movements);
        assertEquals(2, movements.size());
        verify(movementRepository, times(1)).findAll();
    }

    @Test
    void TestGetMovementById(){
        // Arrange
        Long id = 1L;
        String movementType = "DEPOSIT";
        Double amount = 100.0;
        Double balance = 100.0;

        Movement movement = new Movement();
        movement.setId(id);
        movement.setMovementType(movementType);
        movement.setAmount(amount);
        movement.setBalance(balance);

        MovementDTO movementDTO = new MovementDTO();
        movementDTO.setId(id);
        movementDTO.setMovementType(movementType);
        movementDTO.setAmount(amount);
        movementDTO.setBalance(balance);

        Mockito.when(movementRepository.findById(id)).thenReturn(java.util.Optional.of(movement));
        Mockito.when(movementConverter.toDTO(movement)).thenReturn(movementDTO);


        // Act
        MovementDTO result = movementService.getMovementById(id);

        // Assert
        assertEquals(movementDTO, result);
        verify(movementRepository, times(1)).findById(id);
    }

    @Test
    void TestCreateMovement(){
        // Arrange
        Long id = 1L;
        String movementType = "DEPOSIT";
        Double amount = 100.0;
        Double balance = 100.0;

        Movement movement = new Movement();
        movement.setId(id);
        movement.setMovementType(movementType);
        movement.setAmount(amount);
        movement.setBalance(balance);

        MovementDTO movementDTO = new MovementDTO();
        movementDTO.setId(id);
        movementDTO.setMovementType(movementType);
        movementDTO.setAmount(amount);
        movementDTO.setBalance(balance);

        Mockito.when(movementConverter.fromDTO(movementDTO)).thenReturn(movement);
        Mockito.when(movementConverter.toDTO(movement)).thenReturn(movementDTO);
        Mockito.when(movementRepository.save(movement)).thenReturn(movement);
        Mockito.when(movementConverter.toDTO(movement)).thenReturn(movementDTO);

        // Act
        MovementDTO result = movementService.createMovement(movementDTO);

        // Assert
        assertEquals(movementDTO, result);
        verify(movementRepository, times(1)).save(movement);
    }

    @Test
    void TestUpdateMovement(){
        // Arrange
        Long id = 1L;
        String movementType = "DEPOSIT";
        Double amount = 100.0;
        Double balance = 100.0;

        Movement movement = new Movement();
        movement.setId(id);
        movement.setMovementType(movementType);
        movement.setAmount(amount);
        movement.setBalance(balance);

        MovementDTO movementDTO = new MovementDTO();
        movementDTO.setId(id);
        movementDTO.setMovementType(movementType);
        movementDTO.setAmount(amount);
        movementDTO.setBalance(balance);

        Mockito.when(movementRepository.existsById(id)).thenReturn(true);
        Mockito.when(movementConverter.fromDTO(movementDTO)).thenReturn(movement);
        Mockito.when(movementRepository.save(movement)).thenReturn(movement);
        Mockito.when(movementConverter.toDTO(movement)).thenReturn(movementDTO);

        // Act
        MovementDTO result = movementService.updateMovement(id, movementDTO);

        // Assert
        assertEquals(movementDTO, result);
        verify(movementRepository, times(1)).save(movement);
    }

    @Test
    void TestUpdateAccountNotFound(){
        // Arrange
        Long id = 1L;
        MovementDTO movementDTO = new MovementDTO();
        Movement movement = new Movement();

        Mockito.when(movementRepository.existsById(id)).thenReturn(false);
        Mockito.when(messageSource.getMessage("movement.not.found", new Object[]{id}, Locale.getDefault())).thenReturn("Movement not found");

        // Act
        MovementNotFoundException exception = assertThrows(MovementNotFoundException.class, () -> {
            movementService.updateMovement(id, movementDTO);
        });

        // Assert
        assertEquals("Movement not found", exception.getMessage());
        verify(movementRepository, times(1)).existsById(id);
        verify(movementRepository, times(0)).save(movement);
    }

    @Test
    void TestDeleteMovement(){
        // Arrange
        Long id = 1L;
        Movement movement = new Movement();
        Mockito.when(movementRepository.existsById(id)).thenReturn(true);
        Mockito.doNothing().when(movementRepository).deleteById(id);

        // Act
        movementService.deleteMovement(id);

        // Assert
        verify(movementRepository, times(1)).existsById(id);
        verify(movementRepository, times(1)).deleteById(id);

    }

    @Test
    void TestDeleteMovementNotFound(){
        // Arrange
        Long id = 1L;
        Mockito.when(movementRepository.existsById(id)).thenReturn(false);
        Mockito.when(messageSource.getMessage("movement.not.found", new Object[]{id}, Locale.getDefault())).thenReturn("Movement not found");

        // Act
        MovementNotFoundException exception = assertThrows(MovementNotFoundException.class, () -> {
            movementService.deleteMovement(id);
        });

        // Assert
        assertEquals("Movement not found", exception.getMessage());
        verify(movementRepository, times(1)).existsById(id);
        verify(movementRepository, times(0)).deleteById(id);
    }

}
