package taskflow.dto;

import lombok.AllArgsConstructor;
import taskflow.entity.TaskStatus;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TaskResponse {
    private int id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private String username;
    private int userId;
}
