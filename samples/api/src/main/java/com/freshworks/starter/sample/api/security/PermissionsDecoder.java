package com.freshworks.starter.sample.api.security;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PermissionsDecoder {
    // The bits are 1 based
    private Map<Integer, String> bitToPermissionMap;

    public PermissionsDecoder(Map<Integer, String> bitToPermissionMap) {
        this.bitToPermissionMap = bitToPermissionMap;
    }

    public String[] decode(String input) {
        if ("".equals(input)) {
            throw new IllegalArgumentException("Permissions string can't be empty");
        }
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

    public String encode(String... permissions) {
        Map<String, Integer> permissionToBitMap = invert(bitToPermissionMap);
        int[] positions = new int[permissions.length];
        int maxValue = 0;
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            Integer value = permissionToBitMap.get(permission);
            if (value != null) {
                positions[i] = value;
                maxValue = value > maxValue ? value : maxValue;
            }
        }
        byte[] permissionsBitset = new byte[(maxValue - 1) / 8 + 1];
        for (int position : positions) {
            int index = position / 8;
            int bit = 8 - (position % 8);
            permissionsBitset[index] |= 1<<bit;
        }
        return Base64.getEncoder().encodeToString(permissionsBitset);
    }

    private Map<String, Integer> invert(Map<Integer, String> bitToPermissionMap) {
        return bitToPermissionMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }
}
