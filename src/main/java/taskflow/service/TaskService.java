package taskflow.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import taskflow.dto.CreateTaskRequest;
import taskflow.dto.TaskResponse;
import taskflow.dto.UpdateTaskStatusRequest;
import taskflow.entity.Task;
import taskflow.entity.User;
import taskflow.repository.TaskRepository;
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

    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    public List<Task> getMyTasks(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return taskRepository.findAllByUserId(Integer.parseInt(user.getId().toString()));
    }

    public Task createTask(CreateTaskRequest request, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));


        Task task = new Task(
                request.getTitle(),
                request.getDescription(),
                request.getDueDate(),
                request.getStatus(),
                Integer.parseInt(user.getId().toString())
        );

        return taskRepository.save(task);
    }

    public Task update(Integer id, Task updatedTask) {

        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setStatus(updatedTask.getStatus());
        existingTask.setDueDate(updatedTask.getDueDate());

        return taskRepository.save(existingTask);
    }


    public void delete(Integer id) {
        if (!taskRepository.existsById(id)) {
            throw new EntityNotFoundException("Task not found");
        }
        taskRepository.deleteById(id);
    }


    public List<TaskResponse> getTasksByUsername(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Task> tasks =
                taskRepository.findAllByUserId(user.getId());

        return tasks.stream()
                .map(task -> new TaskResponse(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getStatus(),
                        task.getDueDate(),
                        task.getCreatedAt()
                ))
                .toList();

    }

    public void updateStatus(Integer taskId,UpdateTaskStatusRequest request,Authentication auth) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow();

        boolean isOwner = task.getUserId().equals(user.getId());

        task.setStatus(request.getStatus());
        taskRepository.save(task);
    }

}
