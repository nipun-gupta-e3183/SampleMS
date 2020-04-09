package com.freshworks.boot.samples.kafka_processor.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TodoDto {
    private long id;
    private String title;
    private boolean completed;

    @JsonCreator
    public TodoDto(@JsonProperty("id") long id, @JsonProperty("title") String title, @JsonProperty("completed") boolean completed) {
        this.id = id;
        this.title = title;
        this.completed = completed;
    }

}
