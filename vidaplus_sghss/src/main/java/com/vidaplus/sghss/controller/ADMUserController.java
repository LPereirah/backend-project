package com.vidaplus.sghss.controller;

import com.vidaplus.sghss.model.entities.ADMUser;
import com.vidaplus.sghss.repository.ADMUserRepository;
import com.vidaplus.sghss.utilities.LoginRequest;
import com.vidaplus.sghss.utilities.RegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/vidaplus/adm-users")
public class ADMUserController {

    @Autowired
    private ADMUserRepository admUserRepository;

    // Get methods
    @GetMapping
    public ResponseEntity<List<ADMUser>> getAllADMUsers(){
        List<ADMUser> admUserList = admUserRepository.findAll();
        return new ResponseEntity<>(admUserList, HttpStatus.OK);

    }

    // Create an ADMUser. Use the RegistrationRequest class to verify the information, then create or not.
    @PostMapping("/signup")
    public ResponseEntity<?> signUpADMUsers(@RequestBody RegistrationRequest registrationRequest){
        if (admUserRepository.existsByEmail(registrationRequest.getEmail())){
            return new ResponseEntity<>("Este e-mail já existe!", HttpStatus.CONFLICT);
        }
        if (admUserRepository.existsByCpf(registrationRequest.getCpf())){
            return new ResponseEntity<>("Este CPF já existe!", HttpStatus.CONFLICT);
        }

        ADMUser newAdmUser = new ADMUser(
                registrationRequest.getName(),
                registrationRequest.getDob(),
                registrationRequest.getEmail(),
                registrationRequest.getPassword(),
                registrationRequest.getCpf(),
                registrationRequest.getContact()
        );

        ADMUser admUserSaved = admUserRepository.save(newAdmUser);
        return new ResponseEntity<>(admUserSaved, HttpStatus.CREATED);
    }

    //Login feature. Verify the email. If exists, then compare the passwords.
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        Optional<ADMUser> optionalADMUsers = admUserRepository.findByEmail(loginRequest.getEmail());
        // Verify email.
        if (optionalADMUsers.isPresent()){
            // Verify password.
            ADMUser admUserLogin = optionalADMUsers.get();
            if (admUserLogin.getPassword().equals(loginRequest.getPassword())){
                return new ResponseEntity<>(admUserLogin, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    //Generate report feature. Basic implementation.
    @GetMapping("/report")
    public ResponseEntity<?> toGenerateReport(){
        return new ResponseEntity<>("Função de Gerar Relatório.", HttpStatus.OK);
    }

    //Manage user feature. Basic implementation.
    @GetMapping("/manage-user")
    public ResponseEntity<?> manageUer(){
        return new ResponseEntity<>("Função de Gerenciar Usuário.", HttpStatus.OK);
    }


    // Update method. Verify if ID already exists, before trying to update.
    @PutMapping("/{id}")
    public ResponseEntity<ADMUser> updateADMUser(@PathVariable Long id, @RequestBody ADMUser admUser){
        if (admUserRepository.existsById(id)){
            admUser.setId(id);
            ADMUser admUpdated = admUserRepository.save(admUser);
            return new ResponseEntity<>(admUpdated, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete ADMUser. Use existsById to ensure if exists or not, then delete.
    @DeleteMapping("/{id}")
    public ResponseEntity<ADMUser> deleteById(@PathVariable Long id){
        if (admUserRepository.existsById(id)){
            admUserRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

}
