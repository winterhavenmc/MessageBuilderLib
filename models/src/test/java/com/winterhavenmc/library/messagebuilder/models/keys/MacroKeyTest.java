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

package com.winterhavenmc.library.messagebuilder.models.keys;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


class MacroKeyTest
{
	enum TestEnum { VALID_KEY, INVALID$KEY }


    @Nested
    class TestStaticFactoryMethod
	{
		@Test
		void of_with_valid_string_returns_ValidMacroKey()
		{
			// Act
			var result = MacroKey.of("VALID_KEY");

			// Assert
			assertInstanceOf(ValidMacroKey.class, result);
		}


		@Test
		void of_with_null_string_returns_InvalidMacroKey()
		{
			// Act
			var result = MacroKey.of((String) null);

			// Assert
			assertInstanceOf(InvalidKey.class, result);
			assertTrue(result.isValid().isEmpty());
			assertEquals(InvalidKeyReason.KEY_NULL, ((InvalidKey) result).Reason());
		}


		@Test
		void of_with_blank_string_returns_InvalidMacroKey()
		{
			// Act
			var result = MacroKey.of("");

			// Assert
			assertInstanceOf(InvalidKey.class, result);
			assertTrue(result.isValid().isEmpty());
			assertEquals(InvalidKeyReason.KEY_BLANK, ((InvalidKey) result).Reason());
		}


		@Test
		void of_with_valid_dotted_string_returns_ValidMacroKey()
		{
			// Act
			var result = MacroKey.of("BASE.SUB");

			// Assert
			assertInstanceOf(ValidMacroKey.class, result);
			assertEquals("BASE.SUB", result.toString());
		}


		@Test
		void of_with_invalid_string_returns_InvalidKey ()
		{
			assertTrue(MacroKey.of("123INVALID").isValid().isEmpty(), "Should return empty for invalid format.");
			assertTrue(MacroKey.of("").isValid().isEmpty(), "Should return empty for empty string.");
			assertTrue(MacroKey.of(" ").isValid().isEmpty(), "Should return empty for whitespace.");
			assertTrue(MacroKey.of("Invalid Key").isValid().isEmpty(), "Should return empty for whitespace.");
			assertTrue(MacroKey.of((String) null).isValid().isEmpty(), "Should return empty for null.");
			assertTrue(MacroKey.of((TestEnum) null).isValid().isEmpty(), "Should return empty for null.");
		}


		@Test
		void of_with_valid_enum_returns_ValidMacroKey()
		{
			// Arrange & Act
			Optional<ValidMacroKey> result = MacroKey.of(TestEnum.VALID_KEY).isValid();

			// Assert
			assertTrue(result.isPresent());
			assertEquals("VALID_KEY", result.get().toString());
		}


		@Test
		void of_with_null_enum_returns_InvalidKey()
		{
			// Act
			var result = MacroKey.of((TestEnum) null);

			// Assert
			assertInstanceOf(InvalidKey.class, result);
			assertTrue(result.isValid().isEmpty());
			assertEquals(InvalidKeyReason.KEY_NULL, ((InvalidKey) result).Reason());
		}


		@Test
		void of_with_invalid_enum_returns_InvalidKey()
		{
			// Act
			var result = MacroKey.of(TestEnum.INVALID$KEY);

			// Assert
			assertInstanceOf(InvalidKey.class, result);
			assertTrue(result.isValid().isEmpty());
			assertEquals(InvalidKeyReason.KEY_INVALID, ((InvalidKey) result).Reason());
		}
	}


	@Nested
	class TestGetBase
	{
		@Test
		void getBase_returns_valid_base_component()
		{
			// Act
			ValidMacroKey macroKey = MacroKey.of("BASE.SUB").isValid().orElseThrow();

			// Assert
			assertEquals("BASE", macroKey.getBase().toString());
		}


		@Test
		void getBase_returns_valid_simple_key()
		{
			// Act
			ValidMacroKey macroKey = MacroKey.of("BASE_NO_SUB").isValid().orElseThrow();

			// Assert
			assertEquals("BASE_NO_SUB", macroKey.getBase().toString());
		}
	}


	@Nested
	class TestEquality
	{
		@Test
		void MacroKey_equality_true_for_same_key()
		{
			MacroKey key1 = MacroKey.of("SAME_KEY");
			MacroKey key2 = MacroKey.of("SAME_KEY");

			assertEquals(key1, key2);
		}


		@Test
		void ValidMacroKey_equality_true_for_same_Key()
		{
			ValidMacroKey key1 = MacroKey.of("SAME_KEY").isValid().orElseThrow();
			ValidMacroKey key2 = MacroKey.of("SAME_KEY").isValid().orElseThrow();

			assertEquals(key1, key2);
		}


		@Test
		void MacroKey_equality_false_for_different_keys()
		{
			MacroKey key1 = MacroKey.of("KEY_ONE");
			MacroKey key2 = MacroKey.of("KEY_TWO");

			assertNotEquals(key1, key2);
		}
	}


