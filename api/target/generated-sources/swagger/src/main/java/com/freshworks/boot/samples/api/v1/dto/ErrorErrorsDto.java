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
 * ErrorErrorsDto
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-06-25T00:23:58.886880+05:30[Asia/Kolkata]")
public class ErrorErrorsDto   {
  @JsonProperty("field")
  private String field = null;

  @JsonProperty("code")
  private String code = null;

  @JsonProperty("description")
  private String description = null;

  public ErrorErrorsDto field(String field) {
    this.field = field;
    return this;
  }

  /**
   * Get field
   * @return field
  **/
  @ApiModelProperty(value = "")
  
    public String getField() {
    return field;
  }

  public void setField(String field) {
    this.field = field;
  }

  public ErrorErrorsDto code(String code) {
    this.code = code;
    return this;
  }

  /**
   * Get code
   * @return code
  **/
  @ApiModelProperty(value = "")
  
    public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public ErrorErrorsDto description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
  **/
  @ApiModelProperty(value = "")
  
    public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ErrorErrorsDto errorErrors = (ErrorErrorsDto) o;
    return Objects.equals(this.field, errorErrors.field) &&
        Objects.equals(this.code, errorErrors.code) &&
        Objects.equals(this.description, errorErrors.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(field, code, description);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ErrorErrorsDto {\n");
    
    sb.append("    field: ").append(toIndentedString(field)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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
