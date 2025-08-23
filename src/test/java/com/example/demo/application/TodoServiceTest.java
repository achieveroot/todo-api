package com.example.demo.application;

import com.example.demo.IntegrationTestSupport;
import com.example.demo.domain.Priority;
import com.example.demo.domain.Todo;
import com.example.demo.domain.TodoNotFoundException;
import com.example.demo.infrastructure.TodoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class TodoServiceTest extends IntegrationTestSupport {

    @Autowired
    private TodoService todoService;

    @Autowired
    private TodoRepository todoRepository;

    @AfterEach
    void tearDown() {
        todoRepository.deleteAllInBatch();
    }

    @DisplayName("신규 Todo를 생성한다. 기본값은 completed=false, priority=NONE, deadline=null 이다.")
    @Test
    void createTodo() {
        // given
        String title = "할일 1";

        // when
        Todo saved = todoService.createTodo(title);

        // then
        List<Todo> todos = todoRepository.findAll();
        assertThat(todos).hasSize(1)
                .extracting("title", "completed", "priority", "deadline")
                .containsExactly(
                        tuple("할일 1", false, Priority.NONE, null)
                );
        assertThat(saved.getPublicId()).isNotNull();
        assertThat(todos.get(0).getPublicId()).isNotNull();
    }

    @DisplayName("여러 Todo를 저장하고 전체 목록을 조회한다.")
    @Test
    void getTodos() {
        // given
        todoService.createTodo("할일 1");
        todoService.createTodo("할일 2");
        todoService.createTodo("할일 3");

        // when
        List<Todo> todos = todoService.getTodos();

        // then
        assertThat(todos).hasSize(3)
                .extracting("title", "completed")
                .containsExactlyInAnyOrder(
                        tuple("할일 1", false),
                        tuple("할일 2", false),
                        tuple("할일 3", false)
                );
    }

    @DisplayName("publicId로 단건 Todo를 조회한다.")
    @Test
    void getTodo() {
        // given
        Todo saved = todoService.createTodo("할일 1");
        UUID publicId = saved.getPublicId();

        // when
        Todo found = todoService.getTodo(publicId);

        // then
        assertThat(found)
                .extracting("publicId", "title", "completed")
                .containsExactly(publicId, "할일 1", false);
    }

    @DisplayName("존재하지 않는 publicId 조회 시 TodoNotFoundException이 발생한다.")
    @Test
    void getTodo_notFound() {
        // given
        UUID unknown = UUID.randomUUID();

        // when & then
        assertThatThrownBy(() -> todoService.getTodo(unknown))
                .isInstanceOf(TodoNotFoundException.class);
    }

    @DisplayName("publicId로 Todo를 삭제하면 제목을 반환하고 실제로 삭제된다.")
    @Test
    void deleteTodo() {
        // given
        Todo t1 = todoService.createTodo("할일 1(타겟)");
        Todo t2 = todoService.createTodo("할일 2");

        // when
        String deletedTitle = todoService.deleteTodo(t1.getPublicId());

        // then
        assertThat(deletedTitle).isEqualTo("할일 1(타겟)");
        assertThat(todoRepository.findById(t1.getId())).isEmpty();
        assertThat(todoRepository.findAll()).hasSize(1)
                .extracting("title")
                .containsExactly("할일 2");
    }

    @DisplayName("존재하지 않는 publicId 삭제 시 TodoNotFoundException이 발생한다.")
    @Test
    void deleteTodo_notFound() {
        // given
        UUID unknown = UUID.randomUUID();

        // when & then
        assertThatThrownBy(() -> todoService.deleteTodo(unknown))
                .isInstanceOf(TodoNotFoundException.class);
    }

    @DisplayName("완료인 Todo들을 일괄 미완료로 변경한다.")
    @Test
    void resetCompletedAll_changes_true_to_false() {
        // given
        Todo a = Todo.of("할일 1");
        a.complete();
        Todo b = Todo.of("할일 2");
        b.complete();
        Todo c = Todo.of("할일 3");
        todoRepository.save(a);
        todoRepository.save(b);
        todoRepository.save(c);

        // when
        int changed = todoService.resetCompletedTodo();

        // then
        assertThat(changed).isEqualTo(2);
        assertThat(todoRepository.findAll())
                .extracting(todo -> todo.isCompleted())
                .containsOnly(false);
    }
}
