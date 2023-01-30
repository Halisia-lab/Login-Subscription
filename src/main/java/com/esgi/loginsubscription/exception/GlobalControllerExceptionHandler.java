package com.esgi.loginsubscription.exception;

import com.esgi.loginsubscription.config.CodeException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.DateTimeException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Classe permettant de gérer les exceptions lancées par le backend
 * Construit un objet ExceptionResponse avec les informations
 * récupérer dans l'exception et dans le fichier messages-exceptions.properties
 * pour le renvoyer au frontend
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice(annotations = {RestController.class})
public class GlobalControllerExceptionHandler implements AccessDeniedHandler {

    public String getMessage(String messageCode) {
        return ResourceBundle.getBundle("messages-exceptions").getString(messageCode);
    }

    @ExceptionHandler(value= {EntityAlreadyExistingException.class, BadRequestException.class,
            ChangementStatutImpossibleException.class, CreationImpossibleException.class, DateTimeException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ExceptionResponse handleFormationFunctionalExceptions(RuntimeException exception) {
        String errorMessage;
        try {
            errorMessage = getMessage(exception.getMessage());
        } catch (MissingResourceException e) {
            errorMessage = getMessage(CodeException.GENERAL_ERROR);
        }
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                exception.getClass().getSimpleName(),
                HttpStatus.BAD_REQUEST.value(),
                errorMessage);

        return exceptionResponse;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ExceptionResponse handleEntityNotFoundException(EntityNotFoundException exception) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                exception.getClass().getSimpleName(),
                HttpStatus.BAD_REQUEST.value(),
                getMessage(exception.getMessage()));

        return exceptionResponse;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ExceptionResponse handleMethodArgumentNotValidException (
            MethodArgumentNotValidException exception) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                exception.getClass().getSimpleName(),
                HttpStatus.BAD_REQUEST.value(),
                getMessage(CodeException.WRONG_FORM_DATA));
        return exceptionResponse;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ExceptionResponse handleIllegalArgumentException(
            IllegalArgumentException exception) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                exception.getClass().getSimpleName(),
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage());
        return exceptionResponse;
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ExceptionResponse handleIOException(
            IOException exception) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                exception.getClass().getSimpleName(),
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage());
        return exceptionResponse;
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ExceptionResponse handleNullPointerException(NullPointerException exception) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                exception.getClass().getSimpleName(),
                HttpStatus.BAD_REQUEST.value(),
                exception.getMessage());

        return exceptionResponse;
    }


    /**
     * Méthode permettant de gérer les AccessDenied provoqué par les permissions dans les contrôleurs
     * Définition de la méthode de l'interface AccessDeniedHandler
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                exception.getClass().getSimpleName(),
                HttpStatus.FORBIDDEN.value(),
                getMessage(CodeException.ACCESS_DENIED));

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), exceptionResponse);
    }
}
