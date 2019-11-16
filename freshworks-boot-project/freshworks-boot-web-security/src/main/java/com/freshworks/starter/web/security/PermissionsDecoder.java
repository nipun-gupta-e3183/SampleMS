package com.freshworks.starter.web.security;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class PermissionsDecoder {
    // The index is 0 based
    private Map<Integer, String> indexToPermissionMap;

    public PermissionsDecoder(Map<Integer, String> indexToPermissionMap) {
        this.indexToPermissionMap = indexToPermissionMap;
    }

    public PermissionsDecoder(InputStream permissionsInputStream) {
        this.indexToPermissionMap = loadPermissionsFromStream(permissionsInputStream);
    }

    static Map<Integer, String> loadPermissionsFromStream(InputStream permissionsInputStream) {
        Properties permissions = new Properties();
        try {
            permissions.load(permissionsInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return permissions.entrySet().stream()
                .collect(Collectors.toMap(e -> Integer.parseInt((String) e.getKey()), e -> (String) e.getValue()));
    }

    public String[] decode(String input) {
        BigInteger permissionsBits = new BigInteger(input);
        List<String> permissions = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : indexToPermissionMap.entrySet()) {
            if (permissionsBits.testBit(entry.getKey())) {
                permissions.add(entry.getValue());
            }
        }
        return permissions.toArray(new String[0]);
    }

}
