package com.example.personclientmicroservices.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClientTest {

    @Test
    void testClientCreation() {
        // Arrange
        String name = "John";
        Integer age = 25;
        String gender = "Male";
        String identification = "12345";
        String address = "1234 Main St";
        String phone = "123-456-7890";

        // When
        Client client = new Client();
        client.setName(name);
        client.setAge(age);
        client.setGender(gender);
        client.setIdentification(identification);
        client.setAddress(address);
        client.setPhone(phone);


        // Then
        assertEquals(name, client.getName());
        assertEquals(age, client.getAge());
        assertEquals(gender, client.getGender());
        assertEquals(identification, client.getIdentification());
        assertEquals(address, client.getAddress());
        assertEquals(phone, client.getPhone());
    }

    @Test
    void testClientInheritanceFromPerson() {
        // Given
        Client client = new Client();
        client.setName("Jane");
        client.setAge(25);

        // When & Then
        assertTrue(client instanceof Person);
        assertEquals("Jane", client.getName());
        assertEquals(25, client.getAge());
    }

    @Test
    void testClientEquality() {
        // Given
        Client client1 = new Client();
        client1.setId(1L);
        client1.setName("Alice");
        client1.setAge(25);
        client1.setGender("Male");

        Client client2 = new Client();
        client2.setId(1L);
        client2.setName("Alice");
        client2.setAge(25);
        client2.setGender("Male");

        // When & Then
        assertEquals(client1, client2);
    }
}
