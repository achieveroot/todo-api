package com.example.demo.common;

import org.springframework.http.ProblemDetail;

import java.time.LocalDateTime;

public class ProblemDetailFactory {

    public static ProblemDetail create(ErrorCode errorCode, String detail) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(errorCode.getStatus(), detail);
        pd.setTitle(errorCode.getDefaultMessage());
        pd.setProperty("code", errorCode.getCode());
        pd.setProperty("timestamp", LocalDateTime.now().toString());
        return pd;
    }

    public static ProblemDetail create(ErrorCode errorCode) {
        return create(errorCode, errorCode.getDefaultMessage());
    }
}
