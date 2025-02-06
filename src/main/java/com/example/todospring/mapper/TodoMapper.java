package com.example.todospring.mapper;

import com.example.todospring.dto.TodoDto;
import com.example.todospring.model.Todo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TodoMapper {
    TodoDto toDto(Todo entity);
    Todo toEntity(TodoDto dto);
}