package com.freshworks.boot.samples.sqs_processor.mapper;

import com.freshworks.boot.samples.common.model.Todo;
import com.freshworks.boot.samples.common.model.Todo.TodoBuilder;
import com.freshworks.boot.samples.sqs_processor.dto.TodoDto;
import com.freshworks.boot.samples.sqs_processor.dto.TodoDto.TodoDtoBuilder;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-06-24T23:25:34+0530",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.7 (Oracle Corporation)"
)
@Component
public class TodoMapperImpl implements TodoMapper {

    @Override
    public TodoDto convert(Todo todo) {
        if ( todo == null ) {
            return null;
        }

        TodoDtoBuilder todoDto = TodoDto.builder();

        todoDto.id( todo.getId() );
        todoDto.accountId( todo.getAccountId() );
        todoDto.title( todo.getTitle() );
        todoDto.completed( todo.isCompleted() );

        return todoDto.build();
    }

    @Override
    public Todo convert(TodoDto todo) {
        if ( todo == null ) {
            return null;
        }

        TodoBuilder todo1 = Todo.builder();

        todo1.id( todo.getId() );
        todo1.accountId( todo.getAccountId() );
        todo1.title( todo.getTitle() );
        todo1.completed( todo.isCompleted() );

        return todo1.build();
    }
}
