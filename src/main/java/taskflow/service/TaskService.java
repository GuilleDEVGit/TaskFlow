package taskflow.service;

import ch.qos.logback.core.status.Status;
import jakarta.annotation.Priority;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import taskflow.dto.CreateTaskRequest;
import taskflow.dto.TaskResponse;
import taskflow.dto.UpdateTaskStatusRequest;
import taskflow.entity.Task;
import taskflow.entity.TaskStatus;
import taskflow.entity.User;
import taskflow.repository.TaskRepository;
import taskflow.repository.TaskSpecification;
import taskflow.repository.UserRepository;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    //GET
    public Page<TaskResponse> getTasksByFilters(Integer userId,TaskStatus status,String title,int page,int size) {

        Pageable pageable = PageRequest.of(page, size);

        Specification<Task> spec = Specification.allOf(
                TaskSpecification.hasUserId(userId),
                TaskSpecification.hasStatus(status),
                TaskSpecification.titleContains(title)
        );

        Page<Task> taskPage = taskRepository.findAll(spec, pageable);

        return taskPage.map(task -> new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getDueDate(),
                task.getCreatedAt(),
                task.getUser().getUsername()
        ));
    }

    //CREATE
    public Task createTask(CreateTaskRequest request, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));


        Task task = new Task(
                request.getTitle(),
                request.getDescription(),
                request.getDueDate(),
                request.getStatus(),
                user
        );

        return taskRepository.save(task);
    }

    //UPDATE
    public TaskResponse update(Integer id, Task updatedTask) {

        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setStatus(updatedTask.getStatus());
        existingTask.setDueDate(updatedTask.getDueDate());

        Task savedTask = taskRepository.save(existingTask);

        return new TaskResponse(
                savedTask.getId(),
                savedTask.getTitle(),
                savedTask.getDescription(),
                savedTask.getStatus(),
                savedTask.getDueDate(),
                savedTask.getCreatedAt(),
                savedTask.getUser().getUsername()
        );
    }

    public void updateStatus(Integer taskId,UpdateTaskStatusRequest request,Authentication auth) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow();

        boolean isOwner = task.getUser().getId().equals(user.getId());

        task.setStatus(request.getStatus());
        taskRepository.save(task);
    }

    //DELETE
    public void delete(Integer id) {
        if (!taskRepository.existsById(id)) {
            throw new EntityNotFoundException("Task not found");
        }
        taskRepository.deleteById(id);
    }



}
