package com.vidaplus.sghss.model.entities;

import jakarta.persistence.Entity;

@Entity
public class Patient extends User {

    //Variables
    private String address;

    //Constructors - begin
    public Patient(){}

    public Patient(String name, String dob, String email, String password, String cpf,
                   String contact, String address) {
        super(name, dob, email, password, cpf, contact);
        this.address = address;
    }
    //Constructors - end

    // Getters and setters
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
