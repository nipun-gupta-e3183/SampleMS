package com.freshworks.boot.samples.api.v1.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.freshworks.boot.samples.api.v1.dto.TodoDto;
import com.freshworks.boot.samples.api.v1.dto.TodoListResponseMetaDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * TodoListResponseDto
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-06-25T00:23:58.886880+05:30[Asia/Kolkata]")
public class TodoListResponseDto   {
  @JsonProperty("todos")
  @Valid
  private List<TodoDto> todos = null;

  @JsonProperty("meta")
  private TodoListResponseMetaDto meta = null;

  public TodoListResponseDto todos(List<TodoDto> todos) {
    this.todos = todos;
    return this;
  }

  public TodoListResponseDto addTodosItem(TodoDto todosItem) {
    if (this.todos == null) {
      this.todos = new ArrayList<>();
    }
    this.todos.add(todosItem);
    return this;
  }

  /**
   * Get todos
   * @return todos
  **/
  @ApiModelProperty(value = "")
      @Valid
    public List<TodoDto> getTodos() {
    return todos;
  }

  public void setTodos(List<TodoDto> todos) {
    this.todos = todos;
  }

  public TodoListResponseDto meta(TodoListResponseMetaDto meta) {
    this.meta = meta;
    return this;
  }

  /**
   * Get meta
   * @return meta
  **/
  @ApiModelProperty(value = "")
  
    @Valid
    public TodoListResponseMetaDto getMeta() {
    return meta;
  }

  public void setMeta(TodoListResponseMetaDto meta) {
    this.meta = meta;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TodoListResponseDto todoListResponse = (TodoListResponseDto) o;
    return Objects.equals(this.todos, todoListResponse.todos) &&
        Objects.equals(this.meta, todoListResponse.meta);
  }

  @Override
  public int hashCode() {
    return Objects.hash(todos, meta);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TodoListResponseDto {\n");
    
    sb.append("    todos: ").append(toIndentedString(todos)).append("\n");
    sb.append("    meta: ").append(toIndentedString(meta)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
