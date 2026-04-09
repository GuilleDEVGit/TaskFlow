package taskflow.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import taskflow.dto.CreateTaskRequest;
import taskflow.dto.TaskResponse;
import taskflow.dto.UpdateTaskStatusRequest;
import taskflow.entity.Task;
import taskflow.entity.TaskStatus;
import taskflow.entity.User;
import taskflow.repository.TaskRepository;
import taskflow.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static taskflow.service.Datos.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    TaskRepository taskRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    TaskService taskService;

    @Test
    void testGetTasks() {
        List<Task> datos = Arrays.asList(crearTarea001().orElseThrow(), crearTarea002().orElseThrow());
        when(taskRepository.findAll()).thenReturn(datos);

        List<Task> tareas = taskService.getTasks();

        assertFalse(tareas.isEmpty());
        assertEquals(2, tareas.size());
        assertEquals("Tarea 1", tareas.get(0).getTitle());

        verify(taskRepository).findAll();
    }

    @Test
    void testGetMyTasks() {

        User user = crearUsuario().orElseThrow();
        when(userRepository.findByUsername("Andres")).thenReturn(Optional.of(user));

        List<Task> datos = Arrays.asList(crearTarea001().orElseThrow());

        when(taskRepository.findAllByUserId(user.getId())).thenReturn(datos);
        List<Task> tareas = taskService.getMyTasks("Andres");

        assertFalse(tareas.isEmpty());
        assertEquals(1, tareas.size());
        assertEquals("Tarea 1", tareas.get(0).getTitle());

        verify(taskRepository).findAllByUserId(user.getId());
        verify(userRepository).findByUsername("Andres");
    }

    @Test
    void testCreateTask() {
        User user = crearUsuario().orElseThrow();
        CreateTaskRequest request = crearTareaNueva001();

        when(userRepository.findByUsername("Andres")).thenReturn(Optional.of(user));

        when(taskRepository.save(any(Task.class))).then(invocation -> {
            Task taskCreada = invocation.getArgument(0);
            return taskCreada;
        });

        Task result = taskService.createTask(request,"Andres");

        assertEquals(request.getTitle(), result.getTitle());
        assertEquals(request.getDescription(), result.getDescription());
        assertEquals(request.getDueDate(), result.getDueDate());
        assertEquals(request.getStatus(), result.getStatus());
        assertEquals(user.getId(), result.getUserId());

        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void testUpdateTask() {
        // Arrange
        Task existingTask = crearTarea001().orElseThrow();

        Task updatedTask = new Task();
        updatedTask.setTitle("TareaUpdated 1");
        updatedTask.setDescription("Nueva Descripcion");
        updatedTask.setStatus(TaskStatus.DONE);
        updatedTask.setDueDate(LocalDateTime.now());

        when(taskRepository.findById(existingTask.getId()))
                .thenReturn(Optional.of(existingTask));

        when(taskRepository.save(any(Task.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Task result = taskService.update(existingTask.getId(), updatedTask);

        // Assert
        assertEquals("TareaUpdated 1", result.getTitle());
        assertEquals("Nueva Descripcion", result.getDescription());
        assertEquals(TaskStatus.DONE, result.getStatus());

        verify(taskRepository).findById(existingTask.getId());
        verify(taskRepository).save(existingTask);
    }

    @Test
    void testUpdateStatus(){
        // Arrange
        Task task = crearTarea001().orElseThrow();
        User user = crearUsuario().orElseThrow();

        UpdateTaskStatusRequest request = new UpdateTaskStatusRequest();
        request.setStatus(TaskStatus.DONE);

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("Andres");

        when(taskRepository.findById(task.getId()))
                .thenReturn(Optional.of(task));

        when(userRepository.findByUsername("Andres"))
                .thenReturn(Optional.of(user));

        when(taskRepository.save(any(Task.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        taskService.updateStatus(task.getId(), request, auth);

        // Assert
        assertEquals(TaskStatus.DONE, task.getStatus());

        verify(taskRepository).findById(task.getId());
        verify(userRepository).findByUsername("Andres");
        verify(taskRepository).save(task);

    }

    @Test
    void testDelete() {
        // Arrange
        Integer taskId = 1;

        when(taskRepository.existsById(taskId)).thenReturn(true);

        // Act
        taskService.delete(taskId);

        // Assert
        verify(taskRepository).existsById(taskId);
        verify(taskRepository).deleteById(taskId);
    }

    @Test
    void testDeleteException() {
        // Arrange
        Integer taskId = 1;

        when(taskRepository.existsById(taskId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> {
            taskService.delete(taskId);
        });

        // Assert
        verify(taskRepository).existsById(taskId);
        verify(taskRepository, never()).deleteById(any());
    }

}