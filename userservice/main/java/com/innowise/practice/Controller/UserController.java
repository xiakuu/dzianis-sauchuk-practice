package com.innowise.practice.Controller;

import com.innowise.practice.Response.UserRequest;
import com.innowise.practice.Response.UserResponse;
import com.innowise.practice.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers (@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
            List<UserResponse> userResponse = userService.getAllUsers(page, size);
            return ResponseEntity.ok(userResponse);
        }



        @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id){
        UserResponse userResponse = userService.getUserById(id);
        return ResponseEntity.ok(userResponse);
        }

        @GetMapping("/email")
    public ResponseEntity<UserResponse> getUserByEmail(@RequestParam String email){
        UserResponse userResponse = userService.getUserByEmail(email);
        return ResponseEntity.ok(userResponse);
        }

        @PostMapping("/create")
        public ResponseEntity<UserResponse> createUser(@Validated @RequestBody UserRequest userRequest){
        UserResponse userResponseCreated = userService.createUser(userRequest);
        return ResponseEntity.ok(userResponseCreated);
        }

        @PutMapping("/{id}")
        public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @Validated @RequestBody UserRequest userRequest){
        return ResponseEntity.ok(userService.updateUser(id, userRequest));
        }

        @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUserById(@RequestParam Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
        }


    }



    


