package com.vidaplus.sghss;

import com.vidaplus.sghss.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	@Autowired
	private PatientRepository patientRepository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}


//	@Override
//	public void run(String... args) throws Exception {
//
//// Create and save on db
////		Patient patient1 = new Patient("Sora", "10/01/1987", "sora@email.com",
////				"1122", "1235678910", "(11) 912345678", "Islands");
////
////		System.out.println("Patient created!");
////
////		patientRepository.save(patient1);
//
////		System.out.println("Patients: ");
////		for (Patient patient : patientRepository.findAll()){
////			System.out.println(patient.toString());
////		}
//
//	}
}
