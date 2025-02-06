package com.example.todospring.mapper;

import com.example.todospring.dto.TodoDto;
import com.example.todospring.model.Todo;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-05T23:21:25+0100",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.41.0.z20250115-2156, environment: Java 21.0.5 (Eclipse Adoptium)"
)
@Component
public class TodoMapperImpl implements TodoMapper {

    @Override
    public TodoDto toDto(Todo entity) {
        if ( entity == null ) {
            return null;
        }

        TodoDto todoDto = new TodoDto();

        todoDto.setCompleted( entity.isCompleted() );
        todoDto.setDescription( entity.getDescription() );
        todoDto.setId( entity.getId() );
        todoDto.setTitle( entity.getTitle() );

        return todoDto;
    }

    @Override
    public Todo toEntity(TodoDto dto) {
        if ( dto == null ) {
            return null;
        }

        Todo todo = new Todo();

        todo.setCompleted( dto.isCompleted() );
        todo.setDescription( dto.getDescription() );
        todo.setId( dto.getId() );
        todo.setTitle( dto.getTitle() );

        return todo;
    }
}
