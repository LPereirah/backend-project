package com.vidaplus.sghss.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "healthcare_professional")
public class HealthcareProfessional extends User{

    //Variables.
    private String crm;

   //Constructors - Begin.
    public HealthcareProfessional() {
    }

    public HealthcareProfessional(String name,
                                  String dob,
                                  String email,
                                  String password,
                                  String cpf,
                                  String crm,
                                  String contact) {
        super(name, dob, email, password, cpf, contact);

        this.crm = crm;
    }
   //Constructors - End.

    // Getter and setter.
    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

}
