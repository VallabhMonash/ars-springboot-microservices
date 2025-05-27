package com.ars.unit;

public class Passenger extends Person
{
    private String email;
    private String phoneNumber;
    private String cardNumber;
    private int securityCode;
    private String passport;

    public Passenger(){}

    public Passenger(String firstName, String secondName, int age, String gender,String email, String phoneNumber, String passport, String cardNumber,int securityCode)
    {
        super(firstName, secondName, age, gender);
        setEmail(email);
        setPhoneNumber(phoneNumber);
        setPassport(passport);
        setCardNumber(cardNumber);
        setSecurityCode(securityCode);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || !email.contains("@") || !email.contains(".") ||
                email.indexOf('@') > email.lastIndexOf('.') ||
                email.indexOf('@') == 0 || email.endsWith("."))
            throw new IllegalArgumentException("Invalid email format.");
        this.email = email;
    }

    public String getFirstName() {
        return super.getFirstName();
    }

    public String getSecondName() {
        return super.getSecondName();
    }

    public void setSecondName(String secondName) {
        super.setSecondName(secondName);
    }

    public void setFirstName(String firstName) {
        super.setFirstName(firstName);
    }

    public String getPassport() {
        return passport;
    }

    public void setGender(String gender) {
        super.setGender(gender);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getSecurityCode() {
        return securityCode;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setSecurityCode(int securityCode) {
        this.securityCode = securityCode;
    }

    @Override
    public void setAge(int age) {
        super.setAge(age);
    }

    private boolean isValidPassport(String passport) {
        // Australia: 1 letter + 7 digits
        if (passport.length() == 8 && Character.isUpperCase(passport.charAt(0))) {
            for (int i = 1; i < 8; i++) {
                if (!Character.isDigit(passport.charAt(i))) return false;
            }
            return true;
        }

        // USA: 9 digits
        if (passport.length() == 9) {
            boolean allDigits = true;
            for (int i = 0; i < 9; i++) {
                if (!Character.isDigit(passport.charAt(i))) {
                    allDigits = false;
                    break;
                }
            }
            if (allDigits) return true;

            // China: G or E + 8 digits
            char first = passport.charAt(0);
            if ((first == 'G' || first == 'E')) {
                for (int i = 1; i < 9; i++) {
                    if (!Character.isDigit(passport.charAt(i))) return false;
                }
                return true;
            }
        }

        return false;
    }

    public void setPassport(String passport) {
        if (passport == null || passport.isEmpty()) {
            throw new IllegalArgumentException("Passport number is required.");
        }

        if (!isValidPassport(passport)) {
            throw new IllegalArgumentException("Invalid passport format for AU/US/CN.");
        }

        this.passport = passport;
    }

    @Override
    public String getGender() {
        return super.getGender();
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            throw new IllegalArgumentException("Phone number is required.");
        }

        String cleaned = phoneNumber.replaceAll("[\\s\\-()]", "");
        if (cleaned.startsWith("04") && cleaned.length() == 10) {
            this.phoneNumber = "+61" + cleaned.substring(1); // AU
        } else if (cleaned.startsWith("+614") && cleaned.length() == 12) {
            this.phoneNumber = cleaned;
        } else if (cleaned.length() == 10 && Character.isDigit(cleaned.charAt(0))) {
            this.phoneNumber = "+1" + cleaned; // US
        } else if (cleaned.startsWith("+1") && cleaned.length() == 12) {
            this.phoneNumber = cleaned;
        } else if (cleaned.startsWith("1") && cleaned.length() == 11 && cleaned.charAt(1) >= '3' && cleaned.charAt(1) <= '9') {
            this.phoneNumber = "+86" + cleaned; // CN
        } else if (cleaned.startsWith("+861") && cleaned.length() == 14) {
            this.phoneNumber = cleaned;
        } else {
            throw new IllegalArgumentException("Unsupported or invalid phone number.");
        }
    }

    @Override
    public int getAge() {
        return super.getAge();
    }

    @Override
    public String toString()
    {
        return "Passenger{" + " Fullname= "+ super.getFirstName()+" "+super.getSecondName()+
                " ,email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", passport='" + passport +
                '}';
    }
}
