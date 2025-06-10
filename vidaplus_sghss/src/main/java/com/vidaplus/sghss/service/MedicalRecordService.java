package com.vidaplus.sghss.service;

import com.vidaplus.sghss.model.entities.HealthcareProfessional;
import com.vidaplus.sghss.model.entities.Patient;
import com.vidaplus.sghss.repository.HealthcareProfessionalRepository;
import com.vidaplus.sghss.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MedicalRecordService {

    //Dependency Injection.
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    HealthcareProfessionalRepository hcProfRepository;

    // Methods
    public String patientHistory(Long id){
        Optional<Patient> patientOptional = patientRepository.findById(id);

        if (patientOptional.isPresent()){
            return "Histórico do Paciente: " + id + ".";
        }
        else {
            return "Paciente não encontrado pelo id: " + id;
        }
    }

    public String patientDiagnostics(Long id){
        Optional<Patient> patientOptional = patientRepository.findById(id);

        if (patientOptional.isPresent()){
            return "Diagnósticos do paciente: " + id + ".";
        }
        else {
            return "Paciente não encontrado pelo id: " + id;

        }
    }

    public String generateReports() {
        return "Função de gerar relatórios";
    }
}
