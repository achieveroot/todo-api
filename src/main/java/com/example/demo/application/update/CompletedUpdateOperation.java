package com.example.demo.application.update;

import com.example.demo.domain.Todo;
import com.example.demo.presentation.dto.UpdateTodoRequest;
import org.springframework.stereotype.Component;

@Component
public class CompletedUpdateOperation implements UpdateOperation {
    @Override
    public boolean supports(UpdateTodoRequest request) {
        return request.getCompleted() != null;
    }

    @Override
    public void apply(Todo todo, UpdateTodoRequest request) {
        if (Boolean.TRUE.equals(request.getCompleted())) {
            todo.complete();
            return;
        }
        todo.uncomplete();
    }
}
