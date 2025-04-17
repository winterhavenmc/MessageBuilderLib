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

package com.winterhavenmc.util.messagebuilder.keys;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.winterhavenmc.util.messagebuilder.keys.MacroKeyTest.TestEnum.VALID_KEY;
import static org.junit.jupiter.api.Assertions.*;


class MacroKeyTest
{
	enum TestEnum
	{
		INVALID$KEY,
		VALID_KEY,
	}


    @Nested
    class TestStaticFactoryMethod
	{
		@Test
		void testOf_valid_string ()
		{
			// Arrange & Act
			Optional<MacroKey> result = MacroKey.of("VALID_KEY");

			// Assert
			assertTrue(result.isPresent());
			assertEquals("VALID_KEY", result.get().toString());
		}


		@Test
		void testOf_null_string ()
		{
			assertTrue(MacroKey.of((String) null).isEmpty());
		}


		@Test
		void testOf_invalid_strings ()
		{
			assertTrue(MacroKey.of("123INVALID").isEmpty(), "Should return empty for invalid format.");
			assertTrue(MacroKey.of("").isEmpty(), "Should return empty for empty string.");
			assertTrue(MacroKey.of(" ").isEmpty(), "Should return empty for whitespace.");
			assertTrue(MacroKey.of("Invalid Key").isEmpty(), "Should return empty for whitespace.");
			assertTrue(MacroKey.of((String) null).isEmpty(), "Should return empty for null.");
			assertTrue(MacroKey.of((TestEnum) null).isEmpty(), "Should return empty for null.");
		}


		@Test
		void testOf_valid_enum()
		{
			// Arrange & Act
			Optional<MacroKey> result = MacroKey.of(VALID_KEY);

			// Assert
			assertTrue(result.isPresent());
			assertEquals("VALID_KEY", result.get().toString());
		}


		@Test
		void testOf_null_enum ()
		{
			assertTrue(MacroKey.of((TestEnum) null).isEmpty());
		}


		@Test
		void testOf_invalid_enum ()
		{
			assertTrue(MacroKey.of(TestEnum.INVALID$KEY).isEmpty());
		}

	}


	@Test
	void macroKeyEquality_ShouldBeTrueForSameKey()
	{
		Optional<MacroKey> key1 = MacroKey.of("SAME_KEY");
		Optional<MacroKey> key2 = MacroKey.of("SAME_KEY");

		assertTrue(key1.isPresent() && key2.isPresent());
		assertEquals(key1.get(), key2.get());
	}


	@Test
	void macroKeyEquality_ShouldBeFalseForDifferentKeys()
	{
		Optional<MacroKey> key1 = MacroKey.of("KEY_ONE");
		Optional<MacroKey> key2 = MacroKey.of("KEY_TWO");

		assertTrue(key1.isPresent() && key2.isPresent());
		assertNotEquals(key1.get(), key2.get());
	}


	@Test
	void hashCode_ShouldBeConsistentWithEquals()
	{
		Optional<MacroKey> key1 = MacroKey.of("HASH_TEST");
		Optional<MacroKey> key2 = MacroKey.of("HASH_TEST");

		assertTrue(key1.isPresent() && key2.isPresent());
		assertEquals(key1.get().hashCode(), key2.get().hashCode());
	}


	@Test
	void toString_ShouldReturnCorrectKey()
	{
		Optional<MacroKey> key = MacroKey.of("TO_STRING_TEST");
		assertTrue(key.isPresent());
		assertEquals("TO_STRING_TEST", key.get().toString());
	}

	@Nested
	class TestAppend
	{
		@Test
		void testAppend_valid_string_parameter()
		{
			// Arrange
			MacroKey macroKey = MacroKey.of("RECORD_KEY").orElseThrow();

			// Act
			Optional<MacroKey> macroKey1 = macroKey.append("SUB_KEY");

			// Assert
			assertTrue(macroKey1.isPresent());
		}


		@Test
		void append_chained_calls_with_valid_strings()
		{
			// Arrange
			MacroKey macroKey = MacroKey.of("PLAYER").orElseThrow();

			// Act
			MacroKey result = macroKey.append("LOCATION").orElseThrow().append("X").orElseThrow();

			// Assert
			assertEquals("PLAYER.LOCATION.X", result.toString());
		}


		@Test
		void testAppend_invalid_string()
		{
			// Arrange
			MacroKey macroKey = MacroKey.of("RECORD_KEY").orElseThrow();

			// Act
			Optional<MacroKey> macroKey1 = macroKey.append("SUB!KEY");

			// Assert
			assertTrue(macroKey1.isEmpty());
		}


		@Test
		void testAppend_null_string()
		{
			// Arrange
			MacroKey macroKey = MacroKey.of("RECORD_KEY").orElseThrow();

			// Act
			Optional<MacroKey> macroKey1 = macroKey.append((String) null);

			// Assert
			assertTrue(macroKey1.isEmpty());
		}


		@Test
		void testAppend_valid_enum()
		{
			// Arrange
			MacroKey macroKey = MacroKey.of("RECORD_KEY").orElseThrow();

			// Act
			Optional<MacroKey> macroKey1 = macroKey.append(VALID_KEY);

			// Assert
			assertTrue(macroKey1.isPresent());
		}


		@Test
		void testAppend_invalid_enum()
		{
			// Arrange
			MacroKey macroKey = MacroKey.of("RECORD_KEY").orElseThrow();

			// Act
			Optional<MacroKey> macroKey1 = macroKey.append(TestEnum.INVALID$KEY);

			// Assert
			assertTrue(macroKey1.isEmpty());
		}


		@Test
		void testAppend_null_enum()
		{
			// Arrange
			MacroKey macroKey = MacroKey.of("RECORD_KEY").orElseThrow();

			// Act
			Optional<MacroKey> macroKey1 = macroKey.append((Enum) null);

			// Assert
			assertTrue(macroKey1.isEmpty());
		}

	}

}
