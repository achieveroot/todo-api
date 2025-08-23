package com.example.demo.application.update;

import com.example.demo.domain.DeadlineUpdateType;
import com.example.demo.domain.Todo;
import com.example.demo.presentation.dto.UpdateTodoRequest;
import org.springframework.stereotype.Component;

@Component
public class DeadlineUpdateOperation implements UpdateOperation {

    @Override
    public boolean supports(UpdateTodoRequest request) {
        return request.getDeadlineUpdateType() != null;
    }

    @Override
    public void apply(Todo todo, UpdateTodoRequest request) {
        DeadlineUpdateType updateType = request.getDeadlineUpdateType();
        updateType.apply(todo, request.getDeadline());
    }
}