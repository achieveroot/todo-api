package com.example.demo.domain;

import java.util.UUID;

public class TodoNotFoundException extends RuntimeException {
    public TodoNotFoundException(UUID publicId) {
        super(publicId + "에 해당하는 Todo를 찾을 수 없습니다.");
    }
}
