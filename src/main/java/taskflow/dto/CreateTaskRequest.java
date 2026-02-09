package taskflow.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import taskflow.entity.TaskStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class CreateTaskRequest {

    @NotBlank(message = "Tittle must not be blank")
    private String title;

    @NotBlank(message = "Description must not be blank")
    private String description;

    @NotNull(message = "DueDate must not be null")
    private LocalDateTime dueDate;

    @NotNull(message = "status must not be null")
    private TaskStatus status;
}
