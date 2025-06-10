package com.vidaplus.sghss.controller;

import com.vidaplus.sghss.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medical-record")
public class MedicalRecordController {

    //Dependency Injection.
    @Autowired
    MedicalRecordService mrService;

    //Patient history feature.
    @GetMapping("/patient-history/{patientId}")
    public ResponseEntity<String> patientHistory(@PathVariable Long patientId){
        String history = mrService.patientHistory(patientId);

        if (history.startsWith("Paciente não")){
            return new ResponseEntity<>(history, HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(history, HttpStatus.OK);
        }
    }

    //Patient diagnostics feature.
    @GetMapping("/patient-diagnostics/{patientId}")
    public ResponseEntity<String> patientDiagnostics(@PathVariable Long patientId){
        String diagnostics = mrService.patientDiagnostics(patientId);

        if (diagnostics.startsWith("Paciente não")){
            return new ResponseEntity<>(diagnostics, HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(diagnostics, HttpStatus.OK);
        }
    }

    //To issue report feature.
    @GetMapping("/issue-report")
    public ResponseEntity<String> issueReport(){

        return new ResponseEntity<>(mrService.generateReports(), HttpStatus.OK);
    }

}
