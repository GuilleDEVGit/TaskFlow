package taskflow.controller;

import jakarta.validation.Valid;
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

    @GetMapping
    public List<Task> getTasks() {
        return taskService.getTasks();
    }

    @PostMapping
    public Task createTask(@Valid @RequestBody CreateTaskRequest request) {
        return taskService.createTask(request);
    }
}
