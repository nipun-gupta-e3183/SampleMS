package com.freshworks.boot.samples.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;

// Ideally this will be stored in a data store.
@Data
@AllArgsConstructor
public class Account {
    private String accountID;
}
