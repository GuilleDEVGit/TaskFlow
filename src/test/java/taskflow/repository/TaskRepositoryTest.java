package taskflow.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import taskflow.entity.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static taskflow.service.Datos.*;

@DataJpaTest
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void testFindTasksByUserId() {
        // GIVEN
        Task task1 = crearTareaJPA001();
        Task task2 = crearTareaJPA002();
        Task task3 = crearTareaJPA003();

        taskRepository.save(task1);
        taskRepository.save(task2);
        taskRepository.save(task3);

        // WHEN
        List<Task> result = taskRepository.findAllByUserId(1);

        // THEN
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getUserId());
        assertEquals("Tarea 1 JPA", result.get(0).getTitle());
        assertEquals("Tarea 2 JPA", result.get(1).getTitle());
    }

    @Test
    void testFindTasksByUserIdException() {
        List<Task> result = taskRepository.findAllByUserId(999);

        assertTrue(result.isEmpty());
    }

}