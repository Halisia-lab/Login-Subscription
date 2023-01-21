package com.esgi.loginsubscription.payload;

import com.sun.istack.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;


public class UserDtoWithPassword extends UserDto implements Serializable {
    @NotNull
    @Size(min = 3, max = 100)
    private String password;

    public UserDtoWithPassword(String password) {
        super();
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDtoWithPassword that = (UserDtoWithPassword) o;
        return Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }

}
