package com.vidaplus.sghss.controller;

import com.vidaplus.sghss.model.entities.Patient;
import com.vidaplus.sghss.repository.PatientRepository;
import com.vidaplus.sghss.generic.LoginRequest;
import com.vidaplus.sghss.generic.RegistrationRequest;
import com.vidaplus.sghss.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/patients")
public class PatientController {

    //                  Dependency injections - Begin

    //Repository object
    @Autowired
    private PatientRepository patientRepository;

    //Encrypt password object
    @Autowired
    private PasswordEncoder passwordEncoder;

    //Authentication objects
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;
    //                  Dependency injections - End

    //Variable
    private static final Logger logger = LoggerFactory.getLogger(PatientController.class);


    //                  Endpoints

    //Get
    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients(){
        List<Patient> patients = patientRepository.findAll();
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    // Get by ID
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
            logger.warn("Paciente tentou se cadastrar sem sucesso com o e-mail: {}", registrationRequest.getEmail());
            return new ResponseEntity<>("Este e-mail já existe!", HttpStatus.CONFLICT);
        }
        if (patientRepository.existsByCpf(registrationRequest.getCpf())) {
            logger.warn("Paciente tentou se cadastrar sem sucesso com o CPF: {}", registrationRequest.getCpf());
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
                registrationRequest.getAddress());

        Patient savedPatient = patientRepository.save(newPatient);

        logger.info("Paciente cadastrado com sucesso sob ID: {}", savedPatient.getId());
        return new ResponseEntity<>(savedPatient, HttpStatus.CREATED);
    }

    //Login
    @PostMapping("/login")
    public ResponseEntity<?> loginPatient(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            final String jwt = jwtUtil.generateToken(userDetails);

            logger.info("Login de paciente realizado com sucesso pelo e-mail: {}", loginRequest.getEmail());
            return ResponseEntity.ok(Map.of("token", jwt)); // Still return role for client info
        } catch (Exception e) {
            logger.warn("Falha ao realizar o login pelo e-mail: {}", loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("E-mail ou senha inválido");
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
    @GetMapping("/history-online")
    public ResponseEntity<String> historyOnline(){
        return new ResponseEntity<>("Função de Acessar Histórico Online.", HttpStatus.OK);
    }

    //Schedule and cancel appointment feature
    @GetMapping("/appointment")
    public ResponseEntity<String> scheduleCancelAppointment(){
        return new ResponseEntity<>("Função Agendar e Cancelar Consulta.", HttpStatus.OK);
    }
    //                  Special methods - End

    //Update
    @PutMapping("/update/{id}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long id, @RequestBody Patient savedPatient){
        if (patientRepository.existsById(id)){
            savedPatient.setId(id);
            Patient updatedPatient = patientRepository.save(savedPatient);
            return new ResponseEntity<>(updatedPatient, HttpStatus.OK);
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
