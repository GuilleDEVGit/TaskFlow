package taskflow.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import taskflow.dto.CreateTaskRequest;
import taskflow.dto.TaskResponse;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
        List<TaskResponse> tareas = taskService.getTasksByUsername("Andres");

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
}