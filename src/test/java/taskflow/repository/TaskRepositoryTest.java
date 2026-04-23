package taskflow.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import taskflow.entity.Task;

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

        Pageable pageable = PageRequest.of(0, 10);

        // WHEN
        Page<Task> result = taskRepository.findAllByUserId(1, pageable);

        // THEN
        assertEquals(2, result.getContent().size());
        assertEquals(1, result.getContent().get(0).getUserId());
        assertEquals("Tarea 1 JPA", result.getContent().get(0).getTitle());
    }

    @Test
    void testFindTasksByUserIdException() {

        Pageable pageable = PageRequest.of(0, 10);

        // WHEN
        Page<Task> result = taskRepository.findAllByUserId(999, pageable);

        // THEN
        assertTrue(result.getContent().isEmpty());
    }

}