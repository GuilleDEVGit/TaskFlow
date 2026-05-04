package taskflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import taskflow.dto.CreateUserRequest;
import taskflow.dto.UserOptionDTO;
import taskflow.dto.UserResponseDto;
import taskflow.entity.User;
import taskflow.security.JwtAuthenticationFilter;
import taskflow.service.Datos;
import taskflow.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static taskflow.service.Datos.*;

@WebMvcTest(
        controllers = UserController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthenticationFilter.class
        )
)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testGetAll() throws Exception {

        // Arrange
        UserResponseDto user1 = userResponseDto01();
        UserResponseDto user2 = userResponseDto02();

        when(userService.getUsers())
                .thenReturn(List.of(user1, user2));

        // Act & Assert
        mvc.perform(get("/api/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].username").value("Andres"))
                .andExpect(jsonPath("$[1].username").value("Maria"))
                .andExpect(jsonPath("$[0].taskCount").value(3L))
                .andExpect(jsonPath("$[1].taskCount").value(5L));

        verify(userService).getUsers();
    }

    @Test
    @WithMockUser(roles = {"ADMIN","USER"})
    void testGetUserOptions() throws Exception {
        UserOptionDTO user1 = userOptionDTO;
        UserOptionDTO user2 = userOptionDTO2;

        when(userService.getUserOptions())
                .thenReturn(List.of(user1, user2));

        mvc.perform(get("/api/users/options").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[0].username").value("Andres"))
                .andExpect(jsonPath("$[1].username").value("Pepe"));

        verify(userService).getUserOptions();

    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testGetById() throws Exception {

        // Arrange
        User user = crearUsuario().orElseThrow();

        when(userService.getUserById(user.getId()))
                .thenReturn(user);

        // Act & Assert
        mvc.perform(get("/api/users/{id}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.username").value("Andres"));

        verify(userService).getUserById(user.getId());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testCreateUser() throws Exception {

        // Arrange
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("Andres");
        request.setPassword("1234");
        request.setEmail("andres@email.com");
        request.setRole("USER");

        User user = crearUsuario().orElseThrow();

        when(userService.createUser(any(CreateUserRequest.class)))
                .thenReturn(user);

        // Act & Assert
        mvc.perform(post("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("Andres"))
                .andExpect(jsonPath("$.password").value("1234"))
                .andExpect(jsonPath("$.email").value("andres@email.com"))
                .andExpect(jsonPath("$.role").value("USER"));


        verify(userService).createUser(any(CreateUserRequest.class));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testUpdateUser() throws Exception {
        // GIVEN
        Integer userId = 1;

        User inputUser = crearUsuario().orElseThrow();

        User updatedUser = crearUsuario().orElseThrow();
        updatedUser.setUsername("Juan");
        updatedUser.setPassword("5678");

        when(userService.updateUser(eq(userId), any(User.class)))
                .thenReturn(updatedUser);

        // WHEN + THEN
        mvc.perform(put("/api/users/update/{id}", userId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.username").value("Juan"))
                .andExpect(jsonPath("$.password").value("5678"));

        verify(userService).updateUser(eq(userId), any(User.class));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testDeleteUser() throws Exception {
        // GIVEN
        Integer userId = 1;

        doNothing().when(userService).delete(userId);

        // WHEN + THEN
        mvc.perform(delete("/api/users/delete/{id}", userId)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(userService).delete(userId);
    }

}