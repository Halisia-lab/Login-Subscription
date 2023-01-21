package com.esgi.loginsubscription;

import com.esgi.loginsubscription.controller.UserController;
import com.esgi.loginsubscription.payload.request.RegistrationRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class LoginSubscriptionApplicationTests {

    @Autowired
    private UserController userController;

    @Test
    void registrationTest() {
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setFirstName("yacine");
        registrationRequest.setLastName("z");
        registrationRequest.setCity("maville");
        registrationRequest.setAddress("my adress");
        registrationRequest.setEmail("myMail@mail.com");
        registrationRequest.setPhoneNumber("0123456789");
        registrationRequest.setUsername("yacine");

        registrationRequest.setPostIndex("94");
        registrationRequest.setPassword("pass");
        assertThat(userController.saveUser(registrationRequest)).isNotNull();
    }

}
