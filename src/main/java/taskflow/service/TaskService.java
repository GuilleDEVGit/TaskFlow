package taskflow.service;

import jakarta.persistence.EntityNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import taskflow.dto.CreateTaskRequest;
import taskflow.dto.TaskResponse;
import taskflow.dto.TaskUpdateDTO;
import taskflow.dto.UpdateTaskStatusRequest;
import taskflow.entity.*;
import taskflow.repository.ActivityLogRepository;
import taskflow.repository.TaskRepository;
import taskflow.repository.TaskSpecification;
import taskflow.repository.UserRepository;

import java.time.LocalDateTime;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ActivityLogRepository  activityLogRepository;

    private static final Logger logger =
            LogManager.getLogger(TaskService.class);

    public TaskService(TaskRepository taskRepository, UserRepository userRepository,
                       ActivityLogRepository activityLogRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.activityLogRepository = activityLogRepository;
    }

    //GET
    public Page<TaskResponse> getTasksByFilters(Integer userId,TaskStatus status,String title, Pageable pageable) {

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
                task.getUser().getUsername(),
                task.getUser().getId()
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

        Task savedTask = taskRepository.save(task);

        String msgLog = "Tarea creada: " + task.getTitle() + " - " + task.getDescription();
        ActivityLog activityLog = new ActivityLog();
          activityLog.setUsername(savedTask.getUser().getUsername());
          activityLog.setAction(ActionType.CREATE_TASK);
          activityLog.setDetails(msgLog);
          activityLog.setCreatedAt(LocalDateTime.now());

          activityLogRepository.save(activityLog);


        logger.info(
                "TASK_CREATED user={} taskId={} title={} status={}",
                username,
                savedTask.getId(),
                savedTask.getTitle(),
                savedTask.getStatus()
        );

        return savedTask;
    }

    //UPDATE
    public TaskResponse update(Integer id, TaskUpdateDTO updatedTask) {

        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));


        existingTask.setTitle(updatedTask.title());
        existingTask.setDescription(updatedTask.description());
        existingTask.setStatus(updatedTask.status());
        existingTask.setDueDate(updatedTask.dueDate());

        User user = userRepository.findById(updatedTask.userId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        existingTask.setUser(user);

        Task savedTask = taskRepository.save(existingTask);

        String msgLog = "Tarea actualizada: " + savedTask.getTitle() + " - " + savedTask.getDescription();
        ActivityLog activityLog = new ActivityLog();
        activityLog.setUsername(savedTask.getUser().getUsername());
        activityLog.setAction(ActionType.UPDATE_TASK);
        activityLog.setDetails(msgLog);
        activityLog.setCreatedAt(LocalDateTime.now());

        activityLogRepository.save(activityLog);

        logger.info(
                "TASK_UPDATED user={} taskId={} title={} status={}",
                user.getUsername(),
                savedTask.getId(),
                savedTask.getTitle(),
                savedTask.getStatus()
        );

        return new TaskResponse(
                savedTask.getId(),
                savedTask.getTitle(),
                savedTask.getDescription(),
                savedTask.getStatus(),
                savedTask.getDueDate(),
                savedTask.getCreatedAt(),
                savedTask.getUser().getUsername(),
                savedTask.getUser().getId()
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

        String msgLog = "Estado de la tarea actualizado: "  + task.getStatus()+ " - " + task.getTitle() + " - " + task.getDescription();
        ActivityLog activityLog = new ActivityLog();
        activityLog.setUsername(task.getUser().getUsername());
        activityLog.setAction(ActionType.UPDATE_STATUS_TASK);
        activityLog.setDetails(msgLog);
        activityLog.setCreatedAt(LocalDateTime.now());

        activityLogRepository.save(activityLog);

        logger.info(
                "TASK STATUS_UPDATED user={} taskId={} title={} status={}",
                user.getUsername(),
                task.getId(),
                task.getTitle(),
                task.getStatus()
        );
    }

    //DELETE
    public void delete(Integer taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new EntityNotFoundException("Task not found");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getName();

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        taskRepository.deleteById(taskId);

        String msgLog = "Tarea eliminada: "  + task.getStatus()+ " - " + task.getTitle() + " - " + task.getDescription();
        ActivityLog activityLog = new ActivityLog();
        activityLog.setUsername(task.getUser().getUsername());
        activityLog.setAction(ActionType.DELETE_TASK);
        activityLog.setDetails(msgLog);
        activityLog.setCreatedAt(LocalDateTime.now());

        activityLogRepository.save(activityLog);

        logger.info(
                "TASK_DELETED user={} taskId={} title={}",
                username,
                taskId,
                task.getTitle()

        );
    }



}
