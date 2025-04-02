/*
 * Copyright (c) 2025 Tim Savage.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.winterhavenmc.util.messagebuilder.resources.language.yaml;

import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RecordKeyTest {

    @Test
    void createFromValidString_ShouldReturnRecordKey() {
        Optional<RecordKey> result = RecordKey.create("VALID_KEY");
        assertTrue(result.isPresent());
        assertEquals("VALID_KEY", result.get().toString());
    }

    @Test
    void createFromInvalidString_ShouldReturnEmpty() {
        assertTrue(RecordKey.create("123INVALID").isEmpty(), "Should return empty for invalid format.");
        assertTrue(RecordKey.create("").isEmpty(), "Should return empty for empty string.");
        assertTrue(RecordKey.create(" ").isEmpty(), "Should return empty for whitespace.");
        assertTrue(RecordKey.create((String) null).isEmpty(), "Should return empty for null.");
        assertTrue(RecordKey.create((MessageId) null).isEmpty(), "Should return empty for null.");
    }

    @Test
    void createFromEnum_ShouldReturnRecordKey() {
        Optional<RecordKey> result = RecordKey.create(MessageId.ENABLED_MESSAGE);
        assertTrue(result.isPresent());
        assertEquals("ENABLED_MESSAGE", result.get().toString());
    }

    @Test
    void createFromNullEnum_ShouldReturnEmpty() {
        assertTrue(RecordKey.create((MessageId) null).isEmpty());
    }

    @Test
    void recordKeyEquality_ShouldBeTrueForSameKey() {
        Optional<RecordKey> key1 = RecordKey.create("SAME_KEY");
        Optional<RecordKey> key2 = RecordKey.create("SAME_KEY");

        assertTrue(key1.isPresent() && key2.isPresent());
        assertEquals(key1.get(), key2.get());
    }

    @Test
    void recordKeyEquality_ShouldBeFalseForDifferentKeys() {
        Optional<RecordKey> key1 = RecordKey.create("KEY_ONE");
        Optional<RecordKey> key2 = RecordKey.create("KEY_TWO");

        assertTrue(key1.isPresent() && key2.isPresent());
        assertNotEquals(key1.get(), key2.get());
    }

    @Test
    void hashCode_ShouldBeConsistentWithEquals() {
        Optional<RecordKey> key1 = RecordKey.create("HASH_TEST");
        Optional<RecordKey> key2 = RecordKey.create("HASH_TEST");

        assertTrue(key1.isPresent() && key2.isPresent());
        assertEquals(key1.get().hashCode(), key2.get().hashCode());
    }

    @Test
    void toString_ShouldReturnCorrectKey() {
        Optional<RecordKey> key = RecordKey.create("TO_STRING_TEST");
        assertTrue(key.isPresent());
        assertEquals("TO_STRING_TEST", key.get().toString());
    }

}
