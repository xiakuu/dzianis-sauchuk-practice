package innowise.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import innowise.security.domain.model.Role;
import innowise.security.domain.model.User;
import innowise.security.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;


    public User create(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }

        if (repository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }

        return repository.save(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN') or #username = authentication.name")
    public User getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }

    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isUserOwner(#id)")
    public User getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Пользователь с id " + id + " не найден"));
    }

    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isUserOwner(#id)")
    public User updateUser(Long id, User user) {
        User oldUser = getById(id);
        oldUser.setUsername(user.getUsername());
        oldUser.setEmail(user.getEmail());
        oldUser.setPassword(user.getPassword());
        return repository.save(oldUser);
    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    public User getCurrentUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }

    public void getAdmin() {
        var user = getCurrentUser();
        user.setRole(Role.ROLE_ADMIN);
        repository.save(user);
    }
}
