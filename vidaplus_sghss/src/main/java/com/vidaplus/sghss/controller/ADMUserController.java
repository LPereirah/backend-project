package com.vidaplus.sghss.controller;

import com.vidaplus.sghss.model.entities.ADMUser;
import com.vidaplus.sghss.repository.ADMUserRepository;
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
@RequestMapping("/adm-user")
public class ADMUserController {

    //                  Dependency injections - Begin

    //Repository object
    @Autowired
    private ADMUserRepository admUserRepository;

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
    private static final Logger logger = LoggerFactory.getLogger(ADMUserController.class);

    //                  Endpoints

    //Get
    @GetMapping
    public ResponseEntity<List<ADMUser>> getAllADMUsers(){
        List<ADMUser> admUserList = admUserRepository.findAll();
        return new ResponseEntity<>(admUserList, HttpStatus.OK);

    }

    //Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<ADMUser> getADMUserById(@PathVariable Long id){
        Optional<ADMUser> admUserOptional = admUserRepository.findById(id);

        if (admUserOptional.isPresent()){
            ADMUser admUser = admUserOptional.get();
            return new ResponseEntity<>(admUser, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //                  Post methods - Begin

    // Sign up
    @PostMapping("/signup")
    public ResponseEntity<?> signUpAdmUsers(@RequestBody RegistrationRequest registrationRequest){
        if (admUserRepository.existsByEmail(registrationRequest.getEmail())){
            logger.warn("Usuário tentou se cadastrar sem sucesso com o e-mail: {}", registrationRequest.getEmail());
            return new ResponseEntity<>("Este e-mail já existe!", HttpStatus.CONFLICT);
        }
        if (admUserRepository.existsByCpf(registrationRequest.getCpf())){
            logger.warn("Usuário tentou se cadastrar sem sucesso com o CPF: {}", registrationRequest.getCpf());
            return new ResponseEntity<>("Este CPF já existe!", HttpStatus.CONFLICT);
        }

        String cryptPassword = passwordEncoder.encode(registrationRequest.getPassword());

        ADMUser newAdmUser = new ADMUser(
                registrationRequest.getName(),
                registrationRequest.getDob(),
                registrationRequest.getEmail(),
                cryptPassword,
                registrationRequest.getCpf(),
                registrationRequest.getContact());

        ADMUser admUserSaved = admUserRepository.save(newAdmUser);

        logger.info("ADM - Usuário cadastrado com sucesso sob ID: {}", admUserSaved.getId());
        return new ResponseEntity<>(admUserSaved, HttpStatus.CREATED);
    }

    //Login
    @PostMapping("/login")
    public ResponseEntity<?> loginAdmUsers(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            final String jwt = jwtUtil.generateToken(userDetails);

            String role = userDetails.getAuthorities().isEmpty() ? "GENERIC_USER" : userDetails.getAuthorities().stream().findFirst().get().getAuthority();

            logger.info("Login de ADM - User realizado com sucesso pelo e-mail: {}", loginRequest.getEmail());
            return ResponseEntity.ok(Map.of("token", jwt)); // Still return role for client info
        } catch (Exception e) {
            logger.warn("Falha ao realizar o login pelo e-mail: {}", loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("E-mail ou senha inválido");
        }
    }
    //                  Post methods - End


    //                  Special methods - begin

    //Generate report feature
    @GetMapping("/report")
    public ResponseEntity<?> toGenerateReport(){
        return new ResponseEntity<>("Função de Gerar Relatório.", HttpStatus.OK);
    }

    //Manage user feature
    @GetMapping("/manage-user")
    public ResponseEntity<?> manageUer(){
        return new ResponseEntity<>("Função de Gerenciar Usuário.", HttpStatus.OK);
    }

    //                  Special methods - End

    //Update
    @PutMapping("/update/{id}")
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
    @DeleteMapping("/del/{id}")
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
