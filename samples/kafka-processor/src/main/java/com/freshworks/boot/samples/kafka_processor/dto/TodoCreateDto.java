package com.freshworks.boot.samples.kafka_processor.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TodoCreateDto {
    private String title;
    private boolean completed;

    @JsonCreator
    public TodoCreateDto(@JsonProperty("title") String title, @JsonProperty("completed") boolean completed) {
        this.title = title;
        this.completed = completed;
    }

}
