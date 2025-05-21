package com.vidaplus.sghss.controller;

import com.vidaplus.sghss.model.entities.ADMUsers;
import com.vidaplus.sghss.repository.ADMUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/vidaplus/adm-users")
public class ADMUsersController {

    @Autowired
    private ADMUsersRepository admUsersRepository;

    @GetMapping
    public ResponseEntity<List<ADMUsers>> getAllADMUsers(){
        List<ADMUsers> admUsersList = admUsersRepository.findAll();
        return new ResponseEntity<>(admUsersList, HttpStatus.OK);

    }

    // Create an ADMUser. Use the RegistrationRequest class to verify the information, then create or not.
    @PostMapping("/signup")
    public ResponseEntity<?> signUpADMUsers(@RequestBody RegistrationRequest registrationRequest){
        if (admUsersRepository.existsByEmail(registrationRequest.getEmail())){
            return new ResponseEntity<>("Email already exists!", HttpStatus.CONFLICT);
        }
        if (admUsersRepository.existsByCpf(registrationRequest.getCpf())){
            return new ResponseEntity<>("CPF already exists!", HttpStatus.CONFLICT);
        }

        ADMUsers newAdmUser = new ADMUsers(
                registrationRequest.getName(),
                registrationRequest.getDob(),
                registrationRequest.getEmail(),
                registrationRequest.getPassword(),
                registrationRequest.getCpf(),
                registrationRequest.getContact()
        );

        ADMUsers admUserSaved = admUsersRepository.save(newAdmUser);
        return new ResponseEntity<>(admUserSaved, HttpStatus.CREATED);
    }

    //Login feature. Verify the email. If exists, then compare the passwords.
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        Optional<ADMUsers> optionalADMUsers = admUsersRepository.findByEmail(loginRequest.getEmail());
        // Verify email.
        if (optionalADMUsers.isPresent()){
            // Verify password.
            ADMUsers admUserLogin = optionalADMUsers.get();
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
    public ResponseEntity<ADMUsers> updateADMUser(@PathVariable Long id, @RequestBody ADMUsers admUsers){
        if (admUsersRepository.existsById(id)){
            admUsers.setId(id);
            ADMUsers admUpdated = admUsersRepository.save(admUsers);
            return new ResponseEntity<>(admUpdated, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete ADMUser. Use existsById to ensure if exists or not, then delete.
    @DeleteMapping("/{id}")
    public ResponseEntity<ADMUsers> deleteById(@PathVariable Long id){
        if (admUsersRepository.existsById(id)){
            admUsersRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

}
