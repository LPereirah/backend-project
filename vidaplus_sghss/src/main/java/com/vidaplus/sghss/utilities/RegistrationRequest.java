package com.vidaplus.sghss.utilities;

public class RegistrationRequest {
    private String name;
    private String dob;
    private String email;
    private String password;
    private String cpf;
    private String crm;
    private String contact;

    // Default constructor
    public RegistrationRequest() {
    }

    //Constructor for Healthcare Professional validation
    public RegistrationRequest(String name,
                               String dob,
                               String email,
                               String password,
                               String cpf,
                               String crm,
                               String contact) {

        this.name = name;
        this.dob = dob;
        this.email = email;
        this.password = password;
        this.cpf = cpf;
        this.crm = crm;
        this.contact = contact;
    }

    // Generic constructor validation
    public RegistrationRequest(String name, String dob, String email, String password, String cpf, String contact) {
        this.name = name;
        this.dob = dob;
        this.email = email;
        this.password = password;
        this.cpf = cpf;
        this.contact = contact;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
