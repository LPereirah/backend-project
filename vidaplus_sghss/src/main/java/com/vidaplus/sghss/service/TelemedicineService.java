package com.vidaplus.sghss.service;

import com.vidaplus.sghss.model.entities.HealthcareProfessional;
import com.vidaplus.sghss.repository.HealthcareProfessionalRepository;
import com.vidaplus.sghss.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TelemedicineService {

    //Dependency Injection.
    @Autowired
    PatientRepository patientRepository;

    @Autowired
    HealthcareProfessionalRepository hcProfRepository;

    //Methods
    public String validateHcProfessional(Long id){
        Optional<HealthcareProfessional> hcProfOptional = hcProfRepository.findById(id);

        if (hcProfOptional.isPresent()){
            HealthcareProfessional hcProf = hcProfOptional.get();
            return String.format("Acesso garantido ao profissional (CRM: %s)! " + "\n" +
                    "Iniciando chamada com um paciente...", hcProf.getCrm());

        }
        else{
            return String.format("Usuário não encontrado do ID: %s", id);
        }

    }

    public String validatePatient(Long id) {
        if (patientRepository.existsById(id)) {
            return String.format("Acesso garantido ao paciente (ID: %s)! " + "\n" +
                    "Iniciando chamada com um especialista...", id);
        } else {
            return String.format("Usuário não encontrado do ID: %s", id);
        }
    }

}
