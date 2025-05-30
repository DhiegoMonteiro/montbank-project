package com.montbank.ms.user.repositories;

import com.montbank.ms.user.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel, UUID> {

    Optional<UserModel> findByEmail(String email);
    boolean existsByCPF(String CPF);
    boolean existsByEmail(String email);

}

