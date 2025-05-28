package com.vidaplus.sghss.repository;

import com.vidaplus.sghss.model.entities.ADMUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ADMUserRepository extends JpaRepository<ADMUser, Long> {

    //Custom methods.
    Optional<ADMUser> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);

}
