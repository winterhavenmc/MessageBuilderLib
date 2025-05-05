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

package com.winterhavenmc.library.messagebuilder.pipeline.containers;

import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


class MacroStringMapTest
{
	MacroStringMap macroStringMap;


	@BeforeEach
	void setUp()
	{
		macroStringMap = new MacroStringMap();
	}


	@Test @DisplayName("Put method overwrites existing value in result map.")
	void testPut_does_overwrite()
	{
		// Arrange
		MacroKey macroKey = MacroKey.of("abc").orElseThrow();

		// Act
		macroStringMap.put(macroKey, "first value");
		macroStringMap.put(macroKey, "second value");

		// Assert
		assertEquals("second value", macroStringMap.get(macroKey));
	}


	@Test @DisplayName("PutIfAbsent method does not overwrite existing value in result map.")
	void putIfAbsent_does_not_overwrite()
	{
		// Arrange
		MacroKey macroKey = MacroKey.of("KEY").orElseThrow();

		// Act
		macroStringMap.putIfAbsent(macroKey, "first value");
		macroStringMap.putIfAbsent(macroKey, "second value");

		// Assert
		assertEquals("first value", macroStringMap.get(macroKey));
	}


	@Test
	void putIfValid()
	{
		// Arrange
		MacroKey macroKey = MacroKey.of("KEY").orElseThrow();

		// Act
		macroStringMap.put(macroKey, "first value");
		macroStringMap.put(macroKey, "second value");

		// Assert
		assertEquals("second value", macroStringMap.get(macroKey));
	}


	@Test
	void putIfIfAndAbsentAndValid()
	{
		// Arrange
		MacroKey macroKey = MacroKey.of("KEY").orElseThrow();

		// Act
		macroStringMap.put(macroKey, "first value");
		macroStringMap.put(macroKey, "second value");

		// Assert
		assertEquals("second value", macroStringMap.get(macroKey));
	}


	@Test
	void get()
	{
		// Arrange
		MacroKey macroKey = MacroKey.of("abc").orElseThrow();

		// Act
		macroStringMap.put(macroKey, "123");

		String result = macroStringMap.get(macroKey);

		assertEquals("123", result);
	}


	@Test
	void putAll()
	{
		// Arrange
		MacroKey macroKey1 = MacroKey.of("abc").orElseThrow();
		MacroKey macroKey2 = MacroKey.of("xyz").orElseThrow();
		MacroKey macroKey3 = MacroKey.of("jkl").orElseThrow();
		MacroStringMap firstMap = new MacroStringMap();

		firstMap.put(macroKey1, "123");
		firstMap.put(macroKey2, "1999");

		MacroStringMap secondMap = new MacroStringMap();
		secondMap.putAll(firstMap);

		assertTrue(secondMap.containsKey(macroKey1));
		assertTrue(secondMap.containsKey(macroKey2));
		assertFalse(secondMap.containsKey(macroKey3));
	}


	@Test
	void entrySet()
	{
		MacroKey macroKey1 = MacroKey.of("abc").orElseThrow();
		MacroKey macroKey2 = MacroKey.of("xyz").orElseThrow();
		macroStringMap.put(macroKey1, "123");
		macroStringMap.put(macroKey2, "1999");

		var entrySet = macroStringMap.entrySet();

		assertEquals("[abc=123, xyz=1999]",entrySet.toString());

	}

	@Test
	void isEmpty()
	{
		MacroKey macroKey = MacroKey.of("abc").orElseThrow();
		assertTrue(macroStringMap.isEmpty());
		macroStringMap.put(macroKey, "123");
		assertFalse(macroStringMap.isEmpty());
	}

	@Test
	void getValueOrDefault_valid_entry()
	{
		// Arrange
		MacroKey macroKey = MacroKey.of("abc").orElseThrow();
		macroStringMap.put(macroKey, "123");

		// Act
		String result = macroStringMap.getValueOrDefault(macroKey, macroKey.toString());

		// Assert
		assertEquals("123", result);
	}


	@Test
	void getValueOrDefault_no_entry()
	{
		// Arrange
		MacroKey macroKey = MacroKey.of("DEFAULT").orElseThrow();

		// Act
		String result = macroStringMap.getValueOrDefault(macroKey, macroKey.toString());

		// Assert
		assertEquals("DEFAULT", result);
	}


	@Test
	void keySet()
	{
		// Arrange
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


	@Test
	void testSize()
	{
		// Arrange
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

}
