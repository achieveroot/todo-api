package com.example.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "todos")
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Todo {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, updatable = false, columnDefinition = "BINARY(16)")
    private UUID publicId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private boolean completed;

    @Enumerated(STRING)
    private Priority priority;

    private LocalDateTime deadline;

    private Todo(String title) {
        this.publicId = UUID.randomUUID();
        this.title = title;
        this.completed = false;
        this.priority = Priority.NONE;
        this.deadline = null;
    }

    public static Todo of(String title) {
        return new Todo(title);
    }

    public void updatePriority(Priority priority) {
        this.priority = priority;
    }

    public void updateDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void complete() {
        this.completed = true;
    }

    public void uncomplete() {
        this.completed = false;
    }
}
