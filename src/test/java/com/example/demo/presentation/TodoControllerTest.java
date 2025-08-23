package com.example.demo.presentation;

import com.example.demo.ControllerTestSupport;
import com.example.demo.domain.Priority;
import com.example.demo.domain.Todo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TodoControllerTest extends ControllerTestSupport {

    @DisplayName("신규 Todo를 등록하면 201 Created와 Location 헤더, 응답 바디를 반환한다.")
    @Test
    void createTodo() throws Exception {
        // given
        String title = "할일 1";
        String request = """
            { "title": "할일 1" }
        """;

        Todo savedTodo = Todo.of(title);
        when(todoService.createTodo(title)).thenReturn(savedTodo);

        // when & then
        mockMvc.perform(
                        post("/todos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/todos/" + savedTodo.getPublicId()))
                .andExpect(jsonPath("$.publicId").value(savedTodo.getPublicId().toString()))
                .andExpect(jsonPath("$.title").value("할일 1"))
                .andExpect(jsonPath("$.completed").value(false))
                .andExpect(jsonPath("$.priority").value(Priority.NONE.name()))
                .andExpect(jsonPath("$.deadline").doesNotExist());
    }

    @DisplayName("신규 Todo 등록 시 제목이 공백이면 400 Bad Request가 반환된다.")
    @Test
    void createTodo_blankTitle() throws Exception {
        // given
        String request = """
            { "title": "  " }
        """;

        // when // then
        mockMvc.perform(
                        post("/todos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request)
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Todo 목록을 조회하면 200 OK와 배열 응답을 반환한다.")
    @Test
    void getTodos() throws Exception {
        // given
        Todo todo1 = Todo.of("할일 1");
        Todo todo2 = Todo.of("할일 2");
        todo2.complete();
        when(todoService.getTodos()).thenReturn(List.of(todo1, todo2));

        // when & then
        mockMvc.perform(get("/todos"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("할일 1"))
                .andExpect(jsonPath("$[0].completed").value(false))
                .andExpect(jsonPath("$[1].title").value("할일 2"))
                .andExpect(jsonPath("$[1].completed").value(true));

    }

    @DisplayName("publicId로 Todo를 삭제하면 200 OK와 안내 문구를 반환한다.")
    @Test
    void deleteTodo() throws Exception {
        // given
        UUID publicId = UUID.randomUUID();
        when(todoService.deleteTodo(publicId)).thenReturn("할일 1");

        // when & then
        mockMvc.perform(delete("/todos/{publicId}", publicId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("할일 1이(가) 삭제되었습니다."));
    }

    @DisplayName("삭제 요청의 publicId가 UUID 형식이 아니면 400 Bad Request가 반환된다.")
    @Test
    void deleteTodo_typeMismatch() throws Exception {
        mockMvc.perform(delete("/todos/{publicId}", "invalid"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Todo를 부분 업데이트하면 200 OK와 업데이트된 Todo를 반환한다.")
    @Test
    void updateTodo() throws Exception {
        // given
        UUID publicId = UUID.randomUUID();
        String request = """
            { "title": "할일 1" }
        """;

        doNothing().when(todoService).updateTodo(eq(publicId), any());
        Todo updated = Todo.of("할일 1");
        when(todoService.getTodo(publicId)).thenReturn(updated);

        // when & then
        mockMvc.perform(
                        patch("/todos/{publicId}", publicId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("할일 1"))
                .andExpect(jsonPath("$.completed").value(false))
                .andExpect(jsonPath("$.priority").value(Priority.NONE.name()));
    }

    @DisplayName("수동 트리거 리셋 API는 200과 변경 건수를 반환한다.")
    @Test
    void resetCompleted() throws Exception {
        // given
        int result = 3;
        when(todoService.resetCompletedTodo()).thenReturn(result);

        // when & then
        mockMvc.perform(post("/todos/reset-completed"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(result + "건이 초기화 되었습니다."));
    }

}
