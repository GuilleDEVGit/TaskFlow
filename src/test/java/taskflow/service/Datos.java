package taskflow.service;

import taskflow.dto.CreateTaskRequest;
import taskflow.dto.CreateUserRequest;
import taskflow.dto.TaskResponse;
import taskflow.dto.UserResponseDto;
import taskflow.entity.Role;
import taskflow.entity.Task;
import taskflow.entity.TaskStatus;
import taskflow.entity.User;

import java.time.LocalDateTime;
import java.util.Optional;

public class Datos {

    public static User user1 = new User(1,"Andres","andres@email.com","1234" ,Role.USER);
    public static User user2 = new User(2,"Pepe","pepe@email.com","1234" ,Role.USER);

    public static Optional<Task> crearTarea001(){
        return Optional.of(new Task(1,"Tarea 1", "Descripcion de tarea 1", LocalDateTime.now(),
                TaskStatus.TODO,user1));
    }

    public static TaskResponse crearTaskResponse001(){
        return new TaskResponse(1,"Test task", "desc", TaskStatus.TODO,null,null,
                "testuser");
    }

    public static Optional<Task> crearTarea002(){
        return Optional.of(new Task(2,"Tarea 2", "Descripcion de tarea 2", LocalDateTime.now(),
                TaskStatus.TODO,user2));
    }

    public static Task crearTareaJPA001(){
        return new Task("Tarea 1 JPA", "Descripcion de tarea 1", LocalDateTime.now(),
                TaskStatus.TODO,user1);
    }

    public static Task crearTareaJPA002(){
        return new Task("Tarea 2 JPA", "Descripcion de tarea 2", LocalDateTime.now(),
                TaskStatus.TODO,user1);
    }

    public static Task crearTareaJPA003(){
        return new Task("Tarea 3 JPA", "Descripcion de tarea 3", LocalDateTime.now(),
                TaskStatus.TODO,user2);
    }

    public static CreateTaskRequest crearTareaNueva001(){
        return new CreateTaskRequest("Tarea nueva","Descripcion",LocalDateTime.now(),TaskStatus.TODO);
    }

    public static Optional<User> crearUsuario(){
        return Optional.of(new User(1,"Andres","andres@email.com","1234" ,Role.USER));
    }

    public static CreateUserRequest crearUsuarioRequest(){
        return new CreateUserRequest("Andres","1234","andres@test.com","USER");
    }

    public static UserResponseDto userResponseDto01(){
        return new UserResponseDto(1,"Andres","andres@email.com",Role.USER,
                LocalDateTime.now(),3L
        );
    }

    public static UserResponseDto userResponseDto02(){
        return new UserResponseDto(2,"Maria","maria@email.com",Role.ADMIN,
                LocalDateTime.now(),5L
        );
    }

    public static User createUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("testpassword");
        user.setEmail("user1@email.com");
        user.setRole(Role.USER);
        return user;
    }

    public static Task createTask(User user, String title) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(title);
        task.setCreatedAt(LocalDateTime.now());
        task.setDueDate(LocalDateTime.now());
        task.setUser(user);
        task.setStatus(TaskStatus.DONE);
        return task;
    }

}
