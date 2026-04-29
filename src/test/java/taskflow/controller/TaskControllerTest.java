package taskflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import taskflow.dto.CreateTaskRequest;
import taskflow.dto.TaskResponse;
import taskflow.dto.UpdateTaskStatusRequest;
import taskflow.entity.Task;
import taskflow.entity.TaskStatus;
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
    @WithMockUser(roles = {"USER"})
    void testGetTasks() throws Exception {

        TaskResponse response = crearTaskResponse001();

        Page<TaskResponse> page = new PageImpl<>(List.of(response));

        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());

        when(taskService.getTasksByFilters(
                eq(1),
                eq(TaskStatus.TODO),
                eq("Test"),
                eq(pageable)
        )).thenReturn(page);


        mvc.perform(get("/api/tasks")
                        .param("userId", "1")
                        .param("status", "TODO")
                        .param("title", "Test")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andDo(print())
               .andExpect(jsonPath("$.content[0].title").value("Test task"))
               .andExpect(jsonPath("$.content[0].username").value("testuser"));

        verify(taskService).getTasksByFilters(1, TaskStatus.TODO, "Test",  pageable);
    }


    @Test
    @WithMockUser(username = "Andres")
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

        // GIVEN
        Task updatedTask = new Task();
        updatedTask.setTitle("Updated task");
        updatedTask.setDescription("Updated desc");
        updatedTask.setStatus(TaskStatus.DONE);

        TaskResponse response = crearTaskResponse001();

        when(taskService.update(eq(1), any(Task.class)))
                .thenReturn(response);

        // WHEN + THEN
        mvc.perform(put("/api/tasks/update/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test task"))
                .andExpect(jsonPath("$.status").value("TODO"));

        verify(taskService).update(eq(1), any(Task.class));
    }

    @Test
    @WithMockUser()
    void testDeleteTask() throws Exception {

        Integer taskId = 1;

        // Act & Assert
        mvc.perform(delete("/api/tasks/{id}", taskId)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(taskService).delete(taskId);
    }

    @Test
    @WithMockUser(username = "Andres")
    void testUpdateStatus() throws Exception {

        // Arrange
        Integer taskId = 1;

        UpdateTaskStatusRequest request = new UpdateTaskStatusRequest();
        request.setStatus(TaskStatus.DONE);

        // Act & Assert
        mvc.perform(patch("/api/tasks/{id}/status", taskId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(taskService).updateStatus(
                eq(taskId),
                any(UpdateTaskStatusRequest.class),
                any(Authentication.class)
        );
    }
}