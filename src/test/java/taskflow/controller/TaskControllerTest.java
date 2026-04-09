package taskflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import taskflow.dto.CreateTaskRequest;
import taskflow.entity.Task;
import taskflow.security.JwtAuthenticationFilter;
import taskflow.service.TaskService;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

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

    @Test
    @WithMockUser(username = "Andres", roles = {"USER"})
    void testGetMyTasks() throws Exception {

        // Arrange
        Task task1 = crearTarea001().orElseThrow();
        Task task2 = crearTarea002().orElseThrow();

        List<Task> tasks = Arrays.asList(task1, task2);

        when(taskService.getMyTasks("Andres"))
                .thenReturn(tasks);

        // Act & Assert
        mvc.perform(get("/api/tasks/my"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].title").value("Tarea 1"));

        verify(taskService).getMyTasks("Andres");
    }

    @Test
    @WithMockUser(username = "Andres", roles = {"USER"})
    void testCreateTask() throws Exception {

        // Arrange
        CreateTaskRequest request = crearTareaNueva001();

        Task task = crearTarea001().orElseThrow();

        when(taskService.createTask(any(CreateTaskRequest.class), eq("Andres")))
                .thenReturn(task);

        // Act & Assert
        mvc.perform(post("/api/tasks")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Tarea 1"));

        verify(taskService).createTask(any(CreateTaskRequest.class), eq("Andres"));
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void testUpdateTask() throws Exception {

        // Arrange
        Task existingTask = crearTarea001().orElseThrow();

        Task updatedTask = new Task();
        updatedTask.setTitle("Tarea Updated");
        updatedTask.setDescription("Nueva descripcion Updated");

        when(taskService.update(eq(existingTask.getId()), any(Task.class)))
                .thenReturn(updatedTask);

        // Act & Assert
        mvc.perform(put("/api/tasks/update/{id}", existingTask.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Tarea Updated"))
                .andExpect(jsonPath("$.description").value("Nueva descripcion Updated"));

        verify(taskService).update(eq(existingTask.getId()), any(Task.class));
    }
}