package com.vidaplus.sghss.controller;

import com.vidaplus.sghss.model.entities.ADMHospital;
import com.vidaplus.sghss.repository.ADMHospitalRepository;
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
@RequestMapping("adm-hospital")
public class ADMHospitalController {

    //Dependency Injection
    @Autowired
    private ADMHospitalRepository admHospitalRepository;

    //To manage a bean of security and then encrypt password
    @Autowired
    private PasswordEncoder passwordEncoder;

    //                  Endpoints

    //Get
    @GetMapping
    public ResponseEntity<List<ADMHospital>> getAllADMHospital(){
        List<ADMHospital> admHospitallist = admHospitalRepository.findAll();
        return new ResponseEntity<>(admHospitallist, HttpStatus.OK);
    }

    //Get by id
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
    public ResponseEntity<?> signup(@RequestBody RegistrationRequest registrationRequest){
        if (admHospitalRepository.existsByEmail(registrationRequest.getEmail())){
            return new ResponseEntity<>("Este e-mail já existe!", HttpStatus.CONFLICT);
        }
        if (admHospitalRepository.existsByCpf(registrationRequest.getCpf())){
            return new ResponseEntity<>("Este CPF já existe!", HttpStatus.CONFLICT);
        }

        String cryptPassword = passwordEncoder.encode(registrationRequest.getPassword());

        ADMHospital newAdmHospital = new ADMHospital(
                registrationRequest.getName(),
                registrationRequest.getDob(),
                registrationRequest.getEmail(),
                cryptPassword,
                registrationRequest.getCpf(),
                registrationRequest.getContact()
        );

        ADMHospital savedAdmHospital = admHospitalRepository.save(newAdmHospital);
        return new ResponseEntity<>(savedAdmHospital, HttpStatus.CREATED);
    }

    //Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        Optional<ADMHospital> optionalADMHospital = admHospitalRepository.findByEmail(loginRequest.getEmail());
        if (optionalADMHospital.isPresent()){
            ADMHospital admHospitalLogin = optionalADMHospital.get();
            if (passwordEncoder.matches(loginRequest.getPassword(), admHospitalLogin.getPassword())){
                return new ResponseEntity<>(admHospitalLogin, HttpStatus.OK);
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

    //                  Special Methods - Begin

    //Analyze beds feature.
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
    @PutMapping("{id}")
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
    @DeleteMapping("{id}")
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
