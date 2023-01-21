package com.esgi.loginsubscription.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoleToUserDto {

    private Long id;
    private String roleName;
}

