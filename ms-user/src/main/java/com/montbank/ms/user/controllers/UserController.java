package com.montbank.ms.user.controllers;

import com.montbank.ms.user.dtos.UserInformationDTO;
import com.montbank.ms.user.dtos.UserInformationUpdateDTO;
import com.montbank.ms.user.dtos.UserLoginDTO;
import com.montbank.ms.user.dtos.UserRegisterDTO;
import com.montbank.ms.user.models.UserModel;
import com.montbank.ms.user.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserModel> registerUser(@RequestBody @Valid UserRegisterDTO userRegisterDTO) throws SQLException {
        if(userService.userExistsByCPF(userRegisterDTO.CPF())){
            throw new SQLException("Usuário já registrado com esse CPF, por favor digite outro CPF");
        }
        if(userService.userExistsByEmail(userRegisterDTO.email())){
            throw new SQLException("Usuário já registrado com esse email, por favor digite outro email");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userRegisterDTO));
    }
    @PostMapping("/login")
    public  ResponseEntity<String> loginUser(@RequestBody @Valid UserLoginDTO userLoginDTO) {
        String token = userService.loginUser(userLoginDTO.email(), userLoginDTO.password());
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }
    @GetMapping("/user/profile")
    public ResponseEntity<UserInformationDTO> getUserInformation(@RequestAttribute String userId){
    return ResponseEntity.status(HttpStatus.OK).body(userService.getUserInformationById(UUID.fromString(userId)));
    }
    @PutMapping("/user/profile/edit")
    public ResponseEntity<Void> updateUserInformation(@RequestBody @Valid UserInformationUpdateDTO userInformationUpdateDTO,
                                                      @RequestAttribute String userId){
        System.out.println(userInformationUpdateDTO);
        userService.updateUserInformation(userInformationUpdateDTO, UUID.fromString(userId));
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/user/profile/delete")
    public ResponseEntity<Void> deleteUser(@RequestAttribute String userId){
        userService.deleteUser(UUID.fromString(userId));
        return ResponseEntity.noContent().build();
    }
}
