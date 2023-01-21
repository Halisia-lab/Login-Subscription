package com.esgi.loginsubscription.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.esgi.loginsubscription.Service.UserService;
import com.esgi.loginsubscription.exception.ExceptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Filtre surchargeant deux méthodes de Spring Security
 * ** attemptAuthentication : Méthode permettant de récupérer le username et le mot de passe fournit
 *                            par le frontend dans la request et pour les passer à l'authenticationManager
 *
 * ** successfulAuthentication : Méthode permettant en cas d'authentification réussit de forger un accessToken JWT et
 *                               de le renvoyer au frontend dans la réponse HTTP
 * ** unsuccessfulAuthentication : Méthode permettant en cas de non-authentification de renvoyer un message d'erreur
 */
@Slf4j
public class JWTAuthenticationFilter extends
        UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public JWTAuthenticationFilter(
            AuthenticationManager authenticationManager,
            UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

        User user = (User) authentication.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());

        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 120 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        // Récupération des informations de l'utilisateur connecté depuis la BDD
        // Puis MAJ de son token
        com.esgi.loginsubscription.model.User databaseUserAuthenticated = userService.findByUserName(user.getUsername());
        databaseUserAuthenticated.setToken(accessToken);
        userService.updateUser(databaseUserAuthenticated);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("role", databaseUserAuthenticated.getRole().name());
        tokens.put("userId", databaseUserAuthenticated.getId().toString());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());

        ExceptionResponse exceptionResponse = new ExceptionResponse(
                failed.getClass().getSimpleName(),
                HttpStatus.FORBIDDEN.value(),
                "UNSUCCESSFUL_LOGIN");

        new ObjectMapper().writeValue(response.getOutputStream(), exceptionResponse);
    }

}
