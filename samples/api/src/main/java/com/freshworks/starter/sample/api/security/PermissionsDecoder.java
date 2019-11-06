package com.freshworks.starter.sample.api.security;

import java.util.*;
import java.util.stream.Collectors;

public class PermissionsDecoder {
    // The index is 0 based
    private Map<Integer, String> indexToPermissionMap;

    public PermissionsDecoder(Map<Integer, String> indexToPermissionMap) {
        this.indexToPermissionMap = indexToPermissionMap;
    }

    public String[] decode(String input) {
        if ("".equals(input)) {
            throw new IllegalArgumentException("Permissions string can't be empty");
        }
        BitSet permissionsBitSet = BitSet.valueOf(Base64.getDecoder().decode(input));
        List<String> permissions = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : indexToPermissionMap.entrySet()) {
            if (permissionsBitSet.get(entry.getKey())) {
                permissions.add(entry.getValue());
            }
        }
        return permissions.toArray(new String[0]);
    }

    public String encode(String... permissions) {
        Map<String, Integer> permissionToBitMap = invert(indexToPermissionMap);
        BitSet permissionsBitSet = new BitSet();
        for (String permission : permissions) {
            permissionsBitSet.set(permissionToBitMap.get(permission));
        }
        return Base64.getEncoder().encodeToString(permissionsBitSet.toByteArray());
    }

    private Map<String, Integer> invert(Map<Integer, String> bitToPermissionMap) {
        return bitToPermissionMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }
}
