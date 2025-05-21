package com.vidaplus.sghss.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "adm_users")
public class ADMUsers extends User{

    public ADMUsers() {
    }

    public ADMUsers(String name, String dob, String email, String password, String cpf, String contact) {
        super(name, dob, email, password, cpf, contact);
    }

    @Override
    public String toString() {
        return "Name: " + getName() + "/" +
                "CPF: " + getCpf() + "/" +
                "Date of Birth" + getDob() + "/" +
                "Email: " + getEmail() + "/" +
                "Password: " + getPassword();
    }
}
