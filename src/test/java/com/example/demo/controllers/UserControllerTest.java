package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepository = Mockito.mock(UserRepository.class);

    private CartRepository cartRepository = Mockito.mock(CartRepository.class);

    private BCryptPasswordEncoder bCryptPasswordEncoder = Mockito.mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp(){
        userController = new UserController();
        TestUtils.injectObject(userController, "userRepository", userRepository );
        TestUtils.injectObject(userController, "cartRepository", cartRepository );
        TestUtils.injectObject(userController,"bCryptPasswordEncoder",bCryptPasswordEncoder);
    }

    @Test
    public void createUser_successTest(){
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("password123456");
        createUserRequest.setConfirmPassword("password123456");
        ResponseEntity<User> userResponseEntity = userController.createUser(createUserRequest);
        Assertions.assertThat(userResponseEntity).isNotNull();
    }
}
