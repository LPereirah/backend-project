package com.vidaplus.sghss.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "adm_users")
public class ADMUser extends User{

    //Constructors.
    public ADMUser() {
    }

    public ADMUser(String name, String dob, String email, String password, String cpf, String contact) {
        super(name, dob, email, password, cpf, contact);
    }

}
