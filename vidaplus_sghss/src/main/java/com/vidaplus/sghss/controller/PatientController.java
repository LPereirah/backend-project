package com.vidaplus.sghss.controller;

import com.vidaplus.sghss.model.entities.Patient;
import com.vidaplus.sghss.repository.PatientRepository;
import com.vidaplus.sghss.utilities.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/vidaplus/pacientes")
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;

    // Get all patients.
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients(){
        List<Patient> patients = patientRepository.findAll();
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    // Get patients by id.
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id){
        Optional<Patient> patient = patientRepository.findById(id);
        if (patient.isPresent()){
            return new ResponseEntity<>(patient.get(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Post.
    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient){
        if (!patientRepository.existsByCpf(patient.getCpf())){
            Patient patienSaved = patientRepository.save(patient);
            return new ResponseEntity<>(patienSaved, HttpStatus.CREATED);
        }
        else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    //Special.

    //Update.
    @PutMapping("/update/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @RequestBody Patient savedPatient){
        Optional<Patient> existingPatient = patientRepository.findById(id);
        if (existingPatient.isPresent()){
            savedPatient.setId(id);
            Patient updatedPatient = patientRepository.save(savedPatient);
            return new ResponseEntity<>(savedPatient, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Delete object by id
    @DeleteMapping("/del/{id}")
    public ResponseEntity<HttpStatus> deletePatient(@PathVariable Long id){
        // First, verify if the id exists
        if (patientRepository.existsById(id)){
            patientRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Verify if already have the object saved.
    @PostMapping("/login")
    public ResponseEntity<?> loginPatient(@RequestBody LoginRequest loginRequest){
        Optional<Patient> optionalPatient = patientRepository.findByEmail(loginRequest.getEmail());

        if (optionalPatient.isPresent()){
            Patient patient = optionalPatient.get();

            if (patient.getPassword().equals(loginRequest.getPassword())){
                return new ResponseEntity<>(patient, HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
