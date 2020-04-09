package com.freshworks.boot.samples.kafka_processor.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TodoDeleteDto {
    private long id;

    @JsonCreator
    public TodoDeleteDto(@JsonProperty("id") long id) {
        this.id = id;
    }

}
