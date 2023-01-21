package com.esgi.loginsubscription.payload.request;




import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Requête d'authentification
 */

public class LoginRequest {
    @NotBlank
    @Size(min = 10, max = 150)
    private String email;

    @NotBlank
    @Size(min = 3, max = 100)
    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
