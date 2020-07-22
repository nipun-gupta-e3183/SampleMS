package com.freshworks.boot.samples.common.service;

import com.freshworks.boot.common.AccountFetcher;
import com.freshworks.boot.samples.common.model.Account;
import org.springframework.stereotype.Service;

@Service
public class TodoAccountFetcher implements AccountFetcher<Account> {
    @Override
    public Account fetchAccount(String product, String accountID) {
        return new Account(accountID);
    }
}
