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

    private String description;

    @NotNull(message = "DueDate must not be null")
    private LocalDateTime dueDate;

    @NotNull(message = "status must not be null")
    private TaskStatus status;

    public CreateTaskRequest(){}

    public CreateTaskRequest(String title, String description, LocalDateTime dueDate, TaskStatus status) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
    }
}


