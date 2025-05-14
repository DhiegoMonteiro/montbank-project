package com.montbank.ms.user.services;

import com.montbank.ms.user.models.UserModel;
import com.montbank.ms.user.repositories.UserRepository;
import com.montbank.ms.user.security.services.TokenService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    BCryptPasswordEncoder encoder;
    @Autowired
    TokenService tokenService;

    @Transactional
    public UserModel save(UserModel userModel) {
        userModel.setBalance(new BigDecimal(50));
        userModel.setPassword(encoder.encode(userModel.getPassword()));
        return userRepository.save(userModel);
    }

    public String loginUser(String email, String password) {
        UserModel user = userRepository.findByEmail(email).orElseThrow(()
                -> new EntityNotFoundException("Usuário não encontrado"));

        if (!encoder.matches(password, user.getPassword())) {
            throw  new RuntimeException("Credenciais Incorretas");
        }
        return  tokenService.generateToken(user);
    }
}
