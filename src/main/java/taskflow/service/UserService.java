package taskflow.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import taskflow.dto.CreateUserRequest;
import taskflow.dto.UserResponseDto;
import taskflow.entity.Role;
import taskflow.entity.Task;
import taskflow.entity.User;
import taskflow.repository.UserRepository;

import java.security.Timestamp;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(CreateUserRequest request) {

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRole(Role.valueOf(request.getRole()));

        return userRepository.save(user);
    }

    public User updateUser(Integer id, User updatedUser) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        existingUser.setRole(updatedUser.getRole());

        return userRepository.save(existingUser);
    }



    public List<UserResponseDto> getUsers() {

        List<Object[]> results = userRepository.findAllWithTaskCountRaw();

        return results.stream()
                .map(row -> new UserResponseDto(
                        ((Number) row[0]).intValue(),        // id
                        (String) row[1],                     // username
                        (String) row[2],                     // email
                        Role.valueOf((String) row[3]),       // role
                        ((java.sql.Timestamp) row[4]).toLocalDateTime(),
                         ((Number) row[5]).longValue()        // taskCount
                ))
                .toList();
    }



    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void delete(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }
}
