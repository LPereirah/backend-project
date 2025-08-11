package com.vidaplus.sghss.controller;

import com.vidaplus.sghss.model.entities.ADMHospital;
import com.vidaplus.sghss.repository.ADMHospitalRepository;
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
@RequestMapping("adm-hospital")
public class ADMHospitalController {

    //                  Dependency injections - Begin

    //Repository object
    @Autowired
    private ADMHospitalRepository admHospitalRepository;

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
    private static final Logger logger = LoggerFactory.getLogger(ADMHospitalController.class);

    //                  Endpoints

    //Get
    @GetMapping
    public ResponseEntity<List<ADMHospital>> getAllADMHospital(){
        List<ADMHospital> admHospitallist = admHospitalRepository.findAll();
        return new ResponseEntity<>(admHospitallist, HttpStatus.OK);
    }

    //Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<ADMHospital> getADMHospitalById(@PathVariable Long id){
        Optional<ADMHospital> optionalADMHospital = admHospitalRepository.findById(id);

        if (optionalADMHospital.isPresent()){
            ADMHospital admHospital = optionalADMHospital.get();
            return new ResponseEntity<>(admHospital, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //                  Post methods - Begin

    //Sign up
    @PostMapping("/signup")
    public ResponseEntity<?> signupAdmHospital(@RequestBody RegistrationRequest registrationRequest){
        if (admHospitalRepository.existsByEmail(registrationRequest.getEmail())){
            logger.warn("Usuário tentou se cadastrar sem sucesso com o e-mail: {}", registrationRequest.getEmail());
            return new ResponseEntity<>("Este e-mail já existe!", HttpStatus.CONFLICT);
        }
        if (admHospitalRepository.existsByCpf(registrationRequest.getCpf())){
            logger.warn("Usuário tentou se cadastrar sem sucesso com o CPF: {}", registrationRequest.getCpf());
            return new ResponseEntity<>("Este CPF já existe!", HttpStatus.CONFLICT);
        }

        String cryptPassword = passwordEncoder.encode(registrationRequest.getPassword());

        ADMHospital newAdmHospital = new ADMHospital(
                registrationRequest.getName(),
                registrationRequest.getDob(),
                registrationRequest.getEmail(),
                cryptPassword,
                registrationRequest.getCpf(),
                registrationRequest.getContact());

        ADMHospital savedAdmHospital = admHospitalRepository.save(newAdmHospital);

        logger.info("ADM - Hospital cadastrado com sucesso sob ID: {}", savedAdmHospital.getId());
        return new ResponseEntity<>(savedAdmHospital, HttpStatus.CREATED);
    }

    //Login
    @PostMapping("/login")
    public ResponseEntity<?> loginAdmHospital(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            final String jwt = jwtUtil.generateToken(userDetails);

            String role = userDetails.getAuthorities().isEmpty() ? "GENERIC_USER" : userDetails.getAuthorities().stream().findFirst().get().getAuthority();

            logger.info("Login de ADM - Hospital realizado com sucesso pelo e-mail: {}", loginRequest.getEmail());
            return ResponseEntity.ok(Map.of("token", jwt));
        } catch (Exception e) {
            logger.warn("Falha ao realizar o login pelo e-mail: {}", loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("E-mail ou senha inválido");
        }
    }
    //                  Post methods - End

    //                  Special Methods - Begin

    //Analyze beds feature
    @GetMapping("/beds")
    public ResponseEntity<?> analyzeBeds(){
        return new ResponseEntity<>("Função de Avaliar Leitos.", HttpStatus.OK);
    }

    //Generate financial report feature
    @GetMapping("/financial-report")
    public ResponseEntity<?> financialReport(){
        return new ResponseEntity<>("Função de Gerar Relatórios Financeiros.", HttpStatus.OK);
    }

    //Generate supply report feature
    @GetMapping("/supply-report")
    public ResponseEntity<?> supplyReport(){
        return new ResponseEntity<>("Função de Gerar Relatórios de Suprimentos.", HttpStatus.OK);
    }
    //                  Special Methods - End

    //Update
    @PutMapping("/update/{id}")
    public ResponseEntity<ADMHospital> updateAdmHospital(@PathVariable Long id, @RequestBody ADMHospital admHospital){
        if (admHospitalRepository.existsById(id)){
            admHospital.setId(id);
            ADMHospital admHospitalUpdated = admHospitalRepository.save(admHospital);
            return new ResponseEntity<>(admHospitalUpdated, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    //Delete
    @DeleteMapping("/del/{id}")
    public ResponseEntity<?> deleteAdmHospital(@PathVariable Long id){
        if (admHospitalRepository.existsById(id)){
            admHospitalRepository.deleteById(id);
            return new ResponseEntity<>("Deletado!", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Usuário Não Encontrado!", HttpStatus.NOT_FOUND);
        }
    }

}
