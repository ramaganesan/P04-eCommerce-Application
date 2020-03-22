package com.example.demo.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.model.persistence.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

@Slf4j
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;


    private int EXPIRATION_TIME = 60;


    private String SECRET = "auth-course";

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;

    }

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            User user = new ObjectMapper().readValue(request.getInputStream(),User.class);
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), new ArrayList<>());
            this.setDetails(request, authRequest);
            return this.authenticationManager.authenticate(authRequest);
        } catch (IOException e) {
            logger.error("Error during authentication" + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(EXPIRATION_TIME);
        String token = JWT.create()
                        .withSubject(((org.springframework.security.core.userdetails.User) authResult.getPrincipal()).getUsername())
                        .withExpiresAt(Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant()))
                         .sign(Algorithm.HMAC512(SECRET.getBytes()));
        response.setHeader(SecurityConstants.HEADER_STRING,SecurityConstants.TOKEN_PREFIX+token);
    }
}
