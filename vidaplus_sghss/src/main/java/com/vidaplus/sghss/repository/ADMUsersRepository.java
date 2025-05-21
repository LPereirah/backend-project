package com.vidaplus.sghss.repository;

import com.vidaplus.sghss.model.entities.ADMUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ADMUsersRepository extends JpaRepository<ADMUsers, Long> {
    Optional<ADMUsers> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);



}
