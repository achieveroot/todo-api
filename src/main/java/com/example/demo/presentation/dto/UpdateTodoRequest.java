package com.example.demo.presentation.dto;

import com.example.demo.domain.DeadlineUpdateType;
import com.example.demo.domain.Priority;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class UpdateTodoRequest {

    private String title;
    private Boolean completed;
    private Priority priority;

    private DeadlineUpdateType deadlineUpdateType;
    private LocalDateTime deadline;

}
