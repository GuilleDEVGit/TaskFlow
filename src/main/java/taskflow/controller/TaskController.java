package taskflow.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import taskflow.dto.CreateTaskRequest;
import taskflow.dto.UpdateTaskStatusRequest;
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

    @Operation(
            summary = "Get all the tasks",
            description = "Requires roles: ADMIN"
    )
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public List<Task> getTasks() {
        return taskService.getTasks();
    }

    @Operation(
            summary = "Get the tasks by userId",
            description = "Requires roles: USER, ADMIN"
    )
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/my")
    public List<Task> getMyTasks(Authentication authentication) {
        return taskService.getMyTasks(authentication.getName());
    }


    @Operation(
            summary = "Create a new task",
            description = "Requires roles: USER, ADMIN"
    )
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping
    public Task createTask(@Valid @RequestBody CreateTaskRequest request, Authentication authentication) {
        return taskService.createTask(request, authentication.getName());
    }


    @Operation(
            summary = "Update status task",
            description = "Requires roles: USER, ADMIN"
    )
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateTaskStatusRequest request,
            Authentication auth
    ) {
        System.out.println("Entro en Update status ");
        taskService.updateStatus(id, request, auth);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/debug/roles")
    public Object debugRoles(Authentication authentication) {
        return authentication.getAuthorities();
    }

}
