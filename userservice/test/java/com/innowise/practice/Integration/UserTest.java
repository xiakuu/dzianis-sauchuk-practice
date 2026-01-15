package com.innowise.practice.Integration;


import com.innowise.practice.CustomExceptions.DuplicateException;
import com.innowise.practice.CustomExceptions.ResourceNotFoundException;
import com.innowise.practice.Repository.UserRepository;
import com.innowise.practice.Response.UserRequest;
import com.innowise.practice.Response.UserResponse;
import com.innowise.practice.Service.UserService;
import org.apache.catalina.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.assertj.core.api.Assertions;

import java.time.LocalDate;

@Testcontainers
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class UserTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Container
    private static final MySQLContainer<?> mysql = new MySQLContainer<>(DockerImageName.parse("mysql:8.0")).withDatabaseName("test").withUsername("user").withPassword("password");


    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @Test
    void createUser() {
        UserRequest userRequest = new UserRequest();
        userRequest.setName("test");
        userRequest.setEmail("test@test.com");
        userRequest.setSurname("test");
        userRequest.setBirth_date(LocalDate.of(1990, 1, 1));
        UserResponse userResponse1 = userService.createUser(userRequest);

        Assertions.assertThat(userResponse1).isNotNull();
    }

    @Test
    void createUser_Duplicate() {
        UserRequest userRequest = new UserRequest();
        userRequest.setName("test");
        userRequest.setEmail("admin@server.com"); //from liquibase
        userRequest.setSurname("test");
        userRequest.setBirth_date(LocalDate.of(1990, 1, 1));
        Assertions.assertThatThrownBy(() -> userService.createUser(userRequest)).isInstanceOf(DuplicateException.class);
    }

    @Test
    void findUser() {
        UserResponse userResponse = userService.getUserById(1L);
        Assertions.assertThat(userResponse).isNotNull();
    }

    @Test
    void findUser_NotFound() {
        Assertions.assertThatThrownBy(() -> userService.getUserById(100L)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateUser() {
        UserRequest userRequest = new UserRequest();
        userRequest.setName("test");
        userRequest.setEmail("admin@server.com"); //from liquibase
        userRequest.setSurname("test");
        userRequest.setBirth_date(LocalDate.of(1990, 1, 1));
        UserResponse userResponse = userService.updateUser(1L, userRequest);
        Assertions.assertThat(userResponse).isNotNull();
        Assertions.assertThat(userResponse.getName()).isEqualTo(userRequest.getName());
    }

    @Test
    void updateUser_NotFound() {
        UserRequest userRequest = new UserRequest();
        userRequest.setName("test");
        userRequest.setEmail("admin@server.com"); //from liquibase
        userRequest.setSurname("test");
        userRequest.setBirth_date(LocalDate.of(1990, 1, 1));
        Assertions.assertThatThrownBy(() -> userService.updateUser(100L, userRequest)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteUser() {
        UserRequest userRequest = new UserRequest();
        userRequest.setName("test");
        userRequest.setEmail("dasdasd@mail.cd"); //from liquibase
        userRequest.setSurname("test");
        userRequest.setBirth_date(LocalDate.of(1990, 1, 1));
        UserResponse result = userService.createUser(userRequest);

        userService.deleteUser(result.getId());
        Assertions.assertThat(userRepository.existsById(result.getId())).isFalse();
    }

    @Test
    void deleteUser_NotFound() {
        Assertions.assertThatThrownBy(() -> userService.deleteUser(100L)).isInstanceOf(ResourceNotFoundException.class);
    }


}
