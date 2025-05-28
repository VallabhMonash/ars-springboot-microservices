package com.ars.unit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PassengerTest {

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
    void testValidEmail() {
        Passenger passenger = createPassenger(
                "Alice", "Wonderland", 25, "Woman",
                "alice@example.com", "0412345678", "G12345678",
                "4111111111111111", 999);
        passenger.setEmail("abc@domain.com");
        assertEquals("abc@domain.com", passenger.getEmail());
    }

    // ❌ invalid email - no @
    @Test
    void testInvalidEmailFormat() {
        Passenger passenger = createPassenger(
                "Alice", "Wonderland", 25, "Woman",
                "alice@example.com", "0412345678", "G12345678",
                "4111111111111111", 999);
        assertThrows(IllegalArgumentException.class, () -> passenger.setEmail("abc.domain.com"));
    }

    // Consolidated Phone number check
    @Test
    void testPhoneNormalizationAllAtOnce() {
        Passenger passenger = createPassenger(
                "Alice", "Wonderland", 25, "Woman",
                "alice@example.com", "0000000000",  // initial placeholder
                "G12345678", "4111111111111111", 999
        );

        assertAll("Phone normalization",
                // AU local format
                () -> {
                    passenger.setPhoneNumber("0412345678");
                    assertEquals("+61412345678", passenger.getPhoneNumber());
                },
                // AU International format
                () -> {
                    passenger.setPhoneNumber("+61412345678");
                    assertEquals("+61412345678", passenger.getPhoneNumber());
                },
                // CN local format
                () -> {
                    passenger.setPhoneNumber("13812345678");
                    assertEquals("+8613812345678", passenger.getPhoneNumber());
                },
                // CN international format
                () -> {
                    passenger.setPhoneNumber("+8613812345678");
                    assertEquals("+8613812345678", passenger.getPhoneNumber());
                },
                // US local format
                () -> {
                    passenger.setPhoneNumber("2025550123");
                    assertEquals("+12025550123", passenger.getPhoneNumber());
                },
                // US International format
                () -> {
                    passenger.setPhoneNumber("+12025550123");
                    assertEquals("+12025550123", passenger.getPhoneNumber());
                }
        );
    }


    // ❌ invalid phone number
    @Test
    void testInvalidPhoneNumberThrowsException() {
        Passenger passenger = createPassenger(
                "Alice", "Wonderland", 25, "Woman",
                "alice@example.com", "0412345678", "G12345678",
                "4111111111111111", 999);
        assertThrows(IllegalArgumentException.class, () -> passenger.setPhoneNumber("1234"));
    }

    // Consolidated Valid Passports Test
    @Test
    void testValidPassportFormatsAllAtOnce() {
        Passenger passenger = createPassenger(
                "Alice", "Wonderland", 25, "Woman",
                "alice@example.com", "0412345678",
                "G12345678",  // initial passport placeholder
                "4111111111111111", 999
        );

        assertAll("Passport formats",
                // AU format
                () -> {
                    passenger.setPassport("N1234567");
                    assertEquals("N1234567", passenger.getPassport());
                },
                // CN format
                () -> {
                    passenger.setPassport("G12345678");
                    assertEquals("G12345678", passenger.getPassport());
                },
                // US format
                () -> {
                    passenger.setPassport("123456789");
                    assertEquals("123456789", passenger.getPassport());
                }
        );
    }


    // ❌ Invalid passport number
    @Test
    void testInvalidPassportFormat() {
        Passenger passenger = createPassenger(
                "Alice", "Wonderland", 25, "Woman",
                "alice@example.com", "0412345678", "G12345678",
                "4111111111111111", 999);
        assertThrows(IllegalArgumentException.class, () -> passenger.setPassport("X123"));
    }

    // ContactInfo edge‐cases
    @Test
    void testContactInfoFormattingRemovesSpacesAndPunctuation() {
        Passenger.ContactInfo ci = new Passenger.ContactInfo("bob@example.com", "(04) 1234-5678");
        assertEquals("bob@example.com", ci.getEmail());
        assertEquals("+61412345678",    ci.getPhoneNumber());
    }

    @Test
    void testContactInfoInvalidEmailThrows() {
        assertThrows(IllegalArgumentException.class, () ->
                new Passenger.ContactInfo("no-at-symbol", "0412345678")
        );
        assertThrows(IllegalArgumentException.class, () ->
                new Passenger.ContactInfo("a@b", "0412345678")
        );
    }

    // PaymentInfo edge‐cases
    @Test
    void testPaymentInfoValidAndGetters() {
        Passenger.PaymentInfo pi = new Passenger.PaymentInfo("5555444433332222", 321);
        assertEquals("5555444433332222", pi.getCardNumber());
        assertEquals(321,                pi.getSecurityCode());
    }

    @Test
    void testPaymentInfoInvalidCardThrows() {
        assertThrows(IllegalArgumentException.class, () ->
                new Passenger.PaymentInfo("", 123)
        );
        assertThrows(IllegalArgumentException.class, () ->
                new Passenger.PaymentInfo(null, 123)
        );
    }

    @Test
    void testPaymentInfoInvalidSecurityCodeThrows() {
        assertThrows(IllegalArgumentException.class, () ->
                new Passenger.PaymentInfo("4111111111111111", -1)
        );
    }

    // setPassport edge‐cases
    @Test
    void testSetPassportThrowsOnNullOrEmpty() {
        Passenger p = createPassenger(
                "A","B",30,"X",
                "x@y.com","0412345678","G12345678","4111111111111111",123
        );
        assertThrows(IllegalArgumentException.class, () -> p.setPassport(null));
        assertThrows(IllegalArgumentException.class, () -> p.setPassport(""));
    }

    // constructor‐passport validation
    @Test
    void testConstructorThrowsOnBadPassport() {
        Passenger.ContactInfo ci = new Passenger.ContactInfo("x@y.com", "0412345678");
        Passenger.PaymentInfo pi = new Passenger.PaymentInfo("4111111111111111", 123);

        assertThrows(IllegalArgumentException.class, () ->
                new Passenger("A", "B", 30, "X", ci, "BAD", pi)
        );
    }


    // toString coverage
    @Test
    void testToStringContainsAllFields() {
        Passenger p = createPassenger(
                "Tom","Thumb",40,"Other",
                "t@t.com","0412345678","G12345678","4000123412341234",111
        );
        String s = p.toString();
        assertTrue(s.contains("Tom Thumb"));
        assertTrue(s.contains("t@t.com"));
        assertTrue(s.contains("+61412345678"));
        assertTrue(s.contains("G12345678"));
        assertTrue(s.contains("4000123412341234"));
        assertTrue(s.contains("111"));
    }

}
