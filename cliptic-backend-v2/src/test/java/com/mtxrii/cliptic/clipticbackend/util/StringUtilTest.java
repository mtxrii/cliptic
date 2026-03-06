package com.mtxrii.cliptic.clipticbackend.util;

import com.mtxrii.cliptic.clipticbackend.ClipticConst;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StringUtilTest {
    private static final int TEST_RUNS = 10000;

    @Nested
    class Hash4Tests {

        @Test
        public void testHash4_null() {
            assertEquals("0000", StringUtil.hash4(null));
        }

        @Test
        public void testHash4_empty() {
            assertEquals("0000", StringUtil.hash4(""));
        }

        @Test
        public void testHash4_tooBig() {
            String over255 = "a".repeat(256);
            String at255 = "a".repeat(255);
            assertEquals(StringUtil.hash4(at255), StringUtil.hash4(over255));
        }

        @Test
        public void testHash4_normal() {
            Map<Integer, String> expected = new HashMap<>();
            for (int i = 0; i < TEST_RUNS; i++) {
                expected.put(i, StringUtil.hash4(String.valueOf(i)));
            }
            for (int j = 0; j < TEST_RUNS; j++) {
                assertEquals(expected.get(j), StringUtil.hash4(String.valueOf(j)));
            }
        }
    }

    @Nested
    class RandomStringTests {

        @Test
        public void testRandomString_negative() {
            assertNull(StringUtil.randomString(-1));
        }

        @Test
        public void testRandomString_normal_sub32() {
            Set<String> inputs = new HashSet<>();
            for (int i = 0; i < TEST_RUNS; i++) {
                inputs.add(StringUtil.randomString(16));
            }
            assertEquals(TEST_RUNS, inputs.size());
        }

        @Test
        public void testRandomString_normal_over32() {
            Set<String> inputs = new HashSet<>();
            for (int i = 0; i < TEST_RUNS; i++) {
                inputs.add(StringUtil.randomString(64));
            }
            assertEquals(TEST_RUNS, inputs.size());
        }
    }

    @Test
    public void testCreateRandomAlias() {
        Set<String> inputs = new HashSet<>();
        for (int i = 0; i < TEST_RUNS; i++) {
            inputs.add(StringUtil.createRandomAlias(StringUtil.randomString(8)));
        }
        assertEquals(TEST_RUNS, inputs.size());
    }

    @Test
    public void testCreateRandomAlias_sameSize() {
        final int expectedLength = 4 + ClipticConst.CREATE_RANDOM_ALIAS_LENGTH_PADDING;
        Random rng = new Random();
        for (int i = 0; i < TEST_RUNS; i++) {
            String sampleOfRandomLength = StringUtil.randomString(rng.nextInt(33));
            String alias = StringUtil.createRandomAlias(sampleOfRandomLength);
            assertEquals(expectedLength, alias.length());
        }
    }

    @Test
    public void testCreateRandomAlias_upperCase() {
        for (int i = 0; i < TEST_RUNS; i++) {
            String alias = StringUtil.createRandomAlias(StringUtil.randomString(8));
            assertEquals(alias.toUpperCase(), alias);
        }
    }

    @Nested
    class RandomStringMax32Tests {

        @Test
        public void testRandomStringMax32_negative() {
            assertNull(StringUtil.randomStringMax32(-1));
        }

        @Test
        public void testRandomStringMax32_tooBig() {
            assertNull(StringUtil.randomStringMax32(33));
        }

        @Test
        public void testRandomStringMax32_normal() {
            Set<String> inputs = new HashSet<>();
            for (int i = 0; i < TEST_RUNS; i++) {
                inputs.add(StringUtil.randomStringMax32(16));
            }
            assertEquals(TEST_RUNS, inputs.size());
        }
    }

    @Test
    public void testIsLettersNumbersAndDashesOnly_true() {
        String s = "abc123-def456";
        assertTrue(StringUtil.isLettersNumbersAndDashesOnly(s));
    }

    @Test
    public void testIsLettersNumbersAndDashesOnly_false() {
        String s = "https://developer.mozilla.org/en-US/docs";
        assertFalse(StringUtil.isLettersNumbersAndDashesOnly(s));
    }
}
