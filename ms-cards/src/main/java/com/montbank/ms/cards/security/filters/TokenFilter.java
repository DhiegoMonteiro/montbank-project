package com.montbank.ms.cards.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class TokenFilter extends OncePerRequestFilter {

    private final Algorithm algoritmo;

    public TokenFilter(String secret) {
        this.algoritmo = Algorithm.HMAC256(secret);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            try {
                JWTVerifier verificador = JWT.require(algoritmo).build();

                DecodedJWT jwt = verificador.verify(token);

                request.setAttribute("userId", jwt.getClaim("id").asString());
                request.setAttribute("userEmail", jwt.getClaim("email").asString());
                request.setAttribute("name", jwt.getClaim("name").asString());
            } catch (JWTVerificationException e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
                return;
            }
        }
    filterChain.doFilter(request,response);
    }
}
