package com.montbank.ms.user.services;

import com.montbank.ms.user.dtos.UserRegisterDTO;
import com.montbank.ms.user.models.UserModel;
import com.montbank.ms.user.repositories.UserRepository;
import com.montbank.ms.user.security.services.TokenService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    public UserModel save(@Valid UserRegisterDTO userRegisterDTO) {
        var userModel = new UserModel();
        BeanUtils.copyProperties(userRegisterDTO, userModel);
        userModel.setBalance(new BigDecimal(50));
        userModel.setPassword(encoder.encode(userModel.getPassword()));
        return userRepository.save(userModel);
    }

    public String loginUser(String email, String password) {
        UserModel user = userRepository.findByEmail(email).orElseThrow(()
                -> new EntityNotFoundException("Credenciais Incorretas"));

        if (!encoder.matches(password, user.getPassword())) {
            throw  new RuntimeException("Credenciais Incorretas");
        }
        return  tokenService.generateToken(user);
    }
    public boolean userExists(UUID userId){
        return userRepository.existsById(userId);
    }

    @Transactional
    public void updateBalance(UUID senderID, BigDecimal amount, UUID receiverID){
        UserModel sender = userRepository.findById(senderID).orElseThrow(()
                -> new EntityNotFoundException("Sender não encontrado"));
        UserModel receiver = userRepository.findById(receiverID).orElseThrow(()
                -> new EntityNotFoundException("Receiver não encontrado"));
        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));
        userRepository.save(sender);
        userRepository.save(receiver);
    }
}
