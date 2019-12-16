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

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;
/**
 * TodoListResponseMetaDto
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2019-12-16T16:06:56.680618+05:30[Asia/Kolkata]")
public class TodoListResponseMetaDto {
  @JsonProperty("total_items")
  private Long totalItems = null;

  public TodoListResponseMetaDto totalItems(Long totalItems) {
    this.totalItems = totalItems;
    return this;
  }

   /**
   * Total Items
   * @return totalItems
  **/
  @Schema(example = "100", description = "Total Items")
  public Long getTotalItems() {
    return totalItems;
  }

  public void setTotalItems(Long totalItems) {
    this.totalItems = totalItems;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TodoListResponseMetaDto todoListResponseMeta = (TodoListResponseMetaDto) o;
    return Objects.equals(this.totalItems, todoListResponseMeta.totalItems);
  }

  @Override
  public int hashCode() {
    return Objects.hash(totalItems);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TodoListResponseMetaDto {\n");
    
    sb.append("    totalItems: ").append(toIndentedString(totalItems)).append("\n");
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
