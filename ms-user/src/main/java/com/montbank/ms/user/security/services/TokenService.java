package com.montbank.ms.user.security.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.montbank.ms.user.models.UserModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(UserModel userModel){
        return JWT.create().withSubject(userModel.getUserId().toString())
                .withClaim("id", userModel.getUserId().toString())
                .withClaim("email", userModel.getEmail())
                .withClaim("name", userModel.getName())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiration))
                .sign(Algorithm.HMAC256(secret));
    }

}
