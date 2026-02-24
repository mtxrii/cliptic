package com.mtxrii.cliptic.clipticbackend.util;

import com.mtxrii.cliptic.clipticbackend.ClipticConst;

import java.util.UUID;

public final class StringUtil {
    private StringUtil() { }

    public static String createRandomAlias(String originalUrl) {
        return hash4(originalUrl) + randomStringMax32(ClipticConst.CREATE_RANDOM_ALIAS_LENGTH_PADDING);
    }

    public static String randomString(int length) {
        if (length < 0) {
            return null;
        }

        int times = Math.floorDiv(length, 32);
        int extra = length % 32;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) {
            String str = randomStringAlg(32);
            sb.append(str);
        }
        String extraStr = randomStringAlg(extra);
        sb.append(extraStr);
        return sb.toString();
    }

    private static String randomStringAlg(int length) {
        return UUID.randomUUID().toString().replaceAll("-", ClipticConst.EMPTY_STRING).substring(0, length).toUpperCase();
    }

    protected static String randomStringMax32(int length) {
        if (length > 32 || length < 0) {
            return null;
        }
        return randomString(length);
    }

    // Source: https://chatgpt.com/share/699aedd7-1b68-800a-a1a3-f918b042d1e1
    public static String hash4(String input) {
        if (input == null || input.isEmpty()) {
            return "0000";
        }

        // Limit input length to 255 chars
        if (input.length() > 255) {
            input = input.substring(0, 255);
        }

        int hash = 0;

        // Simple hash computation
        for (int i = 0; i < input.length(); i++) {
            hash = (hash * 31 + input.charAt(i)) & 0x7FFFFFFF; // keep positive
        }

        // Convert to base36 (0-9 + a-z) and ensure length 4
        String base36 = Integer.toString(hash, 36);

        // Pad or trim to exactly 4 characters
        if (base36.length() < 4) {
            base36 = String.format("%4s", base36).replace(' ', '0');
        } else if (base36.length() > 4) {
            base36 = base36.substring(0, 4);
        }

        return base36;
    }
}
