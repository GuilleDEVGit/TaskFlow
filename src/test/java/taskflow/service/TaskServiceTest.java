package taskflow.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
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

import static org.assertj.core.api.Assertions.assertThat;
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
    void testGetTasksByFilters() {
        // GIVEN

        Task task = crearTarea001().orElseThrow();

        Page<Task> taskPage = new PageImpl<>(List.of(task));

        when(taskRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(taskPage);

        // WHEN
        Page<TaskResponse> result = taskService.getTasksByFilters(
                1,TaskStatus.TODO,"Test",0,10);

        // THEN
        assertThat(result).hasSize(1);

        TaskResponse response = result.getContent().get(0);

        assertThat(response.getTitle()).isEqualTo("Tarea 1");
        assertThat(response.getUsername()).isEqualTo("Andres");

        verify(taskRepository).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void testCreateTask() {
        User user = crearUsuario().orElseThrow();
        CreateTaskRequest request = crearTareaNueva001();

        when(userRepository.findByUsername("Andres")).thenReturn(Optional.of(user));

        when(taskRepository.save(any(Task.class))).then(invocation -> invocation.<Task>getArgument(0));

        Task result = taskService.createTask(request,"Andres");

        assertEquals(request.getTitle(), result.getTitle());
        assertEquals(request.getDescription(), result.getDescription());
        assertEquals(request.getDueDate(), result.getDueDate());
        assertEquals(request.getStatus(), result.getStatus());
        assertEquals(user.getId(), result.getUser().getId());

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
        TaskResponse result = taskService.update(existingTask.getId(), updatedTask);

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

        assertThrows(EntityNotFoundException.class, () -> taskService.delete(taskId));

        // Assert
        verify(taskRepository).existsById(taskId);
        verify(taskRepository, never()).deleteById(any());
    }

}