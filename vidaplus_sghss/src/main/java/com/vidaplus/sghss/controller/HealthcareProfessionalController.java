package com.vidaplus.sghss.controller;

import com.vidaplus.sghss.model.entities.HealthcareProfessional;
import com.vidaplus.sghss.repository.HealthcareProfessionalRepository;
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
@RequestMapping("/hc-professional")
public class HealthcareProfessionalController {

    //                  Dependency injections - Begin

    //Repository object
    @Autowired
    HealthcareProfessionalRepository hcProfessionalRepository;

    //Encrypt password object
    @Autowired
    PasswordEncoder passwordEncoder;

    //Authentication objects
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    //                  Dependency injections - End

    //Variable
    private static final Logger logger = LoggerFactory.getLogger(HealthcareProfessional.class);

    //                  Endpoints

    //Get
    @GetMapping
    public ResponseEntity<?> findAllHcProfessional(){
        List<HealthcareProfessional> healthcareProfessionalList = hcProfessionalRepository.findAll();
        if (healthcareProfessionalList.isEmpty()){
            return new ResponseEntity<>("Não há nenhum usuário!", HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(healthcareProfessionalList, HttpStatus.OK);
        }
    }

    //Get by id
    @GetMapping("{id}")
    public ResponseEntity<HealthcareProfessional> getHcProfById(@PathVariable Long id){
        Optional<HealthcareProfessional> hcProOptional = hcProfessionalRepository.findById(id);
        if (hcProOptional.isPresent()){
            HealthcareProfessional hcProFound = hcProOptional.get();
            return new ResponseEntity<>(hcProFound, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //                  Post methods - Begin

    //Sign up
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody RegistrationRequest registrationRequest){
        if (hcProfessionalRepository.existsByEmail(registrationRequest.getEmail())){
            logger.warn("Usuário tentou se cadastrar sem sucesso com o e-mail: {}", registrationRequest.getEmail());
            return new ResponseEntity<>("Este e-mail já existe!", HttpStatus.CONFLICT);
        }
        if (hcProfessionalRepository.existsByCpf(registrationRequest.getCpf())){
            logger.warn("Usuário tentou se cadastrar sem sucesso com o CPF: {}", registrationRequest.getCpf());
            return new ResponseEntity<>("Este CPF já existe!", HttpStatus.CONFLICT);
        }
        if (hcProfessionalRepository.existsByCrm(registrationRequest.getCrm())){
            logger.warn("Usuário tentou se cadastrar sem sucesso com o CRM: {}", registrationRequest.getCrm());
            return new ResponseEntity<>("Este CRM já existe!", HttpStatus.CONFLICT);
        }

        String cryptPassword = passwordEncoder.encode(registrationRequest.getPassword());

        HealthcareProfessional newHcProfessional = new HealthcareProfessional(
                registrationRequest.getName(),
                registrationRequest.getDob(),
                registrationRequest.getEmail(),
                cryptPassword,
                registrationRequest.getCpf(),
                registrationRequest.getCrm(),
                registrationRequest.getContact());

        HealthcareProfessional savedHcProfessional = hcProfessionalRepository.save(newHcProfessional);

        logger.info("Paciente cadastrado com sucesso sob ID: {}", savedHcProfessional.getId());
        return new ResponseEntity<>(savedHcProfessional, HttpStatus.CREATED);
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

            String role = userDetails.getAuthorities().isEmpty() ? "GENERIC_USER" : userDetails.getAuthorities().stream().findFirst().get().getAuthority();

            logger.info("Login de Healthcare Professional realizado com sucesso pelo e-mail: {}", loginRequest.getEmail());
            return ResponseEntity.ok(Map.of("token", jwt));
        } catch (Exception e) {
            logger.warn("Falha ao realizar o login pelo e-mail: {}", loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("E-mail ou senha inválido");
        }
    }
    //                  Post methods - End

    //                  Special methods - Begin

    //Manage schedule feature
    @GetMapping("/manage-schedule")
    public ResponseEntity<String> manageSchedule(){
        return new ResponseEntity<>("Função de Gerenciar Agenda.", HttpStatus.OK);
    }

    //Update medical report feature
    @GetMapping("/medical-record")
    public ResponseEntity<String> medicalRecord(){
        return new ResponseEntity<>("Função de Atualizar Prontuário.", HttpStatus.OK);
    }

    //Issue prescription feature
    @GetMapping("/issue-prescription")
    public ResponseEntity<String> issuePrescription(){
        return new ResponseEntity<>("Função de Emitir Receita.", HttpStatus.OK);
    }

    //Follow patient history feature
    @GetMapping("/patient-history")
    public ResponseEntity<String> patientHistory(){
        return new ResponseEntity<>("Função de Acompanhar Histórico do Paciente.", HttpStatus.OK);
    }

    //                  Special methods - End

    //Update
    @PutMapping("/update/{id}")
    public ResponseEntity<HealthcareProfessional> updateHcProfessional(@PathVariable Long id,
                                                                       @RequestBody HealthcareProfessional hcProf){
        if (hcProfessionalRepository.existsById(id)){
            hcProf.setId(id);
            HealthcareProfessional hcProfUpdated = hcProfessionalRepository.save(hcProf);
            return new ResponseEntity<>(hcProfUpdated, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Delete
    @DeleteMapping("/del/{id}")
    public  ResponseEntity<?> deleteHcProf (@PathVariable Long id){
        if (hcProfessionalRepository.existsById(id)){
            hcProfessionalRepository.deleteById(id);
            return new ResponseEntity<>("Usuário Deletado!", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("ID não existe!", HttpStatus.NOT_FOUND);
        }
    }

}
