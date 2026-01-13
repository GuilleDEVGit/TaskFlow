package taskflow.controller;

import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import taskflow.dto.CreateTaskRequest;
import taskflow.entity.Task;
import org.springframework.web.bind.annotation.*;
import taskflow.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController
{
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public List<Task> getTasks() {
        return taskService.getTasks();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my")
    public List<Task> getMyTasks(Authentication authentication) {
        return taskService.getMyTasks(authentication.getName());
    }


    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping
    public Task createTask(@Valid @RequestBody CreateTaskRequest request, Authentication authentication) {
        return taskService.createTask(request, authentication.getName());
    }
}
