/*
 * Todo API
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package com.freshworks.starter.sample.api.v1.dto;

import java.util.Objects;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.freshworks.starter.sample.api.v1.dto.TodoDto;
import com.freshworks.starter.sample.api.v1.dto.TodoListResponseMetaDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;
import javax.validation.Valid;
/**
 * TodoListResponseDto
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2019-12-18T10:46:53.992845+05:30[Asia/Kolkata]")
public class TodoListResponseDto {
  @JsonProperty("todos")
  private List<TodoDto> todos = null;

  @JsonProperty("meta")
  private TodoListResponseMetaDto meta = null;

  public TodoListResponseDto todos(List<TodoDto> todos) {
    this.todos = todos;
    return this;
  }

  public TodoListResponseDto addTodosItem(TodoDto todosItem) {
    if (this.todos == null) {
      this.todos = new ArrayList<TodoDto>();
    }
    this.todos.add(todosItem);
    return this;
  }

   /**
   * Get todos
   * @return todos
  **/
  @Valid
  @Schema(description = "")
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
  @Valid
  @Schema(description = "")
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
