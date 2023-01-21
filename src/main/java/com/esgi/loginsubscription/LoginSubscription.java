package com.esgi.loginsubscription;

import com.esgi.loginsubscription.Service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LoginSubscription {


    public static void main(String[] args) {
        SpringApplication.run(LoginSubscription.class, args);
    }

    @Bean
    CommandLineRunner run (UserService userService){
        return args -> {

            //userService.saveUSer(new User(null,"Francois","francoish","francoish123",Role.ROLE_USER));
            //userService.saveUSer(new User(null,"Yacine","yacinez","yacine123",Role.ROLE_ADMIN));

            //userService.addRoleToUser("yacinez","ROLE_USER");
            //userService.addRoleToUser("yacinez","ROLE_USER");
            //userService.addRoleToUser("francoish","ROLE_USER");
        };
    }
}
