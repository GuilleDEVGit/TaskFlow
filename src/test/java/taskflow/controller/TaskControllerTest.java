package taskflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import taskflow.entity.Task;
import taskflow.security.JwtAuthenticationFilter;
import taskflow.service.TaskService;

import java.util.Arrays;
import java.util.List;

import static taskflow.service.Datos.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = TaskController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthenticationFilter.class
        )
)
class TaskControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private TaskService taskService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetTasks() throws Exception {
        // Arrange
        Task task1 = crearTarea001().orElseThrow();
        Task task2 = crearTarea002().orElseThrow();

        List<Task> tasks = Arrays.asList(task1, task2);

        when(taskService.getTasks()).thenReturn(tasks);

        // Act & Assert
        mvc.perform(get("/api/tasks").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].title").value("Tarea 1"))
                .andExpect(jsonPath("$[1].title").value("Tarea 2"));

        verify(taskService).getTasks();
    }
}