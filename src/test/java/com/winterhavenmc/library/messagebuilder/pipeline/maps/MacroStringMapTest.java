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

package com.winterhavenmc.library.messagebuilder.pipeline.maps;

import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


class MacroStringMapTest
{
	@Test @DisplayName("Put method overwrites existing value in result map.")
	void put_overwrites_existing_entry()
	{
		// Arrange
		MacroStringMap macroStringMap = new MacroStringMap();
		MacroKey macroKey = MacroKey.of("ABC").orElseThrow();


		// Act
		macroStringMap.put(macroKey, "first value");
		macroStringMap.put(macroKey, "second value");

		// Assert
		assertEquals("second value", macroStringMap.get(macroKey));
	}


	@Test @DisplayName("PutIfAbsent inserts key/value pair if not already present in result map.")
	void putIfAbsent_inserts_key_value_pair_if_not_already_present()
	{
		// Arrange
		MacroStringMap macroStringMap = new MacroStringMap();
		MacroKey key1 = MacroKey.of("KEY").orElseThrow();
		MacroKey key2 = MacroKey.of("KEY2").orElseThrow();

		// Act
		macroStringMap.put(key1, "first value");
		macroStringMap.put(key2, "second value");

		// Assert
		assertEquals("first value", macroStringMap.get(key1));
		assertEquals("second value", macroStringMap.get(key2));
	}


	@Test @DisplayName("PutIfAbsent method does not overwrite existing value in result map.")
	void putIfAbsent_does_not_overwrite_existing_entry()
	{
		// Arrange
		MacroStringMap macroStringMap = new MacroStringMap();
		MacroKey macroKey = MacroKey.of("KEY").orElseThrow();

		// Act
		macroStringMap.putIfAbsent(macroKey, "first value");
		macroStringMap.putIfAbsent(macroKey, "second value");

		// Assert
		assertEquals("first value", macroStringMap.get(macroKey));
	}


	@Test @DisplayName("get retrieves value for key in map.")
	void get_retrieves_value_for_key_from_map()
	{
		// Arrange
		MacroStringMap macroStringMap = new MacroStringMap();
		MacroKey macroKey = MacroKey.of("ABC").orElseThrow();

		// Act
		macroStringMap.put(macroKey, "123");

		String result = macroStringMap.get(macroKey);

		assertEquals("123", result);
	}


	@Test @DisplayName("get returns null for key not in map.")
	void get_returns_null_for_nonexistent_entry_in_map()
	{
		// Arrange
		MacroStringMap macroStringMap = new MacroStringMap();
		MacroKey macroKey = MacroKey.of("ABC").orElseThrow();
		MacroKey macroKey2 = MacroKey.of("ABCD").orElseThrow();

		// Act
		macroStringMap.put(macroKey, "123");

		String result = macroStringMap.get(macroKey2);

		assertNull(result);
	}


	@Test @DisplayName("putAll inserts all entries of passed map into existing map.")
	void putAll_inserts_all_map_entries_into_existing_map()
	{
		// Arrange
		MacroKey macroKey1 = MacroKey.of("ABC").orElseThrow();
		MacroKey macroKey2 = MacroKey.of("XYZ").orElseThrow();
		MacroKey macroKey3 = MacroKey.of("JKL").orElseThrow();
		MacroStringMap firstMap = new MacroStringMap();

		firstMap.put(macroKey1, "123");
		firstMap.put(macroKey2, "1999");

		MacroStringMap secondMap = new MacroStringMap();
		secondMap.putAll(firstMap);

		assertTrue(secondMap.containsKey(macroKey1));
		assertTrue(secondMap.containsKey(macroKey2));
		assertFalse(secondMap.containsKey(macroKey3));
	}


	@Test @DisplayName("entrySet returns set of map entries")
	void entrySet_returns_set_of_map_entries()
	{
		MacroStringMap macroStringMap = new MacroStringMap();
		MacroKey macroKey1 = MacroKey.of("ABC").orElseThrow();
		MacroKey macroKey2 = MacroKey.of("XYZ").orElseThrow();
		macroStringMap.put(macroKey1, "123");
		macroStringMap.put(macroKey2, "1999");

		var entrySet = macroStringMap.entrySet();

		assertEquals("[ABC=123, XYZ=1999]",entrySet.toString());

	}

	@Test @DisplayName("isEmpty returns true if map contains no entries.")
	void isEmpty_returns_true_if_map_contains_no_entries()
	{
		MacroStringMap macroStringMap = new MacroStringMap();
		MacroKey macroKey = MacroKey.of("ABC").orElseThrow();
		assertTrue(macroStringMap.isEmpty());
		macroStringMap.put(macroKey, "123");
		assertFalse(macroStringMap.isEmpty());
	}


	@Test @DisplayName("keySet returns set of map keys.")
	void keySet_returns_set_of_map_keys()
	{
		// Arrange
		MacroStringMap macroStringMap = new MacroStringMap();
		MacroKey macroKey1 = MacroKey.of("KEY1").orElseThrow();
		MacroKey macroKey2 = MacroKey.of("KEY2").orElseThrow();
		MacroKey macroKey3 = MacroKey.of("KEY3").orElseThrow();
		macroStringMap.put(macroKey1, "red");
		macroStringMap.put(macroKey2, "blue");

		// Act
		Set<MacroKey> result = macroStringMap.keySet();

		// Assert
		assertTrue(result.contains(macroKey1));
		assertTrue(result.contains(macroKey2));
		assertFalse(result.contains(macroKey3));
	}


	@Test @DisplayName("size returns number of map entries as int.")
	void size_returns_int_number_of_map_entries()
	{
		// Arrange
		MacroStringMap macroStringMap = new MacroStringMap();
		MacroKey macroKey1 = MacroKey.of("KEY1").orElseThrow();
		MacroKey macroKey2 = MacroKey.of("KEY2").orElseThrow();
		MacroKey macroKey3 = MacroKey.of("KEY3").orElseThrow();
		macroStringMap.put(macroKey1, "red");
		macroStringMap.put(macroKey2, "green");
		macroStringMap.put(macroKey3, "blue");

		// Act
		int result = macroStringMap.size();

		// Assert
		assertEquals(3, result);

	}

	@Test @DisplayName("empty returns new instance of map with no entries.")
	void empty_returns_new_empty_instance_of_map()
	{
		// Arrange & Act
		MacroStringMap result = MacroStringMap.empty();

		// Assert
		assertTrue(result.isEmpty());
	}

	@Test @DisplayName("with returns existing map with key/value pair added.")
	void with_returns_map_with_key_value_pair_added()
	{
		// Arrange
		MacroKey key1 = MacroKey.of("KEY1").orElseThrow();
		MacroKey key2 = MacroKey.of("KEY2").orElseThrow();
		MacroStringMap map = new MacroStringMap();
		map.put(key1, "value");

		// Act
		MacroStringMap result = map.with(key2, "new_value");

		// Assert
		assertEquals(2, result.size());
		assertEquals("value", result.get(key1));
		assertEquals("new_value", result.get(key2));
		// original map has entry added
		assertEquals(2, map.size());
		assertEquals("value", map.get(key1));
		assertEquals("new_value", map.get(key2));
	}

}
