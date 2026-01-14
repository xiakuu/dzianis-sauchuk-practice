package com.innowise.practice.Integration;


import com.innowise.practice.CustomExceptions.ResourceNotFoundException;
import com.innowise.practice.DBSchema.CardInfo;
import com.innowise.practice.DBSchema.Users;
import com.innowise.practice.Repository.CardInfoRepository;
import com.innowise.practice.Repository.UserRepository;
import com.innowise.practice.Response.CardInfoRequest;
import com.innowise.practice.Response.CardInfoResponse;
import com.innowise.practice.Response.UserRequest;
import com.innowise.practice.Response.UserResponse;
import com.innowise.practice.Service.CardInfoService;
import com.innowise.practice.Service.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import static org.junit.jupiter.api.Assertions.*;
import org.assertj.core.api.Assertions;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootTest
@Testcontainers
@ExtendWith(SpringExtension.class)
public class CardInfoTest {

    @Autowired
    private CardInfoService cardInfoService;

    @Autowired
    private UserService userService;

    @Autowired
    private CardInfoRepository cardInfoRepository;


    @Container
    private static final MySQLContainer<?> mysql = new MySQLContainer<>(DockerImageName.parse("mysql:8.0")).withDatabaseName("test").withUsername("user").withPassword("password");


    @DynamicPropertySource
     static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

//    @BeforeEach
//    void setUp() {
//        UserRequest userRequest = new UserRequest();
//        userRequest.setName("test");
//        userRequest.setEmail("test@test.com");
//        userRequest.setSurname("test");
//        userRequest.setBirth_date(LocalDate.of(1990, 1, 1));
//        UserResponse userResponse1 = userService.createUser(userRequest);
//
//
//        CardInfoRequest card = new CardInfoRequest();
//        card.setUserId(userResponse1.getId());
//        card.setNumber("1020304050607080");
//        card.setHolder("test");
//        card.setExpirationDate(LocalDate.now().plusDays(10));
//   }

    @Test
    void createCard() {
        UserRequest userRequest = new UserRequest();
        userRequest.setName("test");
        userRequest.setEmail("test@test.com");
        userRequest.setSurname("test");
        userRequest.setBirth_date(LocalDate.of(1990, 1, 1));
        UserResponse userResponse1 = userService.createUser(userRequest);


        CardInfoRequest card = new CardInfoRequest();
        card.setUserId(userResponse1.getId());
        card.setNumber("1020304050607080");
        card.setHolder("test");
        card.setExpirationDate(LocalDate.now().plusDays(10));

        CardInfoResponse result = cardInfoService.createCard(card);

        Assertions.assertThat(result).isNotNull();
    }

    @Test
    void getCard() {
        CardInfoResponse response = cardInfoService.getCard(1L);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThatNoException();
    }

    @Test
    void getCard_NotFound() {
        Assertions.assertThatThrownBy(() -> cardInfoService.getCard(100L)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteCard() {
        CardInfoRequest card = new CardInfoRequest();
        card.setUserId(1L);
        card.setNumber("1020304050607080");
        card.setHolder("test");
        card.setExpirationDate(LocalDate.now().plusDays(10));
        CardInfoResponse response = cardInfoService.createCard(card);

        cardInfoService.deleteCard(response.getId());
        Assertions.assertThat(cardInfoRepository.existsById(response.getId())).isFalse();
    }

    @Test
    void deleteCard_NotFound() {
        Assertions.assertThatThrownBy(() -> cardInfoService.deleteCard(100L)).isInstanceOf(ResourceNotFoundException.class);
    }





}
