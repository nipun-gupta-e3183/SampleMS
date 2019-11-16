package com.freshworks.starter.web.security;

import java.io.FileInputStream;

public class PermissionsStringGenerator {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage: PermissionsEncoderUtil /path/to/permissions.properties comma,separated,permissions");
            System.exit(1);
        }
        FileInputStream permissionsStream = new FileInputStream(args[0]);
        PermissionsEncoder permissionsEncoder = new PermissionsEncoder(permissionsStream);
        String[] permissions;
        if (args[1].equals("")) {
            permissions = new String[0];
        } else {
            permissions = args[1].split(",");
        }
        String permissionsString = permissionsEncoder.encode(permissions);
        System.out.printf("Encoded string for permissions [%s]: %s%n", args[1], permissionsString);
    }
}
