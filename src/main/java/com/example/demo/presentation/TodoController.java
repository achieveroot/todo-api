package com.example.demo.presentation;

import com.example.demo.application.TodoService;
import com.example.demo.domain.Todo;
import com.example.demo.presentation.dto.CreateTodoRequest;
import com.example.demo.presentation.dto.TodoResponse;
import com.example.demo.presentation.dto.UpdateTodoRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    public ResponseEntity<TodoResponse> createTodo(@RequestBody @Valid CreateTodoRequest request) {

        Todo savedTodo = todoService.createTodo(request.getTitle());

        URI uri = URI.create("/todos/" + savedTodo.getPublicId());
        TodoResponse response = TodoResponse.from(savedTodo);

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TodoResponse>> getTodos() {
        List<Todo> todos = todoService.getTodos();

        List<TodoResponse> responses = todos.stream()
                .map(todo -> TodoResponse.from(todo))
                .toList();

        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{publicId}")
    public ResponseEntity<String> deleteTodo(@PathVariable UUID publicId) {
        String deletedTitle = todoService.deleteTodo(publicId);

        return ResponseEntity.ok(deletedTitle + "이(가) 삭제되었습니다.");
    }

    @PatchMapping("/{publicId}")
    public ResponseEntity<TodoResponse> updateTodo(@PathVariable UUID publicId, @RequestBody UpdateTodoRequest request) {
        todoService.updateTodo(publicId, request);

        Todo updatedTodo = todoService.getTodo(publicId);

        return ResponseEntity.ok(TodoResponse.from(updatedTodo));
    }

    @PostMapping("/reset-completed")
    public ResponseEntity<String> resetCompletedTodo() {
        int result = todoService.resetCompletedTodo();
        return ResponseEntity.ok(result + "건이 초기화 되었습니다.");
    }
}
