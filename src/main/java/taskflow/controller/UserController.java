package taskflow.controller;

import io.swagger.v3.oas.annotations.Operation;
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


    @Operation(
            summary = "Get all users",
            description = "Requires roles: ADMIN"
    )
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public List<User> getAll() {
        return userService.getAllUsers();
    }

    @Operation(
            summary = "Get the user by id",
            description = "Requires roles: ADMIN"
    )
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @Operation(
            summary = "Create a new user",
            description = "Requires roles: ADMIN"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public User create(@Valid @RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }
}
