package taskflow.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import taskflow.dto.CreateUserRequest;
import taskflow.dto.UserOptionDTO;
import taskflow.dto.UserResponseDto;
import taskflow.entity.ActionType;
import taskflow.entity.ActivityLog;
import taskflow.entity.Role;
import taskflow.entity.User;
import taskflow.repository.ActivityLogRepository;
import taskflow.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final ActivityLogRepository activityLogRepository;

    private static final Logger logger =
            LogManager.getLogger(UserService.class);

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthService authService, ActivityLogRepository activityLogRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
        this.activityLogRepository = activityLogRepository;
    }

    public User createUser(CreateUserRequest request) {

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRole(Role.valueOf(request.getRole()));

        User savedUser = userRepository.save(user);

        User loggedUser = authService.getLoggedUser();
        String msgLog = "Usuario creado: " + user.getUsername() + " - " + user.getEmail();
        ActivityLog activityLog = new ActivityLog();
        activityLog.setUserId(Long.valueOf(loggedUser.getId()));
        activityLog.setUsername(loggedUser.getUsername());
        activityLog.setAction(ActionType.CREATE_USER);
        activityLog.setDetails(msgLog);
        activityLog.setCreatedAt(LocalDateTime.now());

        activityLogRepository.save(activityLog);

        logger.info(
                "USER_CREATED id={} user={} email={} role={}",
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRole()
        );

        return savedUser;
    }

    public User updateUser(Integer id, User updatedUser) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        existingUser.setRole(updatedUser.getRole());

        existingUser = userRepository.save(existingUser);

        User loggedUser = authService.getLoggedUser();
        String msgLog = "Usuario actualizado: " + existingUser.getUsername() + " - " + existingUser.getEmail();
        ActivityLog activityLog = new ActivityLog();
        activityLog.setUserId(Long.valueOf(loggedUser.getId()));
        activityLog.setUsername(loggedUser.getUsername());
        activityLog.setAction(ActionType.UPDATE_USER);
        activityLog.setDetails(msgLog);
        activityLog.setCreatedAt(LocalDateTime.now());

        activityLogRepository.save(activityLog);

        logger.info(
                "USER_UPDATED id={} user={} email={} role={}",
                existingUser.getId(),
                existingUser.getUsername(),
                existingUser.getEmail(),
                existingUser.getRole()
        );

        return existingUser;
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

    public List<UserOptionDTO> getUserOptions() {
        return userRepository.getUserOptions();
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void delete(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        userRepository.deleteById(id);

        User loggedUser = authService.getLoggedUser();
        String msgLog = "Usuario eliminado: " + user.getUsername() + " - " + user.getEmail();
        ActivityLog activityLog = new ActivityLog();
        activityLog.setUserId(Long.valueOf(loggedUser.getId()));
        activityLog.setUsername(loggedUser.getUsername());
        activityLog.setAction(ActionType.DELETE_USER);
        activityLog.setDetails(msgLog);
        activityLog.setCreatedAt(LocalDateTime.now());

        activityLogRepository.save(activityLog);

        logger.info(
                "USER_DELETED id={} user={} email={} role={}",
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }
}
