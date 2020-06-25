package com.freshworks.boot.samples.api.v1.mapper;

import com.freshworks.boot.samples.api.v1.dto.TodoCreateDto;
import com.freshworks.boot.samples.api.v1.dto.TodoDto;
import com.freshworks.boot.samples.common.model.Todo;
import com.freshworks.boot.samples.common.model.Todo.TodoBuilder;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-06-25T00:24:00+0530",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.7 (Oracle Corporation)"
)
@Component
public class TodoMapperImpl implements TodoMapper {

    @Override
    public TodoDto convert(Todo todo) {
        if ( todo == null ) {
            return null;
        }

        TodoDto todoDto = new TodoDto();

        todoDto.setId( todo.getId() );
        todoDto.setTitle( todo.getTitle() );
        todoDto.setCompleted( todo.isCompleted() );

        return todoDto;
    }

    @Override
    public Todo convert(TodoDto todo) {
        if ( todo == null ) {
            return null;
        }

        TodoBuilder todo1 = Todo.builder();

        if ( todo.getId() != null ) {
            todo1.id( todo.getId() );
        }
        todo1.title( todo.getTitle() );
        if ( todo.isCompleted() != null ) {
            todo1.completed( todo.isCompleted() );
        }

        return todo1.build();
    }

    @Override
    public Todo convert(TodoCreateDto todo) {
        if ( todo == null ) {
            return null;
        }

        TodoBuilder todo1 = Todo.builder();

        todo1.title( todo.getTitle() );
        if ( todo.isCompleted() != null ) {
            todo1.completed( todo.isCompleted() );
        }

        return todo1.build();
    }

    @Override
    public List<TodoDto> convert(List<Todo> todos) {
        if ( todos == null ) {
            return null;
        }

        List<TodoDto> list = new ArrayList<TodoDto>( todos.size() );
        for ( Todo todo : todos ) {
            list.add( convert( todo ) );
        }

        return list;
    }
}
