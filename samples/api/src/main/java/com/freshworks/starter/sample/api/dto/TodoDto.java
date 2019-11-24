package com.freshworks.starter.sample.api.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class TodoDto {
    private long id;
    @NotNull
    @NonNull
    private String title;
    private boolean completed;
}
