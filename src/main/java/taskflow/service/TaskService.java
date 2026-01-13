package taskflow.service;

import org.springframework.stereotype.Service;
import taskflow.dto.CreateTaskRequest;
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
                Integer.parseInt(user.getId().toString())
        );

        return taskRepository.save(task);
    }

}
