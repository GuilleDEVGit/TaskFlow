package taskflow.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import taskflow.entity.TaskStatus;

@Data
@AllArgsConstructor
public class UpdateTaskStatusRequest {

    @NotNull
    private TaskStatus status;

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}
