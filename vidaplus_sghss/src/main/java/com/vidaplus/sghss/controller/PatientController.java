package com.vidaplus.sghss.controller;

import com.vidaplus.sghss.model.entities.Patient;
import com.vidaplus.sghss.repository.PatientRepository;
import com.vidaplus.sghss.utilities.LoginRequest;
import com.vidaplus.sghss.utilities.RegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;

    //To manage a bean of security and then encrypt password
    @Autowired
    private PasswordEncoder passwordEncoder;

    //                  Endpoints

    //Get all
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients(){
        List<Patient> patients = patientRepository.findAll();
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    // Get by id
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

    //                  Post methods - Begin

    //Sign up
    @PostMapping("/signup")
    public ResponseEntity<?> signUpPatient(@RequestBody RegistrationRequest registrationRequest) {
        if (patientRepository.existsByEmail(registrationRequest.getEmail())) {
            return new ResponseEntity<>("Este e-mail já existe!", HttpStatus.CONFLICT);
        }
        if (patientRepository.existsByCpf(registrationRequest.getCpf())) {
            return new ResponseEntity<>("Este CPF já existe!", HttpStatus.CONFLICT);
        }

        String cryptPassword = passwordEncoder.encode(registrationRequest.getPassword());

        Patient newPatient = new Patient(
                registrationRequest.getName(),
                registrationRequest.getDob(),
                registrationRequest.getEmail(),
                cryptPassword,
                registrationRequest.getCpf(),
                registrationRequest.getContact(),
                registrationRequest.getAddress()
                );

        Patient savedPatient = patientRepository.save(newPatient);
        return new ResponseEntity<>(savedPatient, HttpStatus.CREATED);
    }

    //Login
    @PostMapping("/login")
    public ResponseEntity<?> loginPatient(@RequestBody LoginRequest loginRequest){
        Optional<Patient> optionalPatient = patientRepository.findByEmail(loginRequest.getEmail());

        if (optionalPatient.isPresent()){
            Patient patient = optionalPatient.get();

            if (passwordEncoder.matches(loginRequest.getPassword(), patient.getPassword())){
                return new ResponseEntity<>(patient, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>("Senha Incorreta!", HttpStatus.CONFLICT);
            }
        }
        else {
            return new ResponseEntity<>("Usuário não encontrado!", HttpStatus.NOT_FOUND);
        }
    }
    //                  Post methods - End

    //                  Special methods - Begin

    //Access exam feature
    @GetMapping("/access-exam")
    public ResponseEntity<String> accessExam(){
        return new ResponseEntity<>("Função de Acessar Exames.", HttpStatus.OK);
    }

    //History online feature
    @GetMapping("history-online")
    public ResponseEntity<String> historyOnline(){
        return new ResponseEntity<>("Função de Acessar Histórico Online.", HttpStatus.OK);
    }

    //Schedule and cancel appointment feature
    @GetMapping("appointment")
    public ResponseEntity<String> scheduleCancelAppointment(){
        return new ResponseEntity<>("Função Agendar e Cancelar Consulta.", HttpStatus.OK);
    }
    //                  Special methods - End

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

    //Delete
    @DeleteMapping("/del/{id}")
    public ResponseEntity<?> deletePatient(@PathVariable Long id){
        if (patientRepository.existsById(id)){
            patientRepository.deleteById(id);
            return new ResponseEntity<>("Usuário deletado!", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
