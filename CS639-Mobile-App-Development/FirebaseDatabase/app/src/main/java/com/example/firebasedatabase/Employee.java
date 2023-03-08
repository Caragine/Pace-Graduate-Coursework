package com.example.firebasedatabase;

public class Employee {

    private String lastName, firstName;

    Employee(){
        this.lastName = "empty";
        this.firstName = "empty";
    }

    Employee(String fname, String lname){
        this.firstName = fname;
        this.lastName = lname;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstname) {
        this.firstName = firstname;
    }

    public void setLastName(String lastname) {
        this.lastName = lastname;
    }

    public String toString(String ID) {
        return "ID: " + ID + "\n" + "First Name: " + firstName + "\n" + "Last Name: " + lastName + "\n" + "\n";

    }
}
