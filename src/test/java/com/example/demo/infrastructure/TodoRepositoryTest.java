package com.example.demo.infrastructure;

import com.example.demo.IntegrationTestSupport;
import com.example.demo.domain.Priority;
import com.example.demo.domain.Todo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@Transactional
class TodoRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private TodoRepository todoRepository;

    @DisplayName("여러 Todo를 저장하고 전체 조회한다.")
    @Test
    void findAllTodos() {
        // given
        Todo todo1 = createTodo("할일 1", false, Priority.NONE, null);
        Todo todo2 = createTodo("할일 2", true, Priority.NONE, LocalDateTime.now().plusDays(1).withNano(0));
        Todo todo3 = createTodo("할일 3", false, Priority.NONE, LocalDateTime.now().plusDays(3).withNano(0));
        todoRepository.saveAll(List.of(todo1, todo2, todo3));

        // when
        List<Todo> todos = todoRepository.findAll();

        // then
        assertThat(todos).hasSize(3)
                .extracting("title", "completed")
                .containsExactlyInAnyOrder(
                        tuple("할일 1", false),
                        tuple("할일 2", true),
                        tuple("할일 3", false)
                );
    }

    @DisplayName("publicId로 Todo를 조회한다.")
    @Test
    void findByPublicId() {
        // given
        Todo saved = todoRepository.save(createTodo("할일 1", false, Priority.NONE, null));
        UUID publicId = saved.getPublicId();

        // when
        var found = todoRepository.findByPublicId(publicId);

        // then
        assertThat(found).isPresent();
        assertThat(found.get())
                .extracting("publicId", "title", "completed")
                .containsExactly(publicId, "할일 1", false);
    }

    private Todo createTodo(String title, boolean completed, Priority priority, LocalDateTime deadline) {
        Todo todo = Todo.of(title);
        todo.updatePriority(priority);

        if (deadline != null) {
            todo.updateDeadline(deadline);
        }
        if (completed) {
            todo.complete();
        }

        return todo;
    }
}
