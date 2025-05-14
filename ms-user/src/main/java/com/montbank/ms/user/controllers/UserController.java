package com.montbank.ms.user.controllers;

import com.montbank.ms.user.dtos.UserLoginDTO;
import com.montbank.ms.user.dtos.UserRegisterDTO;
import com.montbank.ms.user.models.UserModel;
import com.montbank.ms.user.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserModel> registerUser(@RequestBody @Valid UserRegisterDTO userRegisterDTO) {
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userRegisterDTO, userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userModel));
    }
    @PostMapping("/login")
    public  ResponseEntity<String> loginUser(@RequestBody @Valid UserLoginDTO userLoginDTO) {
        String token = userService.loginUser(userLoginDTO.email(), userLoginDTO.password());
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }
}
