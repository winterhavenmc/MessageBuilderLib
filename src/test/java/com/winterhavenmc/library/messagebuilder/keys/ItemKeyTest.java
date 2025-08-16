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


class ItemKeyTest
{
	@Test
	void of_with_valid_string()
	{
		// Act
		var result = ItemKey.of("VALID_KEY");

		// Assert
		assertInstanceOf(ValidItemKey.class, result);
	}


	@Test
	void of_with_null_string()
	{
		// Act
		var result = ItemKey.of(null);

		// Assert
		assertInstanceOf(InvalidItemKey.class, result);
		assertEquals(InvalidKeyReason.KEY_NULL, ((InvalidItemKey) result).reason());
	}


	@Test
	void of_with_blank_string()
	{
		// Act
		var result = ItemKey.of("");

		// Assert
		assertInstanceOf(InvalidItemKey.class, result);
		assertEquals(InvalidKeyReason.KEY_BLANK, ((InvalidItemKey) result).reason());
	}


	@Test
	void of_with_invalid_string()
	{
		// Act
		var result = ItemKey.of("not-valid-key");

		// Assert
		assertInstanceOf(InvalidItemKey.class, result);
		assertEquals(InvalidKeyReason.KEY_INVALID, ((InvalidItemKey) result).reason());
	}


	@Test
	void isValid_with_valid_key()
	{
		// Act
		var result = ItemKey.of("VALID_KEY");

		// Assert
		assertTrue(result.isValid().isPresent());
	}


	@Test
	void isValid_with_invalid_key()
	{
		// Act
		var result = ItemKey.of(null);

		// Assert
		assertTrue(result.isValid().isEmpty());
	}


	@Nested
	class TestEquality
	{
		@Test
		void ItemKey_equality_true_for_same_key()
		{
			ItemKey key1 = ItemKey.of("SAME_KEY");
			ItemKey key2 = ItemKey.of("SAME_KEY");

			assertEquals(key1, key2);
		}


		@Test
		void ValidItemKey_equality_true_for_same_Key()
		{
			ValidItemKey key1 = ItemKey.of("SAME_KEY").isValid().orElseThrow();
			ValidItemKey key2 = ItemKey.of("SAME_KEY").isValid().orElseThrow();

			assertEquals(key1, key2);
		}


		@Test
		void ItemKey_equality_false_for_different_keys()
		{
			ItemKey key1 = ItemKey.of("KEY_ONE");
			ItemKey key2 = ItemKey.of("KEY_TWO");

			assertNotEquals(key1, key2);
		}
	}


	@Test
	void toString_returns_key()
	{
		// Act
		var result = ItemKey.of("BASE.SUB").isValid().orElseThrow();

		// Assert
		assertEquals("BASE.SUB", result.toString());
	}


	@Test
	void ValidItemKey_hashCode_consistent_with_equals()
	{
		ValidItemKey key1 = ItemKey.of("HASH_TEST").isValid().orElseThrow();
		ValidItemKey key2 = ItemKey.of("HASH_TEST").isValid().orElseThrow();

		assertEquals(key1.hashCode(), key2.hashCode());
	}
}