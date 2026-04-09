package taskflow.service;

import taskflow.dto.CreateTaskRequest;
import taskflow.dto.CreateUserRequest;
import taskflow.dto.UserResponseDto;
import taskflow.entity.Role;
import taskflow.entity.Task;
import taskflow.entity.TaskStatus;
import taskflow.entity.User;

import java.time.LocalDateTime;
import java.util.Optional;

public class Datos {
    public static Optional<Task> crearTarea001(){
        return Optional.of(new Task(1,"Tarea 1", "Descripcion de tarea 1", LocalDateTime.now(),
                TaskStatus.TODO,1));
    }

    public static Optional<Task> crearTarea002(){
        return Optional.of(new Task(2,"Tarea 2", "Descripcion de tarea 2", LocalDateTime.now(),
                TaskStatus.TODO,2));
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

}
