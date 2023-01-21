package com.esgi.loginsubscription.security;

import com.esgi.loginsubscription.exception.ExceptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ResourceBundle;

public class FilterExceptionHandler extends OncePerRequestFilter {

    public String getMessage(String messageCode) {
        return ResourceBundle.getBundle("messages-exceptions").getString(messageCode);
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (RuntimeException exception) {
            ExceptionResponse exceptionResponse = new ExceptionResponse(
                    exception.getClass().getSimpleName(),
                    HttpStatus.BAD_REQUEST.value(),
                    getMessage(exception.getMessage()));

            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), exceptionResponse);
        }
    }
}
