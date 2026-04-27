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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Task() {
    }

    public Task(int id, String title, String description, LocalDateTime dueDate, TaskStatus status,User user) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.user = user;

    }

    public Task(String title, String description, LocalDateTime dueDate, TaskStatus status,User user) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.user = user;

    }
}


