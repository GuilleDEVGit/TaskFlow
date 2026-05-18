package taskflow.dto;

import taskflow.entity.ActionType;

import java.time.LocalDateTime;

public record ActivityLogResponse(
        Long id,
        String username,
        ActionType action,
        String details,
        LocalDateTime createdAt
) {}
