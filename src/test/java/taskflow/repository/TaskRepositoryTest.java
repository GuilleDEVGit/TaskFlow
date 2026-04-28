package taskflow.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import taskflow.entity.Role;
import taskflow.entity.Task;
import taskflow.entity.TaskStatus;
import taskflow.entity.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static taskflow.service.Datos.*;

@DataJpaTest
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testFindAllByUserId() {
        // GIVEN
        User user = createUser();
        entityManager.persist(user);

        Task task1 = entityManager.persistAndFlush(
                createTask(user, "Task 1")
        );

        Task task2 = entityManager.persistAndFlush(
                createTask(user, "Task 2")
        );

        entityManager.flush();

        Pageable pageable = PageRequest.of(0, 10);

        // WHEN
        Page<Task> result = taskRepository.findAllByUserId(user.getId(), pageable);

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent())
                .extracting(Task::getTitle)
                .containsExactlyInAnyOrder("Task 1", "Task 2");
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