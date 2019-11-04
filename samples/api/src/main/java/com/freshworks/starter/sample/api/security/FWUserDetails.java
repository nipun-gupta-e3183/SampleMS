package com.freshworks.starter.sample.api.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public class FWUserDetails extends User {
    private final String userId;
    private final String clientId;
    private final String accountId;
    private final String orgId;

    public FWUserDetails(String userId, String clientId, String accountId, String orgId, List<GrantedAuthority> grantedAuthorities) {
        super(userId, "dummy", grantedAuthorities);
        this.userId = userId;
        this.clientId = clientId;
        this.accountId = accountId;
        this.orgId = orgId;
    }

    public String getUserId() {
        return userId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getOrgId() {
        return orgId;
    }

    @Override
    public String toString() {
        return "FWUserDetails{" +
                "userId='" + userId + '\'' +
                ", clientId='" + clientId + '\'' +
                ", accountId='" + accountId + '\'' +
                ", orgId='" + orgId + '\'' +
                "} " + super.toString();
    }
}
