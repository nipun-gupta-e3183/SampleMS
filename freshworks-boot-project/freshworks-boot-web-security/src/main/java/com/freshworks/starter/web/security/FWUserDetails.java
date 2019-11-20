package com.freshworks.starter.web.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public class FWUserDetails extends User {
    private final String userId;
    private final String accountId;
    private final String orgId;

    @SuppressWarnings("WeakerAccess")
    public FWUserDetails(String userId, String accountId, String orgId, List<GrantedAuthority> grantedAuthorities) {
        super(userId, "dummy", grantedAuthorities);
        this.userId = userId;
        this.accountId = accountId;
        this.orgId = orgId;
    }

    @SuppressWarnings("unused")
    public String getUserId() {
        return userId;
    }

    @SuppressWarnings("unused")
    public String getAccountId() {
        return accountId;
    }

    @SuppressWarnings("unused")
    public String getOrgId() {
        return orgId;
    }

    @Override
    public String toString() {
        return "FWUserDetails{" +
                "userId='" + userId + '\'' +
                ", accountId='" + accountId + '\'' +
                ", orgId='" + orgId + '\'' +
                "} " + super.toString();
    }
}
