package com.ars.unit;

public abstract class Person //abstract class Person
{
    private String firstName;
    private String secondName;
    private int age;
    private String gender;

    public Person(){}

    public Person(String firstName, String secondName, int age, String gender){
        this.age=age;
        this.firstName=firstName;
        this.secondName=secondName;
        this.gender=gender;
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
        if (gender == null || !(gender.equals("Woman") || gender.equals("Man") ||
                gender.equals("Non-Binary") || gender.equals("Prefer not to say") || gender.equals("Other"))) {
            throw new IllegalArgumentException("Invalid gender option.");
        }
        this.gender = gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.isEmpty()) {
            throw new IllegalArgumentException("First name cannot be null or empty.");
        }

        if (!Character.isLetter(firstName.charAt(0))) {
            throw new IllegalArgumentException("First name must start with a letter.");
        }
        this.firstName = firstName;
    }

    public void setSecondName(String secondName) {

        if (secondName == null || secondName.isEmpty()) {
            throw new IllegalArgumentException("Second name cannot be null or empty.");
        }

        if (!Character.isLetter(secondName.charAt(0))) {
            throw new IllegalArgumentException("Second name must start with a letter.");
        }
        this.secondName = secondName;
    }

    @Override
    public String toString()
    {
        return "Person{" +
                "firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                '}';
    }
}
