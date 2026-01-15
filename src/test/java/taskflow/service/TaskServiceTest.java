package taskflow.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import taskflow.dto.TaskResponse;
import taskflow.entity.Task;
import taskflow.entity.User;
import taskflow.repository.TaskRepository;
import taskflow.repository.UserRepository;

import java.nio.file.AccessDeniedException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;


    @Test
    void returnsEmptyListWhenUserHasNoTasks() {

        // 1️⃣ Usuario fake
        User user = new User();
        user.setId(1);
        user.setUsername("guillermo");

        // 2️⃣ Mock: el usuario existe
        when(userRepository.findByUsername("guillermo"))
                .thenReturn(Optional.of(user));

        // 3️⃣ Mock: el usuario no tiene tareas
        when(taskRepository.findAllByUserId(1))
                .thenReturn(List.of());

        // 4️⃣ Ejecutamos el método REAL
        List<TaskResponse> result =
                taskService.getTasksByUsername("guillermo");

        // 5️⃣ Comprobación
        assertEquals(0, result.size());
    }
}

