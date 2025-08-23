package com.example.demo.application.update;

import com.example.demo.domain.Todo;
import com.example.demo.presentation.dto.UpdateTodoRequest;
import org.springframework.stereotype.Component;

@Component
public class PriorityUpdateOperation implements UpdateOperation {

    @Override
    public boolean supports(UpdateTodoRequest request) {
        return request.getPriority() != null;
    }

    @Override
    public void apply(Todo todo, UpdateTodoRequest request) {
        todo.updatePriority(request.getPriority());
    }
}