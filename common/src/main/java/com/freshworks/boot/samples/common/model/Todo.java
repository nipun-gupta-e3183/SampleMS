package com.freshworks.boot.samples.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import javax.persistence.*;

@Entity
@Table(name = "todos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FilterDef(name = "accountFilter", parameters = {@ParamDef(name = "accountId", type = "string")})
@Filter(name = "accountFilter", condition = "account_id = :accountId")
public class Todo extends AccountInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    private long id;

    private String title;
    private boolean completed;
}
