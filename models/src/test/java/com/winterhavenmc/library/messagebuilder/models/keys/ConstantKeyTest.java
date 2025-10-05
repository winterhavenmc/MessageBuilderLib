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

import static org.junit.jupiter.api.Assertions.*;


class ConstantKeyTest
{
	@Test
	void of_with_valid_string()
	{
		// Act
		var result = ConstantKey.of("VALID_KEY");

		// Assert
		assertInstanceOf(ValidConstantKey.class, result);
	}


	@Test
	void of_with_null_string()
	{
		// Act
		var result = ConstantKey.of(null);

		// Assert
		assertInstanceOf(InvalidConstantKey.class, result);
		assertEquals(InvalidKeyReason.KEY_NULL, ((InvalidConstantKey) result).reason());
	}


	@Test
	void of_with_blank_string()
	{
		// Act
		var result = ConstantKey.of("");

		// Assert
		assertInstanceOf(InvalidConstantKey.class, result);
		assertEquals(InvalidKeyReason.KEY_BLANK, ((InvalidConstantKey) result).reason());
	}


	@Test
	void of_with_invalid_string()
	{
		// Act
		var result = ConstantKey.of("not-valid-key");

		// Assert
		assertInstanceOf(InvalidConstantKey.class, result);
		assertEquals(InvalidKeyReason.KEY_INVALID, ((InvalidConstantKey) result).reason());
	}


	@Test
	void of_with_invalid_strings ()
	{
		assertTrue(ConstantKey.of("123INVALID").isValid().isEmpty(), "Should return empty for invalid format.");
		assertTrue(ConstantKey.of("").isValid().isEmpty(), "Should return empty for empty string.");
		assertTrue(ConstantKey.of(" ").isValid().isEmpty(), "Should return empty for whitespace.");
		assertTrue(ConstantKey.of("Invalid Key").isValid().isEmpty(), "Should return empty for whitespace.");
		assertTrue(ConstantKey.of(null).isValid().isEmpty(), "Should return empty for null.");
	}



	@Test
	void isValid_with_valid_key()
	{
		// Act
		var result = ConstantKey.of("VALID_KEY");

		// Assert
		assertTrue(result.isValid().isPresent());
	}


	@Test
	void isValid_with_invalid_key()
	{
		// Act
		var result = ConstantKey.of(null);

		// Assert
		assertTrue(result.isValid().isEmpty());
	}

	@Nested
	class TestEquality
	{
		@Test
		void ConstantKey_equality_true_for_same_key()
		{
			ConstantKey key1 = ConstantKey.of("SAME_KEY");
			ConstantKey key2 = ConstantKey.of("SAME_KEY");

			assertEquals(key1, key2);
		}


		@Test
		void ValidConstantKey_equality_true_for_same_Key()
		{
			ValidConstantKey key1 = ConstantKey.of("SAME_KEY").isValid().orElseThrow();
			ValidConstantKey key2 = ConstantKey.of("SAME_KEY").isValid().orElseThrow();

			assertEquals(key1, key2);
		}


		@Test
		void ConstantKey_equality_false_for_different_keys()
		{
			ConstantKey key1 = ConstantKey.of("KEY_ONE");
			ConstantKey key2 = ConstantKey.of("KEY_TWO");

			assertNotEquals(key1, key2);
		}
	}


	@Test
	void toString_returns_key()
	{
		// Act
		var result = ConstantKey.of("BASE.SUB").isValid().orElseThrow();

		// Assert
		assertEquals("BASE.SUB", result.toString());
	}


	@Test
	void ValidConstantKey_hashCode_consistent_with_equals()
	{
		ValidConstantKey key1 = ConstantKey.of("HASH_TEST").isValid().orElseThrow();
		ValidConstantKey key2 = ConstantKey.of("HASH_TEST").isValid().orElseThrow();

		assertEquals(key1.hashCode(), key2.hashCode());
	}

}
