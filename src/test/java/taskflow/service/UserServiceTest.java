package taskflow.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import taskflow.dto.CreateUserRequest;
import taskflow.dto.UserResponseDto;
import taskflow.entity.Role;
import taskflow.entity.User;
import taskflow.repository.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static taskflow.service.Datos.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void testCreateUser() {

        // Arrange
        CreateUserRequest request = crearUsuarioRequest();

        when(passwordEncoder.encode("1234")).thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User result = userService.createUser(request);

        // Assert
        assertEquals("Andres", result.getUsername());
        assertEquals("encodedPassword", result.getPassword());
        assertEquals("andres@test.com", result.getEmail());
        assertEquals(Role.USER, result.getRole());

        verify(passwordEncoder).encode("1234");
        verify(userRepository).save(any(User.class));

    }

    @Test
    void testUpdateUser() {
        // Arrange
        Integer userId = 1;

        User existingUser = crearUsuario().orElseThrow();

        User updatedUser = new User();
        updatedUser.setUsername("NuevoNombre");
        updatedUser.setEmail("nuevo@email.com");
        updatedUser.setPassword("newPass");
        updatedUser.setRole(Role.ADMIN);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(existingUser));

        when(passwordEncoder.encode("newPass"))
                .thenReturn("encodedPass");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User result = userService.updateUser(userId, updatedUser);

        // Assert
        assertEquals("NuevoNombre", result.getUsername());
        assertEquals("nuevo@email.com", result.getEmail());
        assertEquals("encodedPass", result.getPassword());
        assertEquals(Role.ADMIN, result.getRole());
        assertEquals("NuevoNombre", existingUser.getUsername());

        verify(userRepository).findById(userId);
        verify(passwordEncoder).encode("newPass");
        verify(userRepository).save(existingUser);
    }

    @Test
    void testGetUsers() {

        LocalDateTime now1 = LocalDateTime.now();
        LocalDateTime now2 = now1.minusDays(1);

        // Arrange
        List<Object[]> mockResults = List.of(
                new Object[]{1,"Andres","andres@email.com","USER",Timestamp.valueOf(now1),3L},
                new Object[]{2,"Maria","maria@email.com","ADMIN",Timestamp.valueOf(now2),5L}
        );

        when(userRepository.findAllWithTaskCountRaw())
                .thenReturn(mockResults);

        // Act
        List<UserResponseDto> result = userService.getUsers();

        // Assert
        assertEquals(2, result.size());

        UserResponseDto user1 = result.get(0);
        UserResponseDto user2 = result.get(1);

        // Usuario 1
        assertEquals(1, user1.id());
        assertEquals("Andres", user1.username());
        assertEquals("andres@email.com", user1.email());
        assertEquals(Role.USER, user1.role());
        assertEquals(now1, user1.createdAt());
        assertEquals(3L, user1.taskCount());

        // Usuario 2
        assertEquals(2, user2.id());
        assertEquals("Maria", user2.username());
        assertEquals("maria@email.com", user2.email());
        assertEquals(Role.ADMIN, user2.role());
        assertEquals(now2, user2.createdAt());
        assertEquals(5L, user2.taskCount());

        verify(userRepository).findAllWithTaskCountRaw();
    }

    @Test
    void testGetUserById() {

        // Arrange
        Integer userId = 1;
        User user = crearUsuario().orElseThrow();

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        // Act
        User result = userService.getUserById(userId);

        // Assert
        assertEquals(userId, result.getId());
        assertEquals("Andres", result.getUsername());
        assertEquals("1234", result.getPassword());

        verify(userRepository).findById(userId);
    }

    @Test
    void testGetUserByIdException() {
        Integer userId = 1;

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            userService.getUserById(userId);
        });

        verify(userRepository).findById(userId);
    }

    @Test
    void testDeleteUser() {

        // Arrange
        Integer userId = 1;

        when(userRepository.existsById(userId))
                .thenReturn(true);

        // Act
        userService.delete(userId);

        // Assert
        verify(userRepository).existsById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    void testDeleteUserException() {

        Integer userId = 1;

        when(userRepository.existsById(userId))
                .thenReturn(false);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            userService.delete(userId);
        });

        verify(userRepository).existsById(userId);
        verify(userRepository, never()).deleteById(any());
    }
}