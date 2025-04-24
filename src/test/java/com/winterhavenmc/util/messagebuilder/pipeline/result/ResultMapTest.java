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

package com.winterhavenmc.util.messagebuilder.pipeline.result;

import com.winterhavenmc.util.messagebuilder.keys.MacroKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class ResultMapTest
{
	ResultMap resultMap;


	@BeforeEach
	void setUp()
	{
		resultMap = new ResultMap();
	}


	@Test @DisplayName("Put method overwrites existing value in result map.")
	void testPut_does_overwrite()
	{
		// Arrange
		MacroKey macroKey = MacroKey.of("abc").orElseThrow();

		// Act
		resultMap.put(macroKey, "first value");
		resultMap.put(macroKey, "second value");

		// Assert
		assertEquals("second value", resultMap.get(macroKey));
	}


	@Test @DisplayName("PutIfAbsent method does not overwrite existing value in result map.")
	void putIfAbsent_does_not_overwrite()
	{
		// Arrange
		MacroKey macroKey = MacroKey.of("KEY").orElseThrow();

		// Act
		resultMap.putIfAbsent(macroKey, "first value");
		resultMap.putIfAbsent(macroKey, "second value");

		// Assert
		assertEquals("first value", resultMap.get(macroKey));
	}


	@Test
	void putIfValid()
	{
		// Arrange
		MacroKey macroKey = MacroKey.of("KEY").orElseThrow();

		// Act
		resultMap.put(macroKey, "first value");
		resultMap.put(macroKey, "second value");

		// Assert
		assertEquals("second value", resultMap.get(macroKey));
	}


	@Test
	void putIfIfAndAbsentAndValid()
	{
		// Arrange
		MacroKey macroKey = MacroKey.of("KEY").orElseThrow();

		// Act
		resultMap.put(macroKey, "first value");
		resultMap.put(macroKey, "second value");

		// Assert
		assertEquals("second value", resultMap.get(macroKey));
	}


	@Test
	void get()
	{
		// Arrange
		MacroKey macroKey = MacroKey.of("abc").orElseThrow();

		// Act
		resultMap.put(macroKey, "123");

		String result = resultMap.get(macroKey);

		assertEquals("123", result);
	}


	@Test
	void putAll()
	{
		// Arrange
		MacroKey macroKey1 = MacroKey.of("abc").orElseThrow();
		MacroKey macroKey2 = MacroKey.of("xyz").orElseThrow();
		MacroKey macroKey3 = MacroKey.of("jkl").orElseThrow();
		ResultMap firstMap = new ResultMap();

		firstMap.put(macroKey1, "123");
		firstMap.put(macroKey2, "1999");

		ResultMap secondMap = new ResultMap();
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
		resultMap.put(macroKey1, "123");
		resultMap.put(macroKey2, "1999");

		var entrySet = resultMap.entrySet();

		assertEquals("[abc=123, xyz=1999]",entrySet.toString());

	}

	@Test
	void isEmpty()
	{
		MacroKey macroKey = MacroKey.of("abc").orElseThrow();
		assertTrue(resultMap.isEmpty());
		resultMap.put(macroKey, "123");
		assertFalse(resultMap.isEmpty());
	}

	@Test
	void getValueOrKey()
	{
		// Arrange
		MacroKey macroKey = MacroKey.of("KEY").orElseThrow();

		// Act
		String result = resultMap.getValueOrKey(macroKey);

		// Assert
		assertEquals("KEY", result);
	}

}
