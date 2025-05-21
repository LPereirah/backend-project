package com.vidaplus.sghss.model.entities;

import jakarta.persistence.Entity;

@Entity
public class Patient extends User {

    private String address;

    public Patient(){}

    public Patient(String name, String dob, String email, String password, String cpf, String contact, String address) {
        super(name, dob, email, password, cpf, contact);
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return  "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", dob='" + getDob() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", cpf='" + getCpf() + '\'' +
                ", contact='" + getContact() + '\'' +
                '}';
    }
}
