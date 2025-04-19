package assessment;

public class Passenger extends Person {
    private String email;
    private String phoneNumber;
    private String cardNumber;
    private int securityCode;
    private String passport;

    public Passenger() {

    }

    public Passenger(String firstName, String secondName, int age, String gender, String email, String phoneNumber, String passport, String cardNumber, int securityCode) {
        super();
        this.securityCode = securityCode;
        this.cardNumber = cardNumber;
        this.passport = passport;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return super.getFirstName();
    }

    public void setFirstName(String firstName) {
        super.setFirstName(firstName);
    }

    public String getSecondName() {
        return super.getSecondName();
    }

    public void setSecondName(String secondName) {
        super.setSecondName(secondName);
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(int securityCode) {
        this.securityCode = securityCode;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public String getGender() {
        return super.getGender();
    }

    public void setGender(String gender) {
        super.setGender(gender);
    }

    @Override
    public int getAge() {
        return super.getAge();
    }

    @Override
    public void setAge(int age) {
        super.setAge(age);
    }

    @Override
    public String toString() {
        return "Passenger{" + " Fullname= " + super.getFirstName() + " " + super.getSecondName() +
                " ,email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", passport='" + passport +
                '}';
    }
}
