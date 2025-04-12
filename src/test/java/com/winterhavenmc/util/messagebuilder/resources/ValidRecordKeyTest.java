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

package com.winterhavenmc.util.messagebuilder.resources;

import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import com.winterhavenmc.util.messagebuilder.recordkey.ValidRecordKey;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


class ValidRecordKeyTest
{

    @Test
    void ofFromValidString_ShouldReturnRecordKey() {
        Optional<ValidRecordKey> result = ValidRecordKey.of("VALID_KEY");
        assertTrue(result.isPresent());
        assertEquals("VALID_KEY", result.get().toString());
    }

    @Test
    void ofFromInvalidString_ShouldReturnEmpty() {
        assertTrue(ValidRecordKey.of("123INVALID").isEmpty(), "Should return empty for invalid format.");
        assertTrue(ValidRecordKey.of("").isEmpty(), "Should return empty for empty string.");
        assertTrue(ValidRecordKey.of(" ").isEmpty(), "Should return empty for whitespace.");
        assertTrue(ValidRecordKey.of((String) null).isEmpty(), "Should return empty for null.");
        assertTrue(ValidRecordKey.of((MessageId) null).isEmpty(), "Should return empty for null.");
    }

    @Test
    void ofFromEnum_ShouldReturnRecordKey() {
        Optional<ValidRecordKey> result = ValidRecordKey.of(MessageId.ENABLED_MESSAGE);
        assertTrue(result.isPresent());
        assertEquals("ENABLED_MESSAGE", result.get().toString());
    }

    @Test
    void ofFromNullEnum_ShouldReturnEmpty() {
        assertTrue(ValidRecordKey.of((MessageId) null).isEmpty());
    }

    @Test
    void recordKeyEquality_ShouldBeTrueForSameKey() {
        Optional<ValidRecordKey> key1 = ValidRecordKey.of("SAME_KEY");
        Optional<ValidRecordKey> key2 = ValidRecordKey.of("SAME_KEY");

        assertTrue(key1.isPresent() && key2.isPresent());
        assertEquals(key1.get(), key2.get());
    }

    @Test
    void recordKeyEquality_ShouldBeFalseForDifferentKeys() {
        Optional<ValidRecordKey> key1 = ValidRecordKey.of("KEY_ONE");
        Optional<ValidRecordKey> key2 = ValidRecordKey.of("KEY_TWO");

        assertTrue(key1.isPresent() && key2.isPresent());
        assertNotEquals(key1.get(), key2.get());
    }

    @Test
    void hashCode_ShouldBeConsistentWithEquals() {
        Optional<ValidRecordKey> key1 = ValidRecordKey.of("HASH_TEST");
        Optional<ValidRecordKey> key2 = ValidRecordKey.of("HASH_TEST");

        assertTrue(key1.isPresent() && key2.isPresent());
        assertEquals(key1.get().hashCode(), key2.get().hashCode());
    }

    @Test
    void toString_ShouldReturnCorrectKey() {
        Optional<ValidRecordKey> key = ValidRecordKey.of("TO_STRING_TEST");
        assertTrue(key.isPresent());
        assertEquals("TO_STRING_TEST", key.get().toString());
    }

}
