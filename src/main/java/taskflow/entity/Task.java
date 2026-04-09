package taskflow.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "dueDate", nullable = false)
    private LocalDateTime dueDate;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    public Task() {
    }

    public Task(int id, String title, String description, LocalDateTime dueDate, TaskStatus status,Integer userId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
        this.userId = userId;
        this.createdAt = LocalDateTime.now(); // 🔑 default

    }

    public Task(String title, String description, LocalDateTime dueDate, TaskStatus status,Integer userId) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
        this.userId = userId;
        this.createdAt = LocalDateTime.now(); // 🔑 default

    }
}


