package com.ars.unit;

import java.util.regex.Pattern;

public class Passenger extends Person {
    private  ContactInfo contact;
    private  PaymentInfo payment;
    private  String passport;

    private static final Pattern AUS_PASSPORT = Pattern.compile("^[A-Z]\\d{7}$");
    private static final Pattern US_PASSPORT  = Pattern.compile("^\\d{9}$");
    private static final Pattern CN_PASSPORT  = Pattern.compile("^[GE]\\d{8}$");

    // Default constructor
    public Passenger() {
        super();
        this.contact = null;
        this.payment = null;
        this.passport = null;
    }

    public void setContact(ContactInfo contact) {
        this.contact = contact;
    }

    public void setPayment(PaymentInfo payment) {
        this.payment = payment;
    }

    public void setPassport(String passport) {
        if (passport == null || !isValidPassport(passport)) {
            throw new IllegalArgumentException("Invalid passport format for AU/US/CN.");
        }
        this.passport = passport;
    }

    public void setEmail(String email) {
        // reconstruct ContactInfo with new email, preserving phone
        this.contact = new ContactInfo(email, contact.getPhoneNumber());
    }

    public void setPhoneNumber(String phoneNumber) {
        // reconstruct ContactInfo with new email, preserving phone
        this.contact = new ContactInfo(contact.getEmail(), phoneNumber);
    }

    /**
     * Main constructor now under 7 parameters by using nested ContactInfo & PaymentInfo.
     */
    public Passenger(
            String      firstName,
            String      secondName,
            int         age,
            String      gender,
            ContactInfo contact,
            String      passport,
            PaymentInfo payment
    ) {
        super(firstName, secondName, age, gender);
        if (passport == null || !isValidPassport(passport)) {
            throw new IllegalArgumentException("Invalid passport format for AU/US/CN.");
        }
        this.passport = passport;
        this.contact  = contact;
        this.payment  = payment;
    }

    private boolean isValidPassport(String p) {
        return AUS_PASSPORT.matcher(p).matches()
                || US_PASSPORT .matcher(p).matches()
                || CN_PASSPORT .matcher(p).matches();
    }

    public String getEmail()        { return contact.getEmail(); }
    public String getPhoneNumber()  { return contact.getPhoneNumber(); }
    public String getPassport()     { return passport; }
    public String getCardNumber()   { return payment.getCardNumber(); }
    public int    getSecurityCode() { return payment.getSecurityCode(); }

    @Override
    public String toString() {
        return "Passenger{" +
                "fullName=" + getFirstName() + " " + getSecondName() +
                ", email='" + getEmail() + '\'' +
                ", phoneNumber='" + getPhoneNumber() + '\'' +
                ", passport='" + passport + '\'' +
                ", cardNumber='" + getCardNumber() + '\'' +
                ", securityCode=" + getSecurityCode() +
                '}';
    }

    /**
     * Static nested class to group email & phone validation under one object.
     */
    public static class ContactInfo {
        private final String email;
        private final String phoneNumber;

        public ContactInfo(String email, String phone) {
            if (email == null || !email.contains("@") || !email.contains(".") ||
                    email.indexOf('@') > email.lastIndexOf('.') ||
                    email.indexOf('@') == 0 || email.endsWith(".")) {
                throw new IllegalArgumentException("Invalid email format.");
            }
            this.email = email;
            this.phoneNumber = formatPhone(phone);
        }

        private static String formatPhone(String raw) {
            String cleaned = raw.replaceAll("[\\s()-]", "");

            if (cleaned.startsWith("04") && cleaned.length() == 10) {
                return "+61" + cleaned.substring(1);
            } else if (cleaned.startsWith("+614") && cleaned.length() == 12) {
                return cleaned;
            } else if (cleaned.length() == 10 && Character.isDigit(cleaned.charAt(0))) {
                return "+1" + cleaned;
            } else if (cleaned.startsWith("+1") && cleaned.length() == 12) {
                return cleaned;
            } else if (cleaned.startsWith("1") && cleaned.length() == 11 &&
                    cleaned.charAt(1) >= '3' && cleaned.charAt(1) <= '9') {
                return "+86" + cleaned;
            } else if (cleaned.startsWith("+861") && cleaned.length() == 14) {
                return cleaned;
            } else {
                throw new IllegalArgumentException("Unsupported or invalid phone number.");
            }
        }

        public String getEmail()       { return email; }
        public String getPhoneNumber(){ return phoneNumber; }
    }

    /**
     * Static nested class to group card number & security code validation.
     */
    public static class PaymentInfo {
        private final String cardNumber;
        private final int    securityCode;

        public PaymentInfo(String cardNumber, int securityCode) {
            if (cardNumber == null || cardNumber.isEmpty()) {
                throw new IllegalArgumentException("Card number is required.");
            }
            if (securityCode < 0) {
                throw new IllegalArgumentException("Security code must be non-negative.");
            }
            this.cardNumber   = cardNumber;
            this.securityCode = securityCode;
        }

        public String getCardNumber()  { return cardNumber; }
        public int    getSecurityCode(){ return securityCode; }
    }
}
