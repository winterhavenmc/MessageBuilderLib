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

package com.winterhavenmc.util.messagebuilder.keys;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StandardKeyTest {

	@Test
	void testEquals_sameInstance() {
		MacroKey key = MacroKey.of("FOO").orElseThrow();
		assertEquals(key, key);
	}

	@Test
	void testEquals_sameTypeAndValue_macro() {
		assertEquals(MacroKey.of("ABC"), MacroKey.of("ABC"));
	}

	@Test
	void testEquals_sameTypeAndValue_record() {
		assertEquals(RecordKey.of("XYZ"), RecordKey.of("XYZ"));
	}

	@Test
	void testEquals_differentTypeSameValue() {
		assertNotEquals(MacroKey.of("ABC"), RecordKey.of("ABC"));
	}

	@Test
	void testEquals_differentValueSameType_macro() {
		assertNotEquals(MacroKey.of("ABC"), MacroKey.of("DEF"));
	}

	@Test
	void testEquals_differentValueSameType_record() {
		assertNotEquals(RecordKey.of("ABC"), RecordKey.of("DEF"));
	}

	@Test
	void testEquals_null() {
		assertNotEquals(MacroKey.of("ABC").orElseThrow(), null);
	}

	@Test
	void testEquals_differentClass() {
		assertNotEquals(MacroKey.of("ABC").orElseThrow(), "ABC");
	}

	@Test
	void testHashCode_equalMacroKeys() {
		MacroKey a = MacroKey.of("MATCH").orElseThrow();
		MacroKey b = MacroKey.of("MATCH").orElseThrow();
		assertEquals(a.hashCode(), b.hashCode());
	}

	@Test
	void testHashCode_equalRecordKeys() {
		RecordKey a = RecordKey.of("MATCH").orElseThrow();
		RecordKey b = RecordKey.of("MATCH").orElseThrow();
		assertEquals(a.hashCode(), b.hashCode());
	}

	@Test
	void testToString_returnsWrappedString_macro() {
		MacroKey key = MacroKey.of("FOOBAR").orElseThrow();
		assertEquals("FOOBAR", key.toString());
	}

	@Test
	void testToString_returnsWrappedString_record() {
		RecordKey key = RecordKey.of("LOREM").orElseThrow();
		assertEquals("LOREM", key.toString());
	}

	@Test
	void testDotJoin_instanceMethod_macro() {
		MacroKey key = MacroKey.of("BASE").orElseThrow();
		String result = key.dotJoin("CHILD");
		assertEquals("BASE.CHILD", result);
	}

	@Test
	void testDotJoin_instanceMethod_record() {
		RecordKey key = RecordKey.of("PARENT").orElseThrow();
		String result = key.dotJoin("LEAF");
		assertEquals("PARENT.LEAF", result);
	}

	@Test
	void testDotJoin_staticMethod_macro() {
		MacroKey key = MacroKey.of("STATIC").orElseThrow();
		String result = AbstractKey.dotJoin(key, "METHOD");
		assertEquals("STATIC.METHOD", result);
	}

	@Test
	void testDotJoin_staticMethod_record() {
		RecordKey key = RecordKey.of("NODE").orElseThrow();
		String result = AbstractKey.dotJoin(key, "BRANCH");
		assertEquals("NODE.BRANCH", result);
	}
}
