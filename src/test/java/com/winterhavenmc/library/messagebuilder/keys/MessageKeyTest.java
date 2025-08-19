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

import static org.junit.jupiter.api.Assertions.*;


class MessageKeyTest
{
	enum TestEnum
	{
		invalid_constant_name,
		VALID_CONSTANT_NAME
	}


	@Test
	void of_with_valid_string()
	{
		// Act
		var result = MessageKey.of("VALID_KEY");

		// Assert
		assertInstanceOf(ValidMessageKey.class, result);
	}


	@Test
	void of_with_null_string()
	{
		// Act
		var result = MessageKey.of((String) null);

		// Assert
		assertInstanceOf(InvalidMessageKey.class, result);
		assertEquals(InvalidKeyReason.KEY_NULL, ((InvalidMessageKey) result).reason());
	}


	@Test
	void of_with_blank_string()
	{
		// Act
		var result = MessageKey.of("");

		// Assert
		assertInstanceOf(InvalidMessageKey.class, result);
		assertEquals(InvalidKeyReason.KEY_BLANK, ((InvalidMessageKey) result).reason());
	}


	@Test
	void of_with_invalid_string()
	{
		// Act
		var result = MessageKey.of("not-valid-key");

		// Assert
		assertInstanceOf(InvalidMessageKey.class, result);
		assertEquals(InvalidKeyReason.KEY_INVALID, ((InvalidMessageKey) result).reason());
	}


	@Test
	void of_with_valid_ENUM()
	{
		// Act
		var result = MessageKey.of(TestEnum.VALID_CONSTANT_NAME);

		// Assert
		assertInstanceOf(ValidMessageKey.class, result);
	}


	@Test
	void of_with_null_enum()
	{
		// Act
		var result = MessageKey.of((TestEnum) null);

		// Assert
		assertInstanceOf(InvalidMessageKey.class, result);
		assertEquals(InvalidKeyReason.KEY_NULL, ((InvalidMessageKey) result).reason());
	}


	@Test
	void of_with_invalid_enum()
	{
		// Act
		var result = MessageKey.of(TestEnum.invalid_constant_name);

		// Assert
		assertInstanceOf(InvalidMessageKey.class, result);
		assertEquals(InvalidKeyReason.KEY_INVALID, ((InvalidMessageKey) result).reason());
	}


	@Test
	void isValid_with_valid_key()
	{
		// Act
		var result = MessageKey.of("VALID_KEY");

		// Assert
		assertTrue(result.isValid().isPresent());
	}


	@Test
	void isValid_with_invalid_key()
	{
		// Act
		var result = MessageKey.of((String) null);

		// Assert
		assertTrue(result.isValid().isEmpty());
	}


	@Test
	void toString_returns_key()
	{
		// Act
		var result = MessageKey.of("BASE.SUB").isValid().orElseThrow();

		// Assert
		assertEquals("BASE.SUB", result.toString());
	}


	@Nested
	class TestEquality
	{
		@Test
		void MessageKey_equality_true_for_same_key()
		{
			MessageKey key1 = MessageKey.of("SAME_KEY");
			MessageKey key2 = MessageKey.of("SAME_KEY");

			// Assert
			assertEquals(key1, key2);
		}


		@Test
		void ValidMessageKey_equality_true_for_same_Key()
		{
			ValidMessageKey key1 = MessageKey.of("SAME_KEY").isValid().orElseThrow();
			ValidMessageKey key2 = MessageKey.of("SAME_KEY").isValid().orElseThrow();

			// Assert
			assertEquals(key1, key2);
		}


		@Test
		void MessageKey_equality_false_for_different_keys()
		{
			// Arrange & Act
			MessageKey key1 = MessageKey.of("KEY_ONE");
			MessageKey key2 = MessageKey.of("KEY_TWO");

			// Assert
			assertNotEquals(key1, key2);
		}
	}


	@Test
	void ValidMessageKey_hashCode_consistent_with_equals()
	{
		// Arrange & Act
		ValidMessageKey key1 = MessageKey.of("HASH_TEST").isValid().orElseThrow();
		ValidMessageKey key2 = MessageKey.of("HASH_TEST").isValid().orElseThrow();

		// Assert
		assertEquals(key1.hashCode(), key2.hashCode());
	}
}