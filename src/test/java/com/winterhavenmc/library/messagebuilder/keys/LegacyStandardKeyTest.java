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


class LegacyStandardKeyTest
{
	@Nested
	class EqualsTests
	{
		@Test
		void key_equals_same_instance()
		{
			ValidMacroKey key = MacroKey.of("FOO").isValid().orElseThrow();
			assertEquals(key, key);
		}

		@Test
		void key_equals_same_type_and_values_macro()
		{
			ValidMacroKey key1 = MacroKey.of("ABC").isValid().orElseThrow();
			ValidMacroKey key2 = MacroKey.of("ABC").isValid().orElseThrow();
			assertEquals(key1, key2);
		}

//		@Test
//		void key_equals_same_types_and_values_record()
//		{
//			LegacyRecordKey key1 = MacroKey.of("XYZ").orElseThrow();
//			LegacyRecordKey key2 = MacroKey.of("XYZ").orElseThrow();
//			assertEquals(key1, key2);
//		}

		@Test
		void key_not_equals_same_types_different_values_macro()
		{
			ValidMacroKey key1 = MacroKey.of("ABC").isValid().orElseThrow();
			ValidMacroKey key2 = MacroKey.of("XYZ").isValid().orElseThrow();
			assertNotEquals(key1, key2);
		}

		@Test
		void key_not_equals_same_types_different_values_record()
		{
			ValidMacroKey key1 = MacroKey.of("ABC").isValid().orElseThrow();
			ValidMacroKey key2 = MacroKey.of("XYZ").isValid().orElseThrow();
			assertNotEquals(key1, key2);
		}

		@Test
		void key_not_equals_different_types_different_values_macro()
		{
			ValidMacroKey key1 = MacroKey.of("ABC").isValid().orElseThrow();
			ValidMacroKey key2 = MacroKey.of("XYZ").isValid().orElseThrow();
			assertNotEquals(key1, key2);
		}


		@Test
		void key_not_equals_null_macro()
		{
			ValidMacroKey key1 = MacroKey.of("ABC").isValid().orElseThrow();
			assertNotEquals(null, key1);
		}

		@Test
		void key_not_equals_null_record()
		{
			ValidMacroKey key1 = MacroKey.of("ABC").isValid().orElseThrow();
			assertNotEquals(null, key1);
		}

		@Test
		void key_not_equals_different_class_macro()
		{
			ValidMacroKey key1 = MacroKey.of("ABC").isValid().orElseThrow();
			String key2 = "ABC";
			assertNotEquals(key2, key1);
		}

		@Test
		void key_not_equals_different_class_record()
		{
			ValidMacroKey key1 = MacroKey.of("ABC").isValid().orElseThrow();
			String key2 = "ABC";
			assertNotEquals(key2, key1);
		}
	}


	@Test
	void key_hashCode_equal_MacroKeys() {
		ValidMacroKey a = MacroKey.of("MATCH").isValid().orElseThrow();
		ValidMacroKey b = MacroKey.of("MATCH").isValid().orElseThrow();
		assertEquals(a.hashCode(), b.hashCode());
	}

	@Test
	void key_hashCode_equal_RecordKeys() {
		ValidMacroKey a = MacroKey.of("MATCH").isValid().orElseThrow();
		ValidMacroKey b = MacroKey.of("MATCH").isValid().orElseThrow();
		assertEquals(a.hashCode(), b.hashCode());
	}

	@Test
	void ToString_returns_wrapped_string_macro() {
		ValidMacroKey key = MacroKey.of("FOOBAR").isValid().orElseThrow();
		assertEquals("FOOBAR", key.toString());
	}

	@Test
	void toString_returns_wrapped_string_record() {
		ValidMacroKey key = MacroKey.of("LOREM").isValid().orElseThrow();
		assertEquals("LOREM", key.toString());
	}

}
