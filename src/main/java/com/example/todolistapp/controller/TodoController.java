// src/main/java/com/example/todolistapp/controller/TodoController.java
package com.example.todolistapp.controller;

import com.example.todolistapp.model.Todo;
import com.example.todolistapp.model.User;
import com.example.todolistapp.repository.TodoRepository;
import com.example.todolistapp.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TodoController {

    private final TodoRepository todoRepository;
    private final UserService userService;

    public TodoController(TodoRepository todoRepository, UserService userService) {
        this.todoRepository = todoRepository;
        this.userService = userService;
    }

    @GetMapping("/todos")
    public String listTodos(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        User user = userService.findByUsername(username);
        List<Todo> todos = todoRepository.findByUserId(user.getId());
        model.addAttribute("todos", todos);
        model.addAttribute("newTodo", new Todo());
        return "todos";
    }

    @PostMapping("/todos/add")
    public String addTodo(@ModelAttribute("newTodo") Todo todo, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        User user = userService.findByUsername(username);
        todo.setUser(user);
        todoRepository.save(todo);
        return "redirect:/todos";
    }

    @GetMapping("/todos/update/{id}")
    public String updateTodoStatus(@PathVariable Long id) {
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid todo Id:" + id));
        todo.setCompleted(!todo.isCompleted());
        todoRepository.save(todo);
        return "redirect:/todos";
    }

    @GetMapping("/todos/delete/{id}")
    public String deleteTodo(@PathVariable Long id) {
        todoRepository.deleteById(id);
        return "redirect:/todos";
    }
}