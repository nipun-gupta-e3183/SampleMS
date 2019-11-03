package com.freshworks.starter.sample.api.dto;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class TodoDto {
    private long id;
    @NotNull
    private String title;
    private boolean completed;

    public TodoDto() {
    }

    public TodoDto(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TodoDto)) return false;

        TodoDto todoDto = (TodoDto) o;

        if (id != todoDto.id) return false;
        if (completed != todoDto.completed) return false;
        return Objects.equals(title, todoDto.title);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (completed ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", completed=" + completed +
                '}';
    }
}
