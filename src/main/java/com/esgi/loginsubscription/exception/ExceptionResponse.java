package com.esgi.loginsubscription.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse {

    private String exceptionType;
    private int code;
    private String message;

    @Override
    public String toString() {
        return "ExceptionResponse{" +
                "exceptionType='" + exceptionType + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
