package com.montbank.ms.user.services;

import com.montbank.ms.user.dtos.UserInformationDTO;
import com.montbank.ms.user.dtos.UserInformationUpdateDTO;
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
    @Autowired
    MessageSenderService messageSenderService;

    @Transactional
    public UserModel save(@Valid UserRegisterDTO userRegisterDTO) {
        var userModel = new UserModel();
        BeanUtils.copyProperties(userRegisterDTO, userModel);
        userModel.setBalance(new BigDecimal(50));
        userModel.setPassword(encoder.encode(userModel.getPassword()));
        UserModel savedUser = userRepository.save(userModel);
        messageSenderService.sendEmail(savedUser.getEmail(), savedUser.getName());
        return savedUser;
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

    public boolean userExistsByCPF(String CPF){
        return userRepository.existsByCPF(CPF);
    }

    public boolean userExistsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public void updateBalance(UUID senderID, BigDecimal amount, String receiverEmail){
        UserModel sender = userRepository.findById(senderID).orElseThrow(()
                -> new EntityNotFoundException("Sender não encontrado"));
        UserModel receiver = userRepository.findByEmail(receiverEmail).orElseThrow(()
                -> new EntityNotFoundException("Receiver não encontrado"));
        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));
        userRepository.save(sender);
        userRepository.save(receiver);
    }
    public UserInformationDTO getUserInformationById(UUID userId){
        var user = userRepository.findById(userId).orElseThrow(()
                -> new EntityNotFoundException("Usuário não encontrado"));
        return  new UserInformationDTO(user.getName(), user.getCPF(), user.getEmail(), user.getBalance());
    }
    @Transactional
    public void updateUserInformation(UserInformationUpdateDTO userInformationUpdateDTO,UUID userId){
        var user = userRepository.findById(userId).orElseThrow(()
                -> new EntityNotFoundException("Usuário não encontrado"));
        BeanUtils.copyProperties(userInformationUpdateDTO,user);
        userRepository.save(user);
    }
    @Transactional
    public void deleteUser(UUID userId){
        userRepository.deleteById(userId);
    }
}
