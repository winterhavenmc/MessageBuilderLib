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

package com.winterhavenmc.util.messagebuilder.context;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SourceKeyTest {

	@Test
	void testConstructor() {
		SourceKey sourceKey = new SourceKey(Source.CONST, "path/to/constant");
		assertNotNull(sourceKey);
		assertEquals(Source.CONST, sourceKey.getSource());
		assertEquals("path/to/constant", sourceKey.getKeyPath());
	}

	@Test
	void testGetKey() {
		SourceKey sourceKey = new SourceKey(Source.ITEM, "item/path");
		String expectedKey = "ITEM|item/path";
		assertEquals(expectedKey, sourceKey.getKey());
	}

	@Test
	void testToString() {
		SourceKey sourceKey = new SourceKey(Source.MACRO, "macro/definition");
		String expectedToString = "MACRO|macro/definition";
		assertEquals(expectedToString, sourceKey.toString());
	}

	@Test
	void testEqualsSameObject() {
		SourceKey sourceKey = new SourceKey(Source.CONST, "constant/path");
		assertEquals(sourceKey, sourceKey);
	}

	@Test
	void testEqualsEqualObjects() {
		SourceKey key1 = new SourceKey(Source.CONST, "constant/path");
		SourceKey key2 = new SourceKey(Source.CONST, "constant/path");
		assertEquals(key1, key2);
	}

	@Test
	void testEqualsDifferentSource() {
		SourceKey key1 = new SourceKey(Source.CONST, "common/path");
		SourceKey key2 = new SourceKey(Source.ITEM, "common/path");
		assertNotEquals(key1, key2);
	}

	@Test
	void testEqualsDifferentKeyPath() {
		SourceKey key1 = new SourceKey(Source.MACRO, "macro/path");
		SourceKey key2 = new SourceKey(Source.MACRO, "different/path");
		assertNotEquals(key1, key2);
	}

	@Test
	void testEqualsNullObject() {
		SourceKey sourceKey = new SourceKey(Source.ITEM, "item/path");
		assertNotEquals(sourceKey, null);
	}

	@Test
	void testEqualsDifferentType() {
		SourceKey sourceKey = new SourceKey(Source.CONST, "constant/path");
		String differentType = "Not a SourceKey";
		assertNotEquals(sourceKey, differentType);
	}

	@Test
	void testHashCodeEqualObjects() {
		SourceKey key1 = new SourceKey(Source.CONST, "constant/path");
		SourceKey key2 = new SourceKey(Source.CONST, "constant/path");
		assertEquals(key1.hashCode(), key2.hashCode());
	}

	@Test
	void testHashCodeDifferentSource() {
		SourceKey key1 = new SourceKey(Source.ITEM, "common/path");
		SourceKey key2 = new SourceKey(Source.MACRO, "common/path");
		assertNotEquals(key1.hashCode(), key2.hashCode());
	}

	@Test
	void testHashCodeDifferentKeyPath() {
		SourceKey key1 = new SourceKey(Source.CONST, "constant/path");
		SourceKey key2 = new SourceKey(Source.CONST, "different/path");
		assertNotEquals(key1.hashCode(), key2.hashCode());
	}
}
