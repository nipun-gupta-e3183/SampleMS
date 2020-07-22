package com.freshworks.boot.samples.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class AccountInfo {
    @Column(name = "account_id", nullable = false)
    @TenantSupport(tenantField = "accountID")
    private String accountId;
}
