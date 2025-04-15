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

import com.winterhavenmc.util.messagebuilder.recordkey.RecordKey;
import org.junit.jupiter.api.AfterEach;
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


	@AfterEach
	void tearDown()
	{
		resultMap = null;
	}


	@Test
	void empty()
	{
		assertInstanceOf(ResultMap.class, resultMap);
	}


	@Test @DisplayName("Put method overwrites existing value in result map.")
	void testPut_does_overwrite()
	{
		// Arrange
		RecordKey recordKey = RecordKey.of("abc").orElseThrow();

		// Act
		resultMap.put(recordKey, "first value");
		resultMap.put(recordKey, "second value");

		// Assert
		assertEquals("first second", resultMap.get(recordKey));
	}


	@Test @DisplayName("PutIfAbsent method does not overwrite existing value in result map.")
	void putIfAbsent_does_not_overwrite()
	{
		// Arrange
		RecordKey recordKey = RecordKey.of("KEY").orElseThrow();

		// Act
		resultMap.putIfAbsent(recordKey, "first value");
		resultMap.putIfAbsent(recordKey, "second value");

		// Assert
		assertEquals("first value", resultMap.get(recordKey));
	}


	@Test
	void putIfValid()
	{
		// Arrange
		RecordKey recordKey = RecordKey.of("KEY").orElseThrow();

		// Act
		resultMap.put(recordKey, "first value");
		resultMap.put(recordKey, "second value");

		// Assert
		assertEquals("first second", resultMap.get(recordKey));
	}


	@Test
	void putIfIfAndAbsentAndValid()
	{
		// Arrange
		RecordKey recordKey = RecordKey.of("KEY").orElseThrow();

		// Act
		resultMap.put(recordKey, "first value");
		resultMap.put(recordKey, "second value");

		// Assert
		assertEquals("first second", resultMap.get(recordKey));
	}


	@Test
	void get()
	{
		// Arrange
		RecordKey recordKey = RecordKey.of("abc").orElseThrow();

		// Act
		resultMap.put(recordKey, "123");

		String result = resultMap.get(recordKey);

		assertEquals("123", result);
	}


	@Test
	void putAll()
	{
		// Arrange
		RecordKey recordKey1 = RecordKey.of("abc").orElseThrow();
		RecordKey recordKey2 = RecordKey.of("xyz").orElseThrow();
		RecordKey recordKey3 = RecordKey.of("jkl").orElseThrow();
		ResultMap firstMap = new ResultMap();

		firstMap.put(recordKey1, "123");
		firstMap.put(recordKey2, "1999");

		ResultMap secondMap = new ResultMap();
		secondMap.putAll(firstMap);

		assertTrue(secondMap.containsKey(recordKey1));
		assertTrue(secondMap.containsKey(recordKey2));
		assertFalse(secondMap.containsKey(recordKey3));
	}


	@Test
	void entrySet()
	{
		RecordKey recordKey1 = RecordKey.of("abc").orElseThrow();
		RecordKey recordKey2 = RecordKey.of("xyz").orElseThrow();
		resultMap.put(recordKey1, "123");
		resultMap.put(recordKey2, "1999");

		var entrySet = resultMap.entrySet();

		assertEquals("[abc=123, xyz=1999]",entrySet.toString());

	}

	@Test
	void isEmpty()
	{
		RecordKey recordKey = RecordKey.of("abc").orElseThrow();
		assertTrue(resultMap.isEmpty());
		resultMap.put(recordKey, "123");
		assertFalse(resultMap.isEmpty());
	}

}
