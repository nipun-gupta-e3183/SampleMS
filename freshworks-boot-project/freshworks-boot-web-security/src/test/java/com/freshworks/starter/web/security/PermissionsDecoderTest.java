package com.freshworks.starter.web.security;

import org.junit.jupiter.api.Test;

import java.util.Base64;
import java.util.BitSet;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class PermissionsDecoderTest {
    private Map<Integer, String> INDEX_TO_PERMISSION_MAP = Map.of(
            2, "read",
            4, "write");

    @Test
    void decode_InvalidString_ExceptionThrown() {
        PermissionsDecoder permissionsDecoder = new PermissionsDecoder(INDEX_TO_PERMISSION_MAP);

        Throwable exception = catchThrowable(() -> permissionsDecoder.decode("invalid_base64_string"));

        assertThat(exception).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void decode_NoPermissions_ReturnsEmptyList() {
        PermissionsDecoder permissionsDecoder = new PermissionsDecoder(INDEX_TO_PERMISSION_MAP);
        String permissionsString = Base64.getEncoder().encodeToString(new byte[]{(byte) 0b00000000});

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
    void encode_EmptyPermissions_ReturnsValue() {
        PermissionsDecoder permissionsDecoder = new PermissionsDecoder(INDEX_TO_PERMISSION_MAP);

        String permissionsStr = permissionsDecoder.encode();

        assertThat(permissionsStr).isEqualTo(getPermissionsString());
    }

    @Test
    void encode_IndexesLessThan8_EncodedInOneByte() {
        PermissionsDecoder permissionsDecoder = new PermissionsDecoder(INDEX_TO_PERMISSION_MAP);

        String permissionsStr = permissionsDecoder.encode("read", "write");

        assertThat(permissionsStr).isEqualTo(getPermissionsString(2, 4));
    }

    @Test
    void encode_IndexesMoreThan8_EncodedInMultipleBytes() {
        PermissionsDecoder permissionsDecoder = new PermissionsDecoder(Map.of(2, "read", 4, "write", 9, "admin"));

        String permissionsStr = permissionsDecoder.encode("read", "admin");

        assertThat(permissionsStr).isEqualTo(getPermissionsString(2, 9));
    }

    private String getPermissionsString(int... indexes) {
        BitSet bitSet = new BitSet();
        for (int index : indexes) {
            bitSet.set(index);
        }
        return Base64.getEncoder().encodeToString(bitSet.toByteArray());
    }

}