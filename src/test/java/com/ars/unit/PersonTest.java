package com.ars.unit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    private Person createPerson(String firstName, String secondName, int age, String gender) {
        return new Person() {
            {
                setFirstName(firstName);
                setSecondName(secondName);
                setAge(age);
                setGender(gender);
            }
        };
    }

    @Test
    void testAllFieldsRequired() {
        assertThrows(IllegalArgumentException.class, () -> {
            createPerson(null, "Smith", 25, "Woman");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            createPerson("Alice", "", 25, "Woman");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            createPerson("Alice", "Smith", 25, null);
        });
    }


    @Test
    void testValidGenderOptions() {
        assertDoesNotThrow(() -> createPerson("Alice", "Smith", 25, "Woman"));
        assertDoesNotThrow(() -> createPerson("Alice", "Smith", 25, "Man"));
        assertDoesNotThrow(() -> createPerson("Alice", "Smith", 25, "Non-Binary"));
        assertDoesNotThrow(() -> createPerson("Alice", "Smith", 25, "Prefer not to say"));
        assertDoesNotThrow(() -> createPerson("Alice", "Smith", 25, "Other"));
    }

    @Test
    void testInvalidGender() {
        assertThrows(IllegalArgumentException.class, () -> {
            createPerson("Alice", "Smith", 25, "Alien");
        });
    }


    @Test
    void testInvalidFirstNameStartWithNumber() {
        assertThrows(IllegalArgumentException.class, () -> {
            createPerson("1Alice", "Smith", 25, "Woman");
        });
    }

    @Test
    void testInvalidFirstNameStartWithSymbol() {
        assertThrows(IllegalArgumentException.class, () -> {
            createPerson("@lice", "Smith", 25, "Woman");
        });
    }

    @Test
    void testInvalidSecondNamesStartWithNumber() {
        assertThrows(IllegalArgumentException.class, () -> {
            createPerson("Alice", "9Smith", 25, "Woman");
        });
    }

    @Test
    void testInvalidSecondNamesStartWithSymbol() {
        assertThrows(IllegalArgumentException.class, () -> {
            createPerson("Alice", "$mith", 25, "Woman");
        });
    }


    @Test
    void testBoundaryValidNames() {
        assertDoesNotThrow(() -> createPerson("A", "B", 25, "Man"));
    }

}
