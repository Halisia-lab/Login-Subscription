package com.esgi.loginsubscription.payload.request;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {

    @NotBlank(message = "Name cannot be empty")
    private String firstName;

    @NotBlank(message = "Name cannot be empty")
    private String lastName;

    @NotBlank(message = "Name cannot be empty")
    private String city;

    @NotBlank(message = "Name cannot be empty")
    private String address;

    @NotBlank(message = "Name cannot be empty")
    private String email;

    @NotBlank(message = "Name cannot be empty")
    private String phoneNumber;

    @NotBlank(message = "UserName cannot be empty")
    private String username;

    @NotBlank(message = "UserName cannot be empty")
    private String postIndex;

    @Size(min = 6, max = 16, message = "The password must be between 6 and 16 characters long")
    private String password;



}
