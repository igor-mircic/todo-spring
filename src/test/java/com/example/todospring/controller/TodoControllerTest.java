package com.example.todospring.controller;

import com.example.todospring.dto.TodoDto;
import com.example.todospring.mapper.TodoMapper;
import com.example.todospring.model.Todo;
import com.example.todospring.service.TodoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoController.class)
class TodoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    @MockBean
    private TodoMapper todoMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Todo todo;
    private TodoDto todoDto;
    
    @BeforeEach
    void setUp() {
        todo = new Todo();
        todo.setId(1L);
        todo.setTitle("Test Todo");
        
        todoDto = new TodoDto();
        todoDto.setId(1L);
        todoDto.setTitle("Test Todo");
    }

    @Test
    void getAllTodos_ShouldReturnTodoList() throws Exception {
        Page<Todo> todoPage = new PageImpl<>(Arrays.asList(todo));
        when(todoService.getAllTodos(any(Pageable.class))).thenReturn(todoPage);
        when(todoMapper.toDto(any(Todo.class))).thenReturn(todoDto);

        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Test Todo"))
                .andExpect(jsonPath("$.totalElements").exists())
                .andExpect(jsonPath("$.totalPages").exists());
    }

    @Test
    void getAllTodos_WithPagination_ShouldReturnPagedTodoList() throws Exception {
        int pageSize = 10;
        Page<Todo> todoPage = new PageImpl<>(Arrays.asList(todo), PageRequest.of(0, pageSize), 1);
        when(todoService.getAllTodos(any(Pageable.class))).thenReturn(todoPage);
        when(todoMapper.toDto(any(Todo.class))).thenReturn(todoDto);

        mockMvc.perform(get("/api/todos?page=0&size=" + pageSize + "&sortBy=title"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Test Todo"))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(pageSize))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    void getTodoById_WhenTodoExists_ShouldReturnTodo() throws Exception {
        when(todoService.getTodoById(1L)).thenReturn(Optional.of(todo));
        when(todoMapper.toDto(todo)).thenReturn(todoDto);

        mockMvc.perform(get("/api/todos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Todo"));
    }

    @Test
    void getTodoById_WhenTodoDoesNotExist_ShouldReturn404() throws Exception {
        when(todoService.getTodoById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/todos/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTodo_ShouldReturnCreatedTodo() throws Exception {
        TodoDto inputDto = new TodoDto();
        inputDto.setTitle("New Todo");
        
        Todo newEntity = new Todo();
        newEntity.setTitle("New Todo");
        
        when(todoMapper.toEntity(any(TodoDto.class))).thenReturn(newEntity);
        when(todoService.createTodo(any(Todo.class))).thenReturn(todo);
        when(todoMapper.toDto(todo)).thenReturn(todoDto);

        mockMvc.perform(post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Todo"));
    }

    @Test
    void updateTodo_WhenTodoExists_ShouldReturnUpdatedTodo() throws Exception {
        TodoDto inputDto = new TodoDto();
        inputDto.setTitle("Updated Todo");
        
        Todo updateEntity = new Todo();
        updateEntity.setTitle("Updated Todo");
        
        Todo updatedTodo = new Todo();
        updatedTodo.setId(1L);
        updatedTodo.setTitle("Updated Todo");
        
        TodoDto updatedDto = new TodoDto();
        updatedDto.setId(1L);
        updatedDto.setTitle("Updated Todo");
        
        when(todoMapper.toEntity(any(TodoDto.class))).thenReturn(updateEntity);
        when(todoService.updateTodo(any(Long.class), any(Todo.class))).thenReturn(updatedTodo);
        when(todoMapper.toDto(updatedTodo)).thenReturn(updatedDto);

        mockMvc.perform(put("/api/todos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Updated Todo"));
    }

    @Test
    void deleteTodo_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/todos/1"))
                .andExpect(status().isNoContent());
    }
}