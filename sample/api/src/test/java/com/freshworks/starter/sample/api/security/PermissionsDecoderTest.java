package com.freshworks.starter.sample.api.security;

import org.junit.jupiter.api.Test;

import java.util.Base64;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class PermissionsDecoderTest {
    Map<Integer, String> BIT_TO_PERMISSION_MAP = Map.of(
            2, "read",
            4, "write");

    @Test
    void decode_InvalidString_ExceptionThrown() {
        PermissionsDecoder permissionsDecoder = new PermissionsDecoder(BIT_TO_PERMISSION_MAP);

        Throwable exception = catchThrowable(() -> permissionsDecoder.decode("invalid_base64_string"));

        assertThat(exception).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void decode_NoPermissions_ReturnsEmptyList() {
        PermissionsDecoder permissionsDecoder = new PermissionsDecoder(BIT_TO_PERMISSION_MAP);
        String permissionsString = Base64.getEncoder().encodeToString(new byte[]{(byte) 0b00000000});

        String[] permissions = permissionsDecoder.decode(permissionsString);

        assertThat(permissions).isEmpty();
    }
    @Test
    void decode_EmptyInput_ThrowsInvalidArgumentException() {
        PermissionsDecoder permissionsDecoder = new PermissionsDecoder(BIT_TO_PERMISSION_MAP);

        Throwable ex = catchThrowable(() -> permissionsDecoder.decode(""));

        assertThat(ex).isInstanceOf(IllegalArgumentException.class);
    }
    @Test
    void decode_PermissionsEnabledInOneByte_ReturnedInPermissionsList() {
        PermissionsDecoder permissionsDecoder = new PermissionsDecoder(BIT_TO_PERMISSION_MAP);
        String permissionsString = Base64.getEncoder().encodeToString(new byte[]{(byte) 0b01010000});

        String[] permissions = permissionsDecoder.decode(permissionsString);

        assertThat(permissions).containsExactlyInAnyOrder("read", "write");
    }

    @Test
    void decode_PermissionsNotEnabled_NotIncludedInPermissionsList() {
        PermissionsDecoder permissionsDecoder = new PermissionsDecoder(BIT_TO_PERMISSION_MAP);
        String permissionsString = Base64.getEncoder().encodeToString(new byte[]{(byte) 0b10111111});

        String[] permissions = permissionsDecoder.decode(permissionsString);

        assertThat(permissions).containsExactlyInAnyOrder("write").doesNotContain("read");
    }

    @Test
    void decode_PermissionsAcrossMultipleBytes_ReturnedInPermissionsList() {
        PermissionsDecoder permissionsDecoder = new PermissionsDecoder(Map.of(2, "read", 4, "write", 9, "admin"));
        String permissionsString = Base64.getEncoder().encodeToString(new byte[]{(byte) 0b01000000, (byte) 0b10000000});

        String[] permissions = permissionsDecoder.decode(permissionsString);

        assertThat(permissions).containsExactlyInAnyOrder("read", "admin").doesNotContain("write");
    }

    @Test
    void encode_EmptyPermissions_ReturnsValue() {
        PermissionsDecoder permissionsDecoder = new PermissionsDecoder(BIT_TO_PERMISSION_MAP);

        String permissionsStr = permissionsDecoder.encode();

        assertThat(permissionsStr).isEqualTo("AA==");
    }
    @Test
    void encode_IndexesLessThan8_EncodedInOneByte() {
        PermissionsDecoder permissionsDecoder = new PermissionsDecoder(BIT_TO_PERMISSION_MAP);

        String permissionsStr = permissionsDecoder.encode("read", "write");

        String expectedStr = Base64.getEncoder().encodeToString(new byte[]{(byte) 0b01010000});
        assertThat(permissionsStr).isEqualTo(expectedStr);
    }
    @Test
    void encode_IndexesMoreThan8_EncodedInMultipleBytes() {
        PermissionsDecoder permissionsDecoder = new PermissionsDecoder(Map.of(2, "read", 4, "write", 9, "admin"));

        String permissionsStr = permissionsDecoder.encode("read", "admin");

        String expectedStr = Base64.getEncoder().encodeToString(new byte[]{(byte) 0b01000000, (byte) 0b10000000});
        assertThat(permissionsStr).isEqualTo(expectedStr);
    }
}