package com.vidaplus.sghss.controller;

import com.vidaplus.sghss.model.entities.HealthcareProfessional;
import com.vidaplus.sghss.repository.HealthcareProfessionalRepository;
import com.vidaplus.sghss.utilities.LoginRequest;
import com.vidaplus.sghss.utilities.RegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/hc-professional")
public class HealthcareProfessionalController {

    //Dependency Injection
    @Autowired
    HealthcareProfessionalRepository hcProfessionalRepository;

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

    //                  Post methods - Begin.
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody RegistrationRequest registrationRequest){
        if (hcProfessionalRepository.existsByEmail(registrationRequest.getEmail())){
            return new ResponseEntity<>("Este e-mail já existe!", HttpStatus.CONFLICT);
        }
        if (hcProfessionalRepository.existsByCpf(registrationRequest.getCpf())){
            return new ResponseEntity<>("Este CPF já existe!", HttpStatus.CONFLICT);
        }
        if (hcProfessionalRepository.existsByCrm(registrationRequest.getCrm())){
            return new ResponseEntity<>("Este CRM já existe!", HttpStatus.CONFLICT);
        }

        HealthcareProfessional newHcProfessional = new HealthcareProfessional(
                registrationRequest.getName(),
                registrationRequest.getDob(),
                registrationRequest.getEmail(),
                registrationRequest.getPassword(),
                registrationRequest.getCpf(),
                registrationRequest.getCrm(),
                registrationRequest.getContact());

        HealthcareProfessional saveHcProfessional = hcProfessionalRepository.save(newHcProfessional);
        return new ResponseEntity<>(saveHcProfessional, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        Optional<HealthcareProfessional> hcProfOptional = hcProfessionalRepository.findByEmail(loginRequest.getEmail());
        if (hcProfOptional.isPresent()){
            HealthcareProfessional hcProfessional = hcProfOptional.get();
            if (loginRequest.getPassword().equals(hcProfessional.getPassword())){
                return new ResponseEntity<>("Acesso Liberado!", HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>("Senha incorreta!", HttpStatus.CONFLICT);
            }
        }
        else {
            return new ResponseEntity<>("Usuário não existe!", HttpStatus.NOT_FOUND);
        }
    }
    //                  Post methods - End.

    //                  Special methods - Begin.
    //Manage schedule feature.
    @GetMapping("/manage-schedule")
    public ResponseEntity<String> manageSchedule(){
        return new ResponseEntity<>("Função de Gerenciar Agenda.", HttpStatus.OK);
    }

    //Update medical report feature.
    @GetMapping("/medical-record")
    public ResponseEntity<String> medicalRecord(){
        return new ResponseEntity<>("Função de Atualizar Prontuário.", HttpStatus.OK);
    }

    //Issue prescription feature.
    @GetMapping("/issue-prescription")
    public ResponseEntity<String> issuePrescription(){
        return new ResponseEntity<>("Função de Emitir Receita.", HttpStatus.OK);
    }

    //Follow patient history feature.
    @GetMapping("/patient-history")
    public ResponseEntity<String> patientHistory(){
        return new ResponseEntity<>("Função de Acompanhar Histórico do Paciente.", HttpStatus.OK);
    }

    //                  Special methods - End.

    //Update
    @PutMapping("/{id}")
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
    @DeleteMapping("{id}")
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
