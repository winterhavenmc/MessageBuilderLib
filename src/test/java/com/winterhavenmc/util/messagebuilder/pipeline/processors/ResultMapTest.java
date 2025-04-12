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

package com.winterhavenmc.util.messagebuilder.pipeline.processors;

import com.winterhavenmc.util.messagebuilder.util.RecordKey;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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


	@Test
	void put()
	{
		// Arrange
		RecordKey recordKey = RecordKey.of("abc").orElseThrow();

		// Act
		resultMap.put(recordKey, "123");

		// Assert
		assertTrue(resultMap.containsKey(recordKey));
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
