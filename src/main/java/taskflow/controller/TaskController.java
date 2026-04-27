package taskflow.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import taskflow.dto.CreateTaskRequest;
import taskflow.dto.TaskResponse;
import taskflow.dto.UpdateTaskStatusRequest;
import taskflow.entity.Task;
import org.springframework.web.bind.annotation.*;
import taskflow.entity.TaskStatus;
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

    //GET
    @Operation(
            summary = "Filter task by arguments",
            description = "Requires roles: USER, ADMIN"
    )
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public Page<TaskResponse> getTasks(
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size
    ) {
        return taskService.getTasksByFilters(userId, status, title, page, size);
    }


    //CREATE
    @Operation(
            summary = "Create a new task",
            description = "Requires roles: USER, ADMIN"
    )
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping
    public Task createTask(@Valid @RequestBody CreateTaskRequest request, Authentication authentication) {
        return taskService.createTask(request, authentication.getName());
    }

    //UPDATE
    @Operation(
            summary = "Update task",
            description = "Requires roles: USER, ADMIN"
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Integer id,@RequestBody Task updatedTask) {
        TaskResponse task = taskService.update(id, updatedTask);
        return ResponseEntity.ok(task);
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
        taskService.updateStatus(id, request, auth);
        return ResponseEntity.noContent().build();
    }

    //DELETE
    @Operation(
            summary = "Delete the tasks by id",
            description = "Requires roles: ADMIN"
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> deleteTask(@PathVariable Integer id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }




}
