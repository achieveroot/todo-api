package com.example.demo.application.update;

import com.example.demo.domain.Todo;
import com.example.demo.presentation.dto.UpdateTodoRequest;

public interface UpdateOperation {

    boolean supports(UpdateTodoRequest request);

    void apply(Todo todo, UpdateTodoRequest request);

}
