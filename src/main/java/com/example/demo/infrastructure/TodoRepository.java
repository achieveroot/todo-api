package com.example.demo.infrastructure;

import com.example.demo.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    Optional<Todo> findByPublicId(UUID publicId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Todo t set t.completed = false where t.completed = true")
    int resetCompletedAll();

}
