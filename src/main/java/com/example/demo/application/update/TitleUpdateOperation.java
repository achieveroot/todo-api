package com.example.demo.application.update;

import com.example.demo.domain.Todo;
import com.example.demo.domain.TodoTitleBlankException;
import com.example.demo.presentation.dto.UpdateTodoRequest;
import org.springframework.stereotype.Component;

@Component
public class TitleUpdateOperation implements UpdateOperation {

    @Override
    public boolean supports(UpdateTodoRequest request) {
        return request.getTitle() != null;
    }

    @Override
    public void apply(Todo todo, UpdateTodoRequest request) {
        String title = request.getTitle().trim();
        if (title.isBlank()) {
            throw new TodoTitleBlankException();
        }
        todo.updateTitle(title);
    }
}
