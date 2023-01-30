package com.esgi.loginsubscription.controller;

import com.esgi.loginsubscription.Service.UserService;
import com.esgi.loginsubscription.model.User;
import com.esgi.loginsubscription.payload.RoleToUserDto;
import com.esgi.loginsubscription.payload.request.RegistrationRequest;
import com.esgi.loginsubscription.payload.response.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.modelmapper.ModelMapper;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private final UserService userService;
    @Autowired
    private final ModelMapper modelMapper;


    @GetMapping(path ="/users",produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<UserResponse>>getUsers(){
        List<User> users = userService.getUsers();
        List<UserResponse> userResponseList = users.stream()
                .map(user -> new UserResponse(user.getId(), user.getFirstName(), user.getUsername(), user.getRole().name()))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(userResponseList);
    }

    @PostMapping("/user/save")
    public ResponseEntity<Void> saveUser(@RequestBody RegistrationRequest user){
        User newUser = userService.saveUser(modelMapper.map(user,User.class));
        URI location = URI.create(
                ServletUriComponentsBuilder.fromCurrentRequest().build().toUri() + "/" + newUser.getId());
        return ResponseEntity.created(location).build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/user/addRoleToUser")
    public ResponseEntity<?>saveRole(@RequestBody RoleToUserDto roleToUserDto){
        userService.addRoleToUser(roleToUserDto.getId(), roleToUserDto.getRoleName());
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(path ="/user/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
