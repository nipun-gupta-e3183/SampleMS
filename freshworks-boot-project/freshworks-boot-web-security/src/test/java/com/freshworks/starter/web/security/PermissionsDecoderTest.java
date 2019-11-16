package com.freshworks.starter.web.security;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class PermissionsDecoderTest {
    private Map<Integer, String> INDEX_TO_PERMISSION_MAP = Map.of(
            2, "read",
            4, "write");

    @Test
    void decode_InvalidString_ThrowsException() {
        PermissionsDecoder permissionsDecoder = new PermissionsDecoder(INDEX_TO_PERMISSION_MAP);

        Throwable exception = catchThrowable(() -> permissionsDecoder.decode("invalid_base64_string"));

        assertThat(exception).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void decode_NoPermissions_ReturnsEmptyList() {
        PermissionsDecoder permissionsDecoder = new PermissionsDecoder(INDEX_TO_PERMISSION_MAP);
        String permissionsString = "0";

        String[] permissions = permissionsDecoder.decode(permissionsString);

        assertThat(permissions).isEmpty();
    }

    @Test
    void decode_EmptyInput_ThrowsInvalidArgumentException() {
        PermissionsDecoder permissionsDecoder = new PermissionsDecoder(INDEX_TO_PERMISSION_MAP);

        Throwable ex = catchThrowable(() -> permissionsDecoder.decode(""));

        assertThat(ex).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void decode_PermissionsEnabledInOneByte_ReturnedInPermissionsList() {
        PermissionsDecoder permissionsDecoder = new PermissionsDecoder(INDEX_TO_PERMISSION_MAP);
        String permissionsString = getPermissionsString(2, 4);

        String[] permissions = permissionsDecoder.decode(permissionsString);

        assertThat(permissions).containsExactlyInAnyOrder("read", "write");
    }

    @Test
    void decode_PermissionsNotEnabled_NotIncludedInPermissionsList() {
        PermissionsDecoder permissionsDecoder = new PermissionsDecoder(INDEX_TO_PERMISSION_MAP);
        String permissionsString = getPermissionsString(0, 1, 3, 4, 5, 6, 7);

        String[] permissions = permissionsDecoder.decode(permissionsString);

        assertThat(permissions).containsExactlyInAnyOrder("write").doesNotContain("read");
    }

    @Test
    void decode_PermissionsAcrossMultipleBytes_ReturnedInPermissionsList() {
        PermissionsDecoder permissionsDecoder = new PermissionsDecoder(Map.of(2, "read", 4, "write", 9, "admin"));
        String permissionsString = getPermissionsString(2, 9);

        String[] permissions = permissionsDecoder.decode(permissionsString);

        assertThat(permissions).containsExactlyInAnyOrder("read", "admin").doesNotContain("write");
    }

    @Test
    void decode_PermissionInBigIndex_ReturnedInPermissionsList() {
        PermissionsDecoder permissionsDecoder = new PermissionsDecoder(Map.of(2, "read", 4, "write", 1000, "super-admin"));
        String permissionsString = getPermissionsString(2, 1000);

        String[] permissions = permissionsDecoder.decode(permissionsString);

        assertThat(permissions).containsExactlyInAnyOrder("read", "super-admin").doesNotContain("write");
    }

    static String getPermissionsString(int... indexes) {
        if (indexes.length == 0) {
            return "0";
        }
        Arrays.sort(indexes);
        StringBuilder builder = new StringBuilder();
        for (int i = indexes[indexes.length-1]; i >= 0; i--) {
            builder.append(Arrays.binarySearch(indexes, i) >= 0 ? '1' : '0');
        }
        return new BigInteger(builder.toString(), 2).toString();
    }

}