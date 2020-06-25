package com.freshworks.boot.samples.api.v1.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * TodoListResponseMetaDto
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-06-25T00:23:58.886880+05:30[Asia/Kolkata]")
public class TodoListResponseMetaDto   {
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
  @ApiModelProperty(example = "100", value = "Total Items")
  
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
