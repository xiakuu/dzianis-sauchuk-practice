package com.innowise.practice.Service;

import com.innowise.practice.CustomExceptions.DuplicateException;
import com.innowise.practice.CustomExceptions.ResourceNotFoundException;
import com.innowise.practice.Mapper.UserMapper;
import com.innowise.practice.Response.UserRequest;
import com.innowise.practice.Response.UserResponse;
import com.innowise.practice.Repository.UserRepository;
import com.innowise.practice.DBSchema.Users;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @CachePut(value = "users", key = "#result.id")
    public UserResponse createUser(UserRequest userRequest) {
        if(userRepository.findByEmail(userRequest.getEmail()) != null){
            throw new DuplicateException("User with this email already exists");
        }
        Users savedUser = userRepository.save(userMapper.toUser(userRequest));
        return userMapper.toUserResponse(savedUser);
    }
    @Cacheable(value = "users", key = "#id")
    public UserResponse getUserById(Long id){
        Users user = userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User with id " + id + " not found"));
        return userMapper.toUserResponse(user);
    }

    public List<UserResponse> getAllUsers (int page, int size){
            return userRepository.findAll(PageRequest.of(page, size)).stream().map(userMapper::toUserResponse).collect(Collectors.toList());
    }
    @Cacheable(value = "users", key = "#email")
    public UserResponse getUserByEmail(String email){
        try {
            Users user = userRepository.findByEmail(email);
            return userMapper.toUserResponse(user);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("user with email " + email + " not found");
        }
    }
    @CachePut(value = "users", key = "#id")
    @Transactional
    public UserResponse updateUser(Long id, UserRequest userRequest) {
        Users user = userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("User with id " + id + " not found"));

        user.setEmail(userRequest.getEmail());
        user.setName(userRequest.getName());
        user.setSurname(userRequest.getSurname());
        user.setBirth_date(userRequest.getBirth_date());
        Users updatedUser = userRepository.save(user);
        return userMapper.toUserResponse(updatedUser);
    }
    @CacheEvict(value = "users", key = "#id")
    @Transactional
    public void deleteUser(Long id){
        if(!userRepository.existsById(id)){
            throw new ResourceNotFoundException("User with id " + id + " not found");
        }
            userRepository.deleteById(id);

    }



}
