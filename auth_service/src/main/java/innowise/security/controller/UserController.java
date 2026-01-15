package innowise.security.controller;

import innowise.security.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import innowise.security.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping("/admin/getall")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(service.getAllUsers());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

//    @GetMapping("/admin/getall")
//    public ResponseEntity<List<User>> getAllUsersById() {
//        return ResponseEntity.ok(service.getAllUsers());
//    }

    @GetMapping("/get-admin")
    public void getAdmin() {
        service.getAdmin();
    }
}