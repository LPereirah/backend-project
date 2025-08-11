package com.vidaplus.sghss.service;

import com.vidaplus.sghss.filter.JwtRequestFilter;
import com.vidaplus.sghss.model.entities.HealthcareProfessional;
import com.vidaplus.sghss.repository.HealthcareProfessionalRepository;
import com.vidaplus.sghss.repository.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TelemedicineService {

    //Dependency injections
    @Autowired
    PatientRepository patientRepository;

    @Autowired
    HealthcareProfessionalRepository hcProfRepository;

    //Variable
    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);


    //Methods
    public String validateHcProfessional(Long id){
        Optional<HealthcareProfessional> hcProfOptional = hcProfRepository.findById(id);

        if (hcProfOptional.isPresent()){
            HealthcareProfessional hcProf = hcProfOptional.get();

            logger.info("Função 'Validar Profissional de Sáude' concluída com êxito.");
            return String.format("Acesso garantido ao profissional (CRM: %s)! " + "\n" +
                    "Iniciando chamada com um paciente...", hcProf.getCrm());

        }
        else{
            logger.warn("Função 'Validar Profissional de Sáude' não obteve êxito.");
            return String.format("Usuário não encontrado do ID: %s", id);
        }

    }

    public String validatePatient(Long id) {
        if (patientRepository.existsById(id)) {
            logger.info("Função 'Validar Paciente' concluída com êxito.");
            return String.format("Acesso garantido ao paciente (ID: %s)! " + "\n" +
                    "Iniciando chamada com um especialista...", id);
        } else {
            logger.warn("Função 'Validar Paciente' não obteve êxito.");
            return String.format("Usuário não encontrado do ID: %s", id);
        }
    }

}
