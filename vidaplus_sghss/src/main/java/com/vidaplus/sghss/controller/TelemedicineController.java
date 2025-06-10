package com.vidaplus.sghss.controller;

import com.vidaplus.sghss.service.TelemedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/telemedicine")
public class TelemedicineController {

    //Dependency Injection
    @Autowired
    TelemedicineService telemedicineService;

    //                  Endpoints

    //Verify if is a professional, then make the call
    @GetMapping("/video-call/hc-professional/{id}")
    public ResponseEntity<String> videoCallHcProfessional(@PathVariable Long id){
        String validate = telemedicineService.validateHcProfessional(id);

        if (validate.startsWith("Usuário não")){
            return new ResponseEntity<>(validate, HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(validate, HttpStatus.OK);
        }
    }

    //Verify if is a patient, then make the call
    @GetMapping("/video-call/patient/{id}")
    public ResponseEntity<String> videoCallPatient(@PathVariable Long id){
        String validate = telemedicineService.validatePatient(id);

        if (validate.startsWith("Usuário não")){
            return new ResponseEntity<>(validate, HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(validate, HttpStatus.OK);
        }
    }

    // Register Medical Record feature
    @GetMapping("/register-medical-record")
    public ResponseEntity<String> registerMedicalRecord(){
        return new ResponseEntity<>("Função de registrar prontuário.", HttpStatus.OK);
    }

    // Register Prescriptions feature
    @GetMapping("/register-prescriptions")
    public ResponseEntity<String> registerPrescriptions(){
        return new ResponseEntity<>("Função de registrar prescrições.", HttpStatus.OK);
    }



}
