package com.innowise.practice.Unit;

import com.innowise.practice.CustomExceptions.DuplicateException;
import com.innowise.practice.CustomExceptions.ResourceNotFoundException;
import com.innowise.practice.Mapper.UserMapper;
import com.innowise.practice.Response.CardInfoResponse;
import com.innowise.practice.Response.UserRequest;
import com.innowise.practice.Response.UserResponse;
import com.innowise.practice.Service.UserService;
import com.innowise.practice.Repository.UserRepository;
import com.innowise.practice.DBSchema.CardInfo;
import com.innowise.practice.DBSchema.Users;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private Users user;
    private UserResponse userResponse;
    private Long userId;
    private UserRequest userRequest;


    @BeforeEach
    void setUp() {
        userId = 10L;
        user = new Users();
        user.setId(userId);
        user.setBirth_date(LocalDate.of(1999, 12, 23));
        user.setSurname("testsurname");
        user.setEmail("testemail@test.com");

        userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setBirth_date(user.getBirth_date());
        userResponse.setSurname(user.getSurname());
        userResponse.setEmail(user.getEmail());

        userRequest = new UserRequest();
        userResponse.setBirth_date(user.getBirth_date());
        userResponse.setSurname(user.getSurname());
        userResponse.setEmail(user.getEmail());
    }

    @Test
    void createUser() {
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserResponse(user)).thenReturn(userResponse);
        when(userMapper.toUser(userRequest)).thenReturn(user);

        UserResponse result = userService.createUser(userRequest);
        assertEquals(userResponse.getId(), result.getId());
    }

    @Test
    void createUser_DuplicateException() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(DuplicateException.class, () -> userService.createUser(userRequest));
    }

    @Test
    void getUser() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toUserResponse(user)).thenReturn(userResponse);
        UserResponse result = userService.getUserById(userId);
        assertEquals(userResponse.getId(), result.getId());

    }

    @Test
    void getUser_NotFoundException() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(userId));
    }

    @Test
    void deleteUser() {
        when(userRepository.existsById(userId)).thenReturn(true);
        assertDoesNotThrow(() -> userService.deleteUser(userId));
    }

    @Test
    void deleteUser_NotFoundException() {
        when(userRepository.existsById(userId)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(userId));
    }

    @Test
    void updateUser() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toUserResponse(user)).thenReturn(userResponse);
        when(userRepository.save(user)).thenReturn(user);

        assertEquals(userService.updateUser(userId, userRequest), userResponse);

        verify(userRepository).findById(userId);
        verify(userRepository).save(user);
        verify(userMapper).toUserResponse(user);
    }

    @Test
    void updateUser_NotFoundException() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(userId, userRequest));
        verify(userRepository).findById(userId);
    }






}