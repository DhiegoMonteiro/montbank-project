package com.montbank.ms.user.services;

import com.montbank.ms.user.models.UserModel;
import com.montbank.ms.user.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Transactional
    public UserModel save(UserModel userModel) {
        var balance = new BigDecimal(50);
        userModel.setBalance(balance);
        return userRepository.save(userModel);
    }

    @Transactional
    public void updateBalance(UUID senderId, UUID receiverId, BigDecimal amount) {
        UserModel sender = userRepository.findById(senderId)
                .orElseThrow(() -> new EntityNotFoundException("Sender not found"));
        UserModel receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new EntityNotFoundException("Receiver not found"));
        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));
        userRepository.save(sender);
        userRepository.save(receiver);
    }
}
