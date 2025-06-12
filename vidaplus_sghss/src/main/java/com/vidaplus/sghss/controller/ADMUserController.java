package com.vidaplus.sghss.controller;

import com.vidaplus.sghss.model.entities.ADMUser;
import com.vidaplus.sghss.repository.ADMUserRepository;
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
@RequestMapping("/adm-users")
public class ADMUserController {

    //Dependency injection
    @Autowired
    private ADMUserRepository admUserRepository;

    //To manage a bean of security and then encrypt password
    @Autowired
    private PasswordEncoder passwordEncoder;

    //                  Endpoints

    // Get
    @GetMapping
    public ResponseEntity<List<ADMUser>> getAllADMUsers(){
        List<ADMUser> admUserList = admUserRepository.findAll();
        return new ResponseEntity<>(admUserList, HttpStatus.OK);

    }

    //                  Post methods - Begin

    // Sign up
    @PostMapping("/signup")
    public ResponseEntity<?> signUpADMUsers(@RequestBody RegistrationRequest registrationRequest){
        if (admUserRepository.existsByEmail(registrationRequest.getEmail())){
            return new ResponseEntity<>("Este e-mail já existe!", HttpStatus.CONFLICT);
        }
        if (admUserRepository.existsByCpf(registrationRequest.getCpf())){
            return new ResponseEntity<>("Este CPF já existe!", HttpStatus.CONFLICT);
        }

        String cryptPassword = passwordEncoder.encode(registrationRequest.getPassword());

        ADMUser newAdmUser = new ADMUser(
                registrationRequest.getName(),
                registrationRequest.getDob(),
                registrationRequest.getEmail(),
                cryptPassword,
                registrationRequest.getCpf(),
                registrationRequest.getContact()
        );

        ADMUser admUserSaved = admUserRepository.save(newAdmUser);
        return new ResponseEntity<>(admUserSaved, HttpStatus.CREATED);
    }

    //Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        Optional<ADMUser> optionalADMUsers = admUserRepository.findByEmail(loginRequest.getEmail());
        // Verify email.
        if (optionalADMUsers.isPresent()){
            // Verify password.
            ADMUser admUserLogin = optionalADMUsers.get();
            if (passwordEncoder.matches(loginRequest.getPassword(), admUserLogin.getPassword())){
                return new ResponseEntity<>(admUserLogin, HttpStatus.OK);
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


    //                  Special methods - begin
    //Generate report feature
    @GetMapping("/report")
    public ResponseEntity<?> toGenerateReport(){
        return new ResponseEntity<>("Função de Gerar Relatório.", HttpStatus.OK);
    }

    //Manage user feature. Basic implementation.
    @GetMapping("/manage-user")
    public ResponseEntity<?> manageUer(){
        return new ResponseEntity<>("Função de Gerenciar Usuário.", HttpStatus.OK);
    }

    //                  Special methods - End

    //Update
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

    // Delete
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
