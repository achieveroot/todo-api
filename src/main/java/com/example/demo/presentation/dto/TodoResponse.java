package com.example.demo.presentation.dto;

import com.example.demo.domain.Priority;
import com.example.demo.domain.Todo;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
public class TodoResponse {

    private UUID publicId;
    private String title;
    private boolean completed;
    private Priority priority;
    private LocalDateTime deadline;

    public static TodoResponse from(Todo todo) {
        return TodoResponse.builder()
                .publicId(todo.getPublicId())
                .title(todo.getTitle())
                .completed(todo.isCompleted())
                .priority(todo.getPriority())
                .deadline(todo.getDeadline())
                .build();
    }
}
