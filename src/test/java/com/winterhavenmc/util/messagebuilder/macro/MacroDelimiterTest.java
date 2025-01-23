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

package com.winterhavenmc.util.messagebuilder.macro;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class MacroDelimiterTest {

	@BeforeEach
	void setUp() {
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void getDefaultChar() {
		assertEquals('{', MacroDelimiter.OPEN.getDefaultChar());
		assertEquals('}', MacroDelimiter.CLOSE.getDefaultChar());
	}

	@Test
	void set() {
		MacroDelimiter.OPEN.set('<');
		MacroDelimiter.CLOSE.set('>');

		// assert
		assertEquals('<', MacroDelimiter.OPEN.toChar());
		assertEquals('>', MacroDelimiter.CLOSE.toChar());
	}

	@Test
	void testToString() {
		assertEquals("{", MacroDelimiter.OPEN.toString());
		assertEquals("}", MacroDelimiter.CLOSE.toString());
	}

	@Test
	void toChar() {
		// Arrange
		MacroDelimiter.OPEN.set('%');
		MacroDelimiter.CLOSE.set('%');

		// Act & Assert
		assertEquals('%', MacroDelimiter.OPEN.toChar());
		assertEquals('%', MacroDelimiter.CLOSE.toChar());
	}

	@Test
	void values() {
		// Arrange & Act
		MacroDelimiter[] macroDelimiters = MacroDelimiter.values();

		// Assert
		assertEquals(MacroDelimiter.OPEN, macroDelimiters[0]);
		assertEquals(MacroDelimiter.CLOSE, macroDelimiters[1]);
	}

	@Test
	void valueOf() {
		assertEquals(MacroDelimiter.OPEN, MacroDelimiter.valueOf("OPEN"));
		assertEquals(MacroDelimiter.CLOSE, MacroDelimiter.valueOf("CLOSE"));
	}

}
