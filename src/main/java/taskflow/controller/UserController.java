package taskflow.controller;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import taskflow.dto.CreateUserRequest;
import taskflow.entity.User;
import taskflow.repository.UserRepository;
import taskflow.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public List<User> getAll() {
        return userService.getAllUsers();
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public User create(@Valid @RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }
}