	@Test
	void ValidMacroKey_hashCode_consistent_with_equals()
	{
		ValidMacroKey key1 = MacroKey.of("HASH_TEST").isValid().orElseThrow();
		ValidMacroKey key2 = MacroKey.of("HASH_TEST").isValid().orElseThrow();

		assertEquals(key1.hashCode(), key2.hashCode());
	}


	@Test
	void ValidMacroKey_toString_returns_Key()
	{
		ValidMacroKey key = MacroKey.of("TO_STRING_TEST").isValid().orElseThrow();

		assertEquals("TO_STRING_TEST", key.toString());
	}


	@Nested
	class TestAppend
	{
		@Test
		void append_with_valid_string_returns_ValidMacroKey()
		{
			// Arrange
			ValidMacroKey macroKey = MacroKey.of("RECORD_KEY").isValid().orElseThrow();

			// Act
			var result = macroKey.append("SUB_KEY");

			// Assert
			assertInstanceOf(ValidMacroKey.class, result);
			assertTrue(result.isValid().isPresent());
			assertEquals("RECORD_KEY.SUB_KEY", result.toString());
		}


		@Test
		void append_chained_calls_with_valid_strings()
		{
			// Arrange
			ValidMacroKey macroKey = MacroKey.of("PLAYER").isValid().orElseThrow();

			// Act
			ValidMacroKey result = macroKey.append("LOCATION").isValid().orElseThrow().append("X").isValid().orElseThrow();

			// Assert
			assertEquals("PLAYER.LOCATION.X", result.toString());
		}


		@Test
		void testAppend_with_invalid_string()
		{
			// Arrange
			ValidMacroKey macroKey = MacroKey.of("RECORD_KEY").isValid().orElseThrow();

			// Act
			Optional<ValidMacroKey> macroKey1 = macroKey.append("SUB!KEY").isValid();

			// Assert
			assertTrue(macroKey1.isEmpty());
		}


		@Test
		void testAppend_with_null_string()
		{
			// Arrange
			ValidMacroKey macroKey = MacroKey.of("RECORD_KEY").isValid().orElseThrow();

			// Act
			Optional<ValidMacroKey> macroKey1 = macroKey.append((String) null).isValid();

			// Assert
			assertTrue(macroKey1.isEmpty());
		}


		@Test
		void testAppend_with_blank_string()
		{
			// Arrange
			ValidMacroKey macroKey = MacroKey.of("RECORD_KEY").isValid().orElseThrow();

			// Act
			Optional<ValidMacroKey> macroKey1 = macroKey.append("").isValid();

			// Assert
			assertTrue(macroKey1.isEmpty());
		}


		@Test
		void testAppend_with_valid_enum()
		{
			// Arrange
			ValidMacroKey macroKey = MacroKey.of("RECORD_KEY").isValid().orElseThrow();

			// Act
			Optional<ValidMacroKey> macroKey1 = macroKey.append(TestEnum.VALID_KEY).isValid();

			// Assert
			assertTrue(macroKey1.isPresent());
		}


		@Test
		void testAppend_with_invalid_enum()
		{
			// Arrange
			ValidMacroKey macroKey = MacroKey.of("RECORD_KEY").isValid().orElseThrow();

			// Act
			Optional<ValidMacroKey> macroKey1 = macroKey.append(TestEnum.INVALID$KEY).isValid();

			// Assert
			assertTrue(macroKey1.isEmpty());
		}


		@Test
		void testAppend_with_null_enum()
		{
			// Arrange
			ValidMacroKey macroKey = MacroKey.of("RECORD_KEY").isValid().orElseThrow();

			// Act
			Optional<ValidMacroKey> macroKey1 = macroKey.append((Enum) null).isValid();

			// Assert
			assertTrue(macroKey1.isEmpty());
		}

	}


	@Test
	void getBase_with_simple_key()
	{
		// Arrange
		ValidMacroKey macroKey = MacroKey.of("PLAYER").isValid().orElseThrow();
		ValidMacroKey expected = MacroKey.of("PLAYER").isValid().orElseThrow();

		// Act
		ValidMacroKey result = macroKey.getBase();

		// Assert
		assertEquals(expected, result);
	}


	@Test
	void getBase_with_compound_key()
	{
		// Arrange
		ValidMacroKey macroKey1 = MacroKey.of("PLAYER.NAME").isValid().orElseThrow();
		ValidMacroKey macroKey2 = MacroKey.of("PLAYER.LOCATION.WORLD").isValid().orElseThrow();
		ValidMacroKey expected = MacroKey.of("PLAYER").isValid().orElseThrow();

		// Act
		ValidMacroKey result1 = macroKey1.getBase();
		ValidMacroKey result2 = macroKey2.getBase();

		// Assert
		assertEquals(expected, result1);
		assertEquals(expected, result2);
	}


	@Test
	void asPlaceholder_returns_key_with_delimiters()
	{
		// Arrange
		ValidMacroKey macroKey = MacroKey.of("PLAYER.NAME").isValid().orElseThrow();

		// Act
		String placeholder = macroKey.asPlaceholder();

		// Assert
		assertEquals("{PLAYER.NAME}", placeholder);
	}

}
