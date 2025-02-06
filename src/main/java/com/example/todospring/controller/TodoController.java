package com.example.todospring.controller;

import com.example.todospring.dto.TodoDto;
import com.example.todospring.mapper.TodoMapper;
import com.example.todospring.model.Todo;
import com.example.todospring.service.TodoService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
@Validated
public class TodoController {
    private final TodoService todoService;
    private final TodoMapper todoMapper;
    
    @GetMapping
    public ResponseEntity<Page<TodoDto>> getAllTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Todo> todos = todoService.getAllTodos(pageable);
        Page<TodoDto> todoDtos = todos.map(todoMapper::toDto);
        return ResponseEntity.ok(todoDtos);
    }

    @GetMapping("/{id}")
    public TodoDto getTodoById(@PathVariable Long id) {
        return todoService.getTodoById(id)
                .map(todoMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Todo not found with id: " + id));
    }

    @PostMapping
    public ResponseEntity<TodoDto> createTodo(@Valid @RequestBody TodoDto todoDto) {
        Todo todo = todoMapper.toEntity(todoDto);
        Todo savedTodo = todoService.createTodo(todo);
        return ResponseEntity.status(HttpStatus.CREATED).body(todoMapper.toDto(savedTodo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoDto> updateTodo(@PathVariable Long id, @Valid @RequestBody TodoDto todoDto) {
        Todo todo = todoMapper.toEntity(todoDto);
        Todo updatedTodo = todoService.updateTodo(id, todo);
        return ResponseEntity.ok(todoMapper.toDto(updatedTodo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.noContent().build();
    }
}