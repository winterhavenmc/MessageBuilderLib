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

package com.winterhavenmc.util.messagebuilder.macro.processor;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class ResultMapTest {

	ResultMap resultMap;

	@BeforeEach
	void setUp() {
		resultMap = new ResultMap();
	}

	@AfterEach
	void tearDown() {
		resultMap = null;
	}


	@Test
	void empty() {
		assertInstanceOf(ResultMap.class, resultMap);
	}

	@Test
	void put() {
		resultMap.put("abc", "123");
		assertTrue(resultMap.containsKey("abc"));
		assertFalse(resultMap.containsKey("123"));
	}

	@Test
	void get() {
		resultMap.put("abc", "123");

		String result = resultMap.get("abc");

		assertEquals("123", result);
		assertNotEquals("abc", result);
	}

	@Test
	void putAll() {
		ResultMap firstMap = new ResultMap();
		firstMap.put("abc", "123");
		firstMap.put("xyz", "1999");

		ResultMap secondMap = new ResultMap();
		secondMap.putAll(firstMap);

		assertTrue(secondMap.containsKey("abc"));
		assertTrue(secondMap.containsKey("xyz"));
		assertFalse(secondMap.containsKey("jkl"));
	}

	@Test
	void containsKey() {
		resultMap.put("abc", "123");

		assertTrue(resultMap.containsKey("abc"));
		assertFalse(resultMap.containsKey("123"));
	}

	@Test
	void entrySet() {
		resultMap.put("abc", "123");
		resultMap.put("xyz", "1999");

		var entrySet = resultMap.entrySet();

		assertEquals("[abc=123, xyz=1999]",entrySet.toString());

	}

	@Test
	void isEmpty() {

		assertTrue(resultMap.isEmpty());

		resultMap.put("abc", "123");

		assertFalse(resultMap.isEmpty());
	}

}
