package com.vidaplus.sghss.repository;

import com.vidaplus.sghss.model.entities.HealthcareProfessional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HealthcareProfessionalRepository  extends JpaRepository<HealthcareProfessional, Long> {

    //Custom methods.
    Optional<HealthcareProfessional> findByEmail(String email);
    boolean existsByCrm(String crm);
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);

}
