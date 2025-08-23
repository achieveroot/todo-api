package com.example.demo.presentation;

import com.example.demo.common.ErrorCode;
import com.example.demo.common.ProblemDetailFactory;
import com.example.demo.domain.TodoNotFoundException;
import com.example.demo.domain.TodoTitleBlankException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleException(MethodArgumentNotValidException ex) {
        return ProblemDetailFactory.create(ErrorCode.INVALID_INPUT);
    }

    @ExceptionHandler(TodoNotFoundException.class)
    public ProblemDetail handleException(TodoNotFoundException ex) {
        return ProblemDetailFactory.create(ErrorCode.TODO_NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleException(MethodArgumentTypeMismatchException ex) {
        String param = ex.getName();
        String value = String.valueOf(ex.getValue());
        String expected = Objects.requireNonNull(ex.getRequiredType()).getSimpleName();

        String detail = String.format("%s 파라미터 형식이 잘못되었습니다. 전달값=%s, 기대타입=%s", param, value, expected);

        return ProblemDetailFactory.create(ErrorCode.TYPE_MISMATCH, detail);
    }

    @ExceptionHandler(TodoTitleBlankException.class)
    public ProblemDetail handleException(TodoTitleBlankException ex) {
        return ProblemDetailFactory.create(ErrorCode.TODO_TITLE_BLANK);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpectedException(Exception ex) {
        log.error("Exception 예외 발생", ex);
        return ProblemDetailFactory.create(ErrorCode.INTERNAL_SERVER_ERROR);
    }

}
