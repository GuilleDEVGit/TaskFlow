package taskflow.dto;

import taskflow.entity.Role;
import java.time.LocalDateTime;


public record UserResponseDto(
        Integer id,
        String username,
        String email,
        Role role,
        LocalDateTime createdAt,
        Long taskCount
) {}

