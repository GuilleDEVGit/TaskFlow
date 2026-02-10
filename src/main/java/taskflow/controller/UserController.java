package taskflow.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import taskflow.dto.CreateUserRequest;
import taskflow.dto.UserResponseDto;
import taskflow.entity.Task;
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
    public List<UserResponseDto> getAll() {
        return userService.getAllUsers();
    }

    @Operation(
            summary = "Get the user by id",
            description = "Requires roles: ADMIN"
    )
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/{id}")
    public User getById(@PathVariable Integer id) {
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

    @Operation(
            summary = "Update user",
            description = "Requires roles: ADMIN"
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Integer id,
            @RequestBody User updatedUser
    ) {
        User user = userService.updateUser(id, updatedUser);
        return ResponseEntity.ok(user);
    }


    @Operation(
            summary = "Delete a user",
            description = "Requires roles: ADMIN"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id){
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
