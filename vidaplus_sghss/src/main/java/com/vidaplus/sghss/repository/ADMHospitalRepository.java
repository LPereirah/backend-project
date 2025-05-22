package com.vidaplus.sghss.repository;

import com.vidaplus.sghss.model.entities.ADMHospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ADMHospitalRepository extends JpaRepository<ADMHospital, Long> {
    Optional<ADMHospital> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
}
