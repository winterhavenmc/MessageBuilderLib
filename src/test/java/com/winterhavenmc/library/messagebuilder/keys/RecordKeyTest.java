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

package com.winterhavenmc.library.messagebuilder.keys;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


class RecordKeyTest
{
	enum TestEnum
	{
		INVALID$KEY,
		VALID_KEY
	}


	@Test
	void append()
	{
		// Arrange
		RecordKey recordKey = RecordKey.of("PLAYER").orElseThrow();

		// Act
		RecordKey result = recordKey.append("LOCATION").orElseThrow().append("X").orElseThrow();

		// Assert
		assertEquals("PLAYER.LOCATION.X", result.toString());
	}


    @Nested
    class TestStaticFactoryMethod
	{
		@Test
		void testOf_valid_string ()
		{
			// Arrange & Act
			Optional<RecordKey> result = RecordKey.of("VALID_KEY");

			// Assert
			assertTrue(result.isPresent());
			assertEquals("VALID_KEY", result.get().toString());
		}


		@Test
		void testOf_null_string ()
		{
			assertTrue(RecordKey.of((String) null).isEmpty());
		}


		@Test
		void testOf_invalid_strings ()
		{
			assertTrue(RecordKey.of("123INVALID").isEmpty(), "Should return empty for invalid format.");
			assertTrue(RecordKey.of("").isEmpty(), "Should return empty for empty string.");
			assertTrue(RecordKey.of(" ").isEmpty(), "Should return empty for whitespace.");
			assertTrue(RecordKey.of("Invalid Key").isEmpty(), "Should return empty for whitespace.");
			assertTrue(RecordKey.of((String) null).isEmpty(), "Should return empty for null.");
			assertTrue(RecordKey.of((TestEnum) null).isEmpty(), "Should return empty for null.");
		}


		@Test
		void testOf_valid_enum()
		{
			// Arrange & Act
			Optional<RecordKey> result = RecordKey.of(TestEnum.VALID_KEY);

			// Assert
			assertTrue(result.isPresent());
			assertEquals("VALID_KEY", result.get().toString());
		}


		@Test
		void testOf_null_enum ()
		{
			assertTrue(RecordKey.of((TestEnum) null).isEmpty());
		}


		@Test
		void testOf_invalid_enum ()
		{
			assertTrue(RecordKey.of(TestEnum.INVALID$KEY).isEmpty());
		}

	}


	@Test
	void recordKeyEquality_ShouldBeTrueForSameKey()
	{
		Optional<RecordKey> key1 = RecordKey.of("SAME_KEY");
		Optional<RecordKey> key2 = RecordKey.of("SAME_KEY");

		assertTrue(key1.isPresent() && key2.isPresent());
		assertEquals(key1.get(), key2.get());
	}


	@Test
	void recordKeyEquality_ShouldBeFalseForDifferentKeys()
	{
		Optional<RecordKey> key1 = RecordKey.of("KEY_ONE");
		Optional<RecordKey> key2 = RecordKey.of("KEY_TWO");

		assertTrue(key1.isPresent() && key2.isPresent());
		assertNotEquals(key1.get(), key2.get());
	}


	@Test
	void hashCode_ShouldBeConsistentWithEquals()
	{
		Optional<RecordKey> key1 = RecordKey.of("HASH_TEST");
		Optional<RecordKey> key2 = RecordKey.of("HASH_TEST");

		assertTrue(key1.isPresent() && key2.isPresent());
		assertEquals(key1.get().hashCode(), key2.get().hashCode());
	}


	@Test
	void toString_ShouldReturnCorrectKey()
	{
		Optional<RecordKey> key = RecordKey.of("TO_STRING_TEST");
		assertTrue(key.isPresent());
		assertEquals("TO_STRING_TEST", key.get().toString());
	}

	@Nested
	class TestAppend
	{
		@Test
		void testAppend_valid_parameters()
		{
			// Arrange
			RecordKey recordKey = RecordKey.of("RECORD_KEY").orElseThrow();

			// Act
			Optional<RecordKey> recordKey1 = recordKey.append("SUB_KEY");

			// Assert
			assertTrue(recordKey1.isPresent());
		}


		@Test
		void testAppend_invalid_string()
		{
			// Arrange
			RecordKey recordKey = RecordKey.of("RECORD_KEY").orElseThrow();

			// Act
			Optional<RecordKey> recordKey1 = recordKey.append("SUB!KEY");

			// Assert
			assertTrue(recordKey1.isEmpty());
		}


		@Test
		void testAppend_null_enum()
		{
			// Arrange
			RecordKey recordKey = RecordKey.of("RECORD_KEY").orElseThrow();

			// Act
			Optional<RecordKey> recordKey1 = recordKey.append(null);

			// Assert
			assertTrue(recordKey1.isEmpty());
		}
	}

}
