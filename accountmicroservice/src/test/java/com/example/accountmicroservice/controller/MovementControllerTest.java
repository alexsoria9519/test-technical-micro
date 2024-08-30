package com.example.accountmicroservice.controller;

import com.example.accountmicroservice.models.MovementDTO;
import com.example.accountmicroservice.service.MovementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class MovementControllerTest {

    @Mock
    private MovementService movementService;

    @InjectMocks
    private MovementController movementController;

    @Test
    void testGetAllMovements() {
        // Arrange
        List<MovementDTO> movements = Arrays.asList(new MovementDTO(), new MovementDTO());
        Mockito.when(movementService.getAllMovements()).thenReturn(movements);

        // Act
        ResponseEntity<List<MovementDTO>> response = movementController.getAllMovements();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(movements, response.getBody());

    }

    @Test
    void testGetMovementById() {
        // Arrange
        Long id = 1L;
        MovementDTO movement = new MovementDTO();
        Mockito.when(movementService.getMovementById(id)).thenReturn(movement);

        // Act

        ResponseEntity<MovementDTO> response = movementController.getMovementById(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(movement, response.getBody());
    }

    @Test
    void testCreateMovement() {
        // Arrange
        MovementDTO movement = new MovementDTO();
        Mockito.when(movementService.createMovement(movement)).thenReturn(movement);

        // Act

        ResponseEntity<MovementDTO> response = movementController.createMovement(movement);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(movement, response.getBody());
    }

    @Test
    void testUpdateMovement() {
        // Arrange
        Long id = 1L;
        MovementDTO movement = new MovementDTO();
        Mockito.when(movementService.updateMovement(id, movement)).thenReturn(movement);

        // Act

        ResponseEntity<MovementDTO> response = movementController.updateMovement(id, movement);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(movement, response.getBody());
    }

    @Test
    void testDeleteMovement() {
        // Arrange
        Long id = 1L;
        Mockito.doNothing().when(movementService).deleteMovement(id);

        // Act

        ResponseEntity<Void> response = movementController.deleteMovement(id);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

}
