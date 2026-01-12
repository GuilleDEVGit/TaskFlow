package taskflow.service;

import org.springframework.stereotype.Service;
import taskflow.dto.CreateTaskRequest;
import taskflow.entity.Task;
import taskflow.repository.TaskRepository;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    public Task createTask(CreateTaskRequest request) {
        // ⚠️ TEMPORAL: user hardcodeado
        Integer userId = 1;

        Task task = new Task(
                request.getTitle(),
                request.getDescription(),
                request.getDueDate(),
                userId
        );

        return taskRepository.save(task);
    }

}
