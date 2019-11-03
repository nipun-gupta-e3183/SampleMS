package com.freshworks.starter.sample.api.security;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class PermissionsDecoder {
    // The bits are 1 based
    private Map<Integer, String> bitToPermissionMap;

    public PermissionsDecoder(Map<Integer, String> bitToPermissionMap) {
        this.bitToPermissionMap = bitToPermissionMap;
    }

    public String[] decode(String input) {
        byte[] permissionsBitset = Base64.getDecoder().decode(input);
        List<String> permissions = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : bitToPermissionMap.entrySet()) {
            int position = entry.getKey();
            int index = position / 8;
            int bit = 8 - (position % 8);
            if (permissionsBitset.length >= index && (permissionsBitset[index] & 1<<bit) != 0) {
                permissions.add(entry.getValue());
            }
        }
        return permissions.toArray(new String[0]);
    }
}
