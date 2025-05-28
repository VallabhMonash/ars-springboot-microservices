package com.ars.unit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PassengerTest {

    private Passenger createPassenger(String firstName, String secondName, int age, String gender,
                                      String email, String phoneNumber, String passport, String cardNumber, int securityCode) {

        Person personMock = mock(Person.class);
        when(personMock.getFirstName()).thenReturn(firstName);
        when(personMock.getSecondName()).thenReturn(secondName);
        when(personMock.getAge()).thenReturn(age);
        when(personMock.getGender()).thenReturn(gender);

        return new Passenger(
                personMock.getFirstName(),
                personMock.getSecondName(),
                personMock.getAge(),
                personMock.getGender(),
                new Passenger.ContactInfo(email, phoneNumber),
                passport,
                new Passenger.PaymentInfo(cardNumber, securityCode));

    }

    // ✅ valid email
    @Test
    public void testValidEmail() {
        Passenger passenger = createPassenger(
                "Alice", "Wonderland", 25, "Woman",
                "alice@example.com", "0412345678", "G12345678",
                "4111111111111111", 999);
        passenger.setEmail("abc@domain.com");
        assertEquals("abc@domain.com", passenger.getEmail());
    }

    // ❌ invalid email - no @
    @Test
    public void testInvalidEmailFormat() {
        Passenger passenger = createPassenger(
                "Alice", "Wonderland", 25, "Woman",
                "alice@example.com", "0412345678", "G12345678",
                "4111111111111111", 999);
        assertThrows(IllegalArgumentException.class, () -> passenger.setEmail("abc.domain.com"));
    }

    // ✅ Valid phone number AU - local
    @Test
    public void testValidAustralianPhoneLocalFormat() {
        Passenger passenger = createPassenger(
                "Alice", "Wonderland", 25, "Woman",
                "alice@example.com", "0412345678", "G12345678",
                "4111111111111111", 999);
        passenger.setPhoneNumber("0412345678");
        assertEquals("+61412345678", passenger.getPhoneNumber());
    }

    // ✅ Valid phone number AU - international
    @Test
    public void testValidAustralianPhoneInternationalFormat() {
        Passenger passenger = createPassenger(
                "Alice", "Wonderland", 25, "Woman",
                "alice@example.com", "0412345678", "G12345678",
                "4111111111111111", 999);
        passenger.setPhoneNumber("+61412345678");
        assertEquals("+61412345678", passenger.getPhoneNumber());
    }

    // ✅ Valid phone number CN - local
    @Test
    public void testValidChinesePhoneLocalFormat() {
        Passenger passenger = createPassenger(
                "Alice", "Wonderland", 25, "Woman",
                "alice@example.com", "0412345678", "G12345678",
                "4111111111111111", 999);
        passenger.setPhoneNumber("13812345678");
        assertEquals("+8613812345678", passenger.getPhoneNumber());
    }

    // ✅ Valid phone number CN - international
    @Test
    public void testValidChinesePhoneInternationalFormat() {
        Passenger passenger = createPassenger(
                "Alice", "Wonderland", 25, "Woman",
                "alice@example.com", "0412345678", "G12345678",
                "4111111111111111", 999);
        passenger.setPhoneNumber("+8613812345678");
        assertEquals("+8613812345678", passenger.getPhoneNumber());
    }

    // ✅ Valid phone number US - local
    @Test
    public void testValidUSPhoneLocalFormat() {
        Passenger passenger = createPassenger(
                "Alice", "Wonderland", 25, "Woman",
                "alice@example.com", "0412345678", "G12345678",
                "4111111111111111", 999);
        passenger.setPhoneNumber("2025550123");
        assertEquals("+12025550123", passenger.getPhoneNumber());
    }

    // ✅ Valid phone number US - international
    @Test
    public void testValidUSPhoneInternationalFormat() {
        Passenger passenger = createPassenger(
                "Alice", "Wonderland", 25, "Woman",
                "alice@example.com", "0412345678", "G12345678",
                "4111111111111111", 999);
        passenger.setPhoneNumber("+12025550123");
        assertEquals("+12025550123", passenger.getPhoneNumber());
    }

    // ❌ invalid phone number
    @Test
    public void testInvalidPhoneNumberThrowsException() {
        Passenger passenger = createPassenger(
                "Alice", "Wonderland", 25, "Woman",
                "alice@example.com", "0412345678", "G12345678",
                "4111111111111111", 999);
        assertThrows(IllegalArgumentException.class, () -> passenger.setPhoneNumber("1234"));
    }

    // ✅ Valid passport number - AU
    @Test
    public void testValidAustralianPassport() {
        Passenger passenger = createPassenger(
                "Alice", "Wonderland", 25, "Woman",
                "alice@example.com", "0412345678", "G12345678",
                "4111111111111111", 999);
        passenger.setPassport("N1234567");
        assertEquals("N1234567", passenger.getPassport());
    }

    // ✅ Valid passport number - CN
    @Test
    public void testValidChinesePassport() {
        Passenger passenger = createPassenger(
                "Alice", "Wonderland", 25, "Woman",
                "alice@example.com", "0412345678", "G12345678",
                "4111111111111111", 999);
        passenger.setPassport("G12345678");
        assertEquals("G12345678", passenger.getPassport());
    }

    // ✅ Valid passport number - US
    @Test
    public void testValidUSPassport() {
        Passenger passenger = createPassenger(
                "Alice", "Wonderland", 25, "Woman",
                "alice@example.com", "0412345678", "G12345678",
                "4111111111111111", 999);
        passenger.setPassport("123456789");
        assertEquals("123456789", passenger.getPassport());
    }

    // ❌ Invalid passport number
    @Test
    public void testInvalidPassportFormat() {
        Passenger passenger = createPassenger(
                "Alice", "Wonderland", 25, "Woman",
                "alice@example.com", "0412345678", "G12345678",
                "4111111111111111", 999);
        assertThrows(IllegalArgumentException.class, () -> passenger.setPassport("X123"));
    }

}
