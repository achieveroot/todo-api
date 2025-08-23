package com.example.demo.domain;

import java.time.LocalDateTime;

public enum DeadlineUpdateType {

    SET {
        @Override
        public void apply(Todo todo, LocalDateTime deadline) {
            todo.updateDeadline(deadline);
        }
    },
    CLEAR {
        @Override
        public void apply(Todo todo, LocalDateTime deadline) {
            todo.updateDeadline(null);
        }
    };

    public abstract void apply(Todo todo, LocalDateTime deadline);
}
