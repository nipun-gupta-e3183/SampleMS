package com.freshworks.starter.sample.sqs_processor.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TodoDto {
    private long id;
    private String title;
    private boolean completed;

    @JsonCreator
    public TodoDto(@JsonProperty("title") String title, @JsonProperty("completed") boolean completed) {
        this.title = title;
        this.completed = completed;
    }

}
