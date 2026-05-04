package taskflow.dto;

import taskflow.entity.TaskStatus;

import java.time.LocalDateTime;

public record TaskUpdateDTO (
        int id,
        String title,
        String description,
        TaskStatus status,
        LocalDateTime dueDate,
        int userId
) {}
