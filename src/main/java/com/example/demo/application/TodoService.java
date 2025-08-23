package com.example.demo.application;

import com.example.demo.application.update.UpdateOperation;
import com.example.demo.domain.Todo;
import com.example.demo.domain.TodoNotFoundException;
import com.example.demo.infrastructure.TodoRepository;
import com.example.demo.presentation.dto.UpdateTodoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoService {

    private final TodoRepository todoRepository;
    private final List<UpdateOperation> updateOperations;

    @Transactional
    public Todo createTodo(String title) {
        Todo todo = Todo.of(title);
        return todoRepository.save(todo);
    }

    public List<Todo> getTodos() {
        return todoRepository.findAll();
    }

    @Transactional
    public String deleteTodo(UUID publicId) {
        Todo todo = todoRepository.findByPublicId(publicId)
                .orElseThrow(() -> new TodoNotFoundException(publicId));

        todoRepository.delete(todo);
        return todo.getTitle();
    }

    @Transactional
    public void updateTodo(UUID publicId, UpdateTodoRequest request) {
        Todo todo = todoRepository.findByPublicId(publicId)
                .orElseThrow(() -> new TodoNotFoundException(publicId));

        updateOperations.stream()
                .filter(updateOperation -> updateOperation.supports(request))
                .forEach(updateOperation -> updateOperation.apply(todo, request));
    }

    public Todo getTodo(UUID publicId) {
        return todoRepository.findByPublicId(publicId)
                .orElseThrow(() -> new TodoNotFoundException(publicId));
    }

    @Transactional
    public int resetCompletedTodo() {
        return todoRepository.resetCompletedAll();
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    @Transactional
    public void resetCompletedTodoBatch() {
        todoRepository.resetCompletedAll();
    }
}
