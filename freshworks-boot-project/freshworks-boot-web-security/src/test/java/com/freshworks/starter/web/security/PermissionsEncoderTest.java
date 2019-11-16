package com.freshworks.starter.web.security;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.freshworks.starter.web.security.PermissionsDecoderTest.getPermissionsString;
import static org.assertj.core.api.Assertions.assertThat;

public class PermissionsEncoderTest {
    private Map<Integer, String> INDEX_TO_PERMISSION_MAP = Map.of(
            2, "read",
            4, "write");
    @Test
    void encode_EmptyPermissions_ReturnsZero() {
        PermissionsEncoder permissionsEncoder = new PermissionsEncoder(INDEX_TO_PERMISSION_MAP);

        String permissionsStr = permissionsEncoder.encode();

        assertThat(permissionsStr).isEqualTo("0");
    }

    @Test
    void encode_IndexesLessThan8_EncodedInOneByte() {
        PermissionsEncoder permissionsEncoder = new PermissionsEncoder(INDEX_TO_PERMISSION_MAP);

        String permissionsStr = permissionsEncoder.encode("read", "write");

        assertThat(permissionsStr).isEqualTo(getPermissionsString(2, 4));
    }

    @Test
    void encode_IndexesMoreThan8_EncodedInMultipleBytes() {
        PermissionsEncoder permissionsEncoder = new PermissionsEncoder(Map.of(2, "read", 4, "write", 9, "admin"));

        String permissionsStr = permissionsEncoder.encode("read", "admin");

        assertThat(permissionsStr).isEqualTo(getPermissionsString(2, 9));
    }
    @Test
    void encode_BigIndexes_ProperlyEncoded() {
        PermissionsEncoder permissionsEncoder = new PermissionsEncoder(Map.of(2, "read", 4, "write", 1000, "admin"));

        String permissionsStr = permissionsEncoder.encode("read", "admin");

        assertThat(permissionsStr).isEqualTo(getPermissionsString(2, 1000));
    }
}
