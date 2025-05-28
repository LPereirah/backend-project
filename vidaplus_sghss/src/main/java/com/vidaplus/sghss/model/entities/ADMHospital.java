package com.vidaplus.sghss.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "adm_hospital")
public class ADMHospital extends User{

    //Constructors.
    public ADMHospital() {
        super();
    }

    public ADMHospital(String name, String dob, String email, String password, String cpf, String contact) {
        super(name, dob, email, password, cpf, contact);
    }

}
