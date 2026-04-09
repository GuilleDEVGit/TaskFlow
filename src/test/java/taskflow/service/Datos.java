package taskflow.service;

import taskflow.dto.CreateTaskRequest;
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
        return Optional.of(new User(1,"Andres","user@email.conm","1234" ,Role.USER));
    }

}
