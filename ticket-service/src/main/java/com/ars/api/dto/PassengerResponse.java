package com.ars.api.dto;

public class PassengerResponse {
    private String firstName;
    private String secondName;
    private int age;
    private String gender;
    private String email;
    private String phoneNumber;
    private String passport;
    private String cardNumber;
    private int securityCode;

    public PassengerResponse() {
    }

    public PassengerResponse(String firstName, String secondName, int age, String gender,
            String email, String phoneNumber, String passport,
            String cardNumber, int securityCode) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.passport = passport;
        this.cardNumber = cardNumber;
        this.securityCode = securityCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(int securityCode) {
        this.securityCode = securityCode;
    }
}
