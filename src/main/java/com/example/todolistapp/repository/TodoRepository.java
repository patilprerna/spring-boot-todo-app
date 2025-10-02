// src/main/java/com/example/todolistapp/repository/TodoRepository.java
package com.example.todolistapp.repository;

import com.example.todolistapp.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByUserId(Long userId);
}