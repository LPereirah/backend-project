package com.vidaplus.sghss.service;

import com.vidaplus.sghss.filter.JwtRequestFilter;
import com.vidaplus.sghss.model.entities.Patient;
import com.vidaplus.sghss.repository.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MedicalRecordService {

    //Dependency injections
    @Autowired
    PatientRepository patientRepository;

    //Variable
    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    // Methods
    public String patientHistory(Long id){
        Optional<Patient> patientOptional = patientRepository.findById(id);
        
        if (patientOptional.isPresent()){
            logger.info("Função 'Histórico do Paciente' concluída com êxito.");
            return "Histórico do Paciente: " + id + ".";
        }
        else {
            logger.warn("Função 'Histórico do Paciente' falhou.");
            return "Paciente não encontrado pelo id: " + id;
        }
    }

    public String patientDiagnostics(Long id){
        Optional<Patient> patientOptional = patientRepository.findById(id);

        if (patientOptional.isPresent()){
            logger.info("Função 'Diagnósticos do Paciente' concluída com êxito.");
            return "Diagnósticos do paciente: " + id + ".";
        }
        else {
            logger.warn("Função 'Diagnósticos do Paciente' falhou.");
            return "Paciente não encontrado pelo id: " + id;

        }
    }

    public String generateReports() {
        return "Função de gerar relatórios";
    }
}
