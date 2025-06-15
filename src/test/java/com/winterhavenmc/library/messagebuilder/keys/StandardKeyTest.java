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

class StandardKeyTest
{
	@Nested
	class EqualsTests
	{
		@Test
		void key_equals_same_instance()
		{
			MacroKey key = MacroKey.of("FOO").orElseThrow();
			assertEquals(key, key);
		}

		@Test
		void key_equals_same_type_and_values_macro()
		{
			MacroKey key1 = MacroKey.of("ABC").orElseThrow();
			MacroKey key2 = MacroKey.of("ABC").orElseThrow();
			assertEquals(key1, key2);
		}

		@Test
		void key_equals_same_types_and_values_record()
		{
			RecordKey key1 = RecordKey.of("XYZ").orElseThrow();
			RecordKey key2 = RecordKey.of("XYZ").orElseThrow();
			assertEquals(key1, key2);
		}

		@Test
		void key_not_equals_same_types_different_values_macro()
		{
			MacroKey key1 = MacroKey.of("ABC").orElseThrow();
			MacroKey key2 = MacroKey.of("XYZ").orElseThrow();
			assertNotEquals(key1, key2);
		}

		@Test
		void key_not_equals_same_types_different_values_record()
		{
			RecordKey key1 = RecordKey.of("ABC").orElseThrow();
			RecordKey key2 = RecordKey.of("XYZ").orElseThrow();
			assertNotEquals(key1, key2);
		}

		@Test
		void key_not_equals_different_types_same_values()
		{
			MacroKey key1 = MacroKey.of("ABC").orElseThrow();
			RecordKey key2 = RecordKey.of("ABC").orElseThrow();
			assertNotEquals(key1, key2);
		}

		@Test
		void key_not_equals_different_types_different_values_macro()
		{
			MacroKey key1 = MacroKey.of("ABC").orElseThrow();
			RecordKey key2 = RecordKey.of("XYZ").orElseThrow();
			assertNotEquals(key1, key2);
		}


		@Test
		void key_not_equals_null_macro()
		{
			MacroKey key1 = MacroKey.of("ABC").orElseThrow();
			assertNotEquals(null, key1);
		}

		@Test
		void key_not_equals_null_record()
		{
			RecordKey key1 = RecordKey.of("ABC").orElseThrow();
			assertNotEquals(null, key1);
		}

		@Test
		void key_not_equals_different_class_macro()
		{
			MacroKey key1 = MacroKey.of("ABC").orElseThrow();
			String key2 = "ABC";
			assertNotEquals(key2, key1);
		}

		@Test
		void key_not_equals_different_class_record()
		{
			RecordKey key1 = RecordKey.of("ABC").orElseThrow();
			String key2 = "ABC";
			assertNotEquals(key2, key1);
		}
	}


	@Test
	void key_hashCode_equal_MacroKeys() {
		MacroKey a = MacroKey.of("MATCH").orElseThrow();
		MacroKey b = MacroKey.of("MATCH").orElseThrow();
		assertEquals(a.hashCode(), b.hashCode());
	}

	@Test
	void key_hashCode_equal_RecordKeys() {
		RecordKey a = RecordKey.of("MATCH").orElseThrow();
		RecordKey b = RecordKey.of("MATCH").orElseThrow();
		assertEquals(a.hashCode(), b.hashCode());
	}

	@Test
	void ToString_returns_wrapped_string_macro() {
		MacroKey key = MacroKey.of("FOOBAR").orElseThrow();
		assertEquals("FOOBAR", key.toString());
	}

	@Test
	void toString_returns_wrapped_string_record() {
		RecordKey key = RecordKey.of("LOREM").orElseThrow();
		assertEquals("LOREM", key.toString());
	}

}
