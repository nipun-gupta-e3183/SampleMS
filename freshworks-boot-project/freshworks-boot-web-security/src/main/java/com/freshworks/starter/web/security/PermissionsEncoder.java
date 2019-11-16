package com.freshworks.starter.web.security;

import java.io.InputStream;
import java.math.BigInteger;
import java.util.Map;
import java.util.stream.Collectors;

import static com.freshworks.starter.web.security.PermissionsDecoder.loadPermissionsFromStream;

public class PermissionsEncoder {
    // The index is 0 based
    private Map<Integer, String> indexToPermissionMap;

    public PermissionsEncoder(Map<Integer, String> indexToPermissionMap) {
        this.indexToPermissionMap = indexToPermissionMap;
    }

    public PermissionsEncoder(InputStream permissionsInputStream) {
        this.indexToPermissionMap = loadPermissionsFromStream(permissionsInputStream);
    }

    public String encode(String... permissions) {
        Map<String, Integer> permissionToBitMap = invert(indexToPermissionMap);
        Integer maxIndex = permissionToBitMap.values().stream().max(Integer::compareTo).orElse(1);
        int numOfBytes = (maxIndex + 1) / 8 + 1; // extra 1 bit to avoid setting sign bit
        byte[] bytes = new byte[numOfBytes];
        for (String permission : permissions) {
            Integer index = permissionToBitMap.get(permission);
            if (index == null) {
                throw new IllegalArgumentException("Unknown permission granted: " + permission);
            }
            bytes[numOfBytes - 1 - index / 8] |= (1 << (index % 8));
        }
        return new BigInteger(bytes).toString();
    }

    private Map<String, Integer> invert(Map<Integer, String> bitToPermissionMap) {
        return bitToPermissionMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }
}
