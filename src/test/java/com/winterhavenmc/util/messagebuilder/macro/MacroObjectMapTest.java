/*
 * Copyright (c) 2022 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.macro;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MacroObjectMapTest {

	@Test
	void put() {
		String key = "TEST_KEY";
		MacroObjectMap objectMap = new MacroObjectMap();
		objectMap.put(key, 42);
		assertTrue(objectMap.containsKey(key));
		assertEquals(42, objectMap.get(key));
	}

	@Test
	void get() {
		String key = "TEST_KEY";
		MacroObjectMap objectMap = new MacroObjectMap();
		objectMap.put(key, 42);
		assertTrue(objectMap.containsKey(key));
		assertEquals(42, objectMap.get(key));
	}

	@Test
	void containsKey() {
		String key = "TEST_KEY";
		MacroObjectMap objectMap = new MacroObjectMap();
		objectMap.put(key, 42);
		assertTrue(objectMap.containsKey(key));
		assertEquals(42, objectMap.get(key));
	}

	@Test
	void entrySet() {
		String key = "TEST_KEY";
		MacroObjectMap objectMap = new MacroObjectMap();
		objectMap.put(key, 42);
		assertTrue(objectMap.containsKey(key));
		assertFalse(objectMap.entrySet().isEmpty());
	}

}
