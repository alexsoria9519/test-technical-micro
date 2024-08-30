package com.example.accountmicroservice.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MovementTest {

    @Test
    void testMovementCreation(){
        String movementType = "DEPOSIT";
        Double amount = 100.0;
        Double balance = 100.0;

        Movement movement = new Movement();
        movement.setMovementType(movementType);
        movement.setAmount(amount);
        movement.setBalance(balance);

        assertEquals(movementType, movement.getMovementType());
        assertEquals(amount, movement.getAmount());
        assertEquals(balance, movement.getBalance());
    }

    @Test
    void testMovementEquality(){
        Movement movement1 = new Movement();
        movement1.setId(1L);
        movement1.setMovementType("DEPOSIT");
        movement1.setAmount(100.0);
        movement1.setBalance(100.0);

        Movement movement2 = new Movement();
        movement2.setId(1L);
        movement2.setMovementType("DEPOSIT");
        movement2.setAmount(100.0);
        movement2.setBalance(100.0);

        assertEquals(movement1, movement2);
    }

}
