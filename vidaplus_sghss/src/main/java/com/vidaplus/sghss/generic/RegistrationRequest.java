package com.vidaplus.sghss.generic;

public class RegistrationRequest {
    private String name;
    private String dob;
    private String email;
    private String password;
    private String cpf;
    private String crm;
    private String contact;
    private String address;
    private String role;

    // Default constructor
    public RegistrationRequest() {
    }

    //Constructor for Healthcare Professional validation
    public RegistrationRequest(String name, String dob, String email, String password,
                               String cpf, String crm, String contact, String role) {

        this.name = name;
        this.dob = dob;
        this.email = email;
        this.password = password;
        this.cpf = cpf;
        this.crm = crm;
        this.contact = contact;
        this.role = role;
    }

    //Constructor for Patient
    public RegistrationRequest(String name, String dob, String email, String password, String cpf,
                               String address, String contact, String role, boolean isPatient) {

        this.name = name;
        this.dob = dob;
        this.email = email;
        this.password = password;
        this.cpf = cpf;
        this.contact = contact;
        this.address = address;
        this.role = role;
    }

    //Generic constructor validation
    public RegistrationRequest(String name, String dob, String email, String password,
                               String cpf, String contact, String role) {
        this.name = name;
        this.dob = dob;
        this.email = email;
        this.password = password;
        this.cpf = cpf;
        this.contact = contact;
        this.role = role;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
