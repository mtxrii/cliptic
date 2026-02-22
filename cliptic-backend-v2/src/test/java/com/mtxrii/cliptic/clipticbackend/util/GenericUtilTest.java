package com.mtxrii.cliptic.clipticbackend.util;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GenericUtilTest {

    @Nested
    class EqualsTests {

        @Test
        public void testEquals_aNullBNotNull() {
            assertFalse(GenericUtil.equals(null, "test"));
        }

        @Test
        public void testEquals_aNotNullBNull() {
            assertFalse(GenericUtil.equals("test", null));
        }

        @Test
        public void testEquals_aNullBNull() {
            assertTrue(GenericUtil.equals(null, null));
        }

        @Test
        public void testEquals_bothNotNull_notEquals() {
            assertFalse(GenericUtil.equals("test1", "test2"));
        }

        @Test
        public void testEquals_bothNotNull_equals() {
            assertTrue(GenericUtil.equals("test", "test"));
        }
    }
}
