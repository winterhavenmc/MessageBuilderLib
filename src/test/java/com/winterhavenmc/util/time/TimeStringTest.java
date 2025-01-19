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

package com.winterhavenmc.util.time;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class TimeStringTest {

	@Test
	void getTimeString() {
		// Arrange
		long millis = 1000 * 2 * 60 * 60 - 1001; // 2 hours - (1 second + 1 millisecond)

		// Act & Assert
		assertEquals("1 hour, 59 minutes, and 59 seconds", TimeString.getTimeString(millis));
	}

	@Test
	void getTimeString_using_language_tag() {
		// Arrange
		long millis = 1000 * 2 * 60 * 60 - 1001; // 2 hours - (1 second + 1 millisecond)

		// Act & Assert
		assertEquals("1 hour, 59 minutes, and 59 seconds", TimeString.getTimeString("en-US", millis));
		assertEquals("1 hour, 59 minutes and 59 seconds", TimeString.getTimeString("en-GB", millis));
		assertEquals("1 Stunde, 59 Minuten und 59 Sekunden", TimeString.getTimeString("de-DE", millis));
	}

	@Test
	void getTimeString_using_locale() {
		// Arrange
		long millis = 1000 * 2 * 60 * 60 - 1001; // 2 hours - (1 second + 1 millisecond)

		// Act & Assert
		assertEquals("1 hour, 59 minutes, and 59 seconds", TimeString.getTimeString(Locale.US, millis));
		assertEquals("1 hour, 59 minutes and 59 seconds", TimeString.getTimeString(Locale.UK, millis));
		assertEquals("1 ora, 59 minuti e 59 secondi", TimeString.getTimeString(Locale.ITALIAN, millis));
	}

	@Test
	void getTimeString_null_parameter_locale() {
		// Arrange
		long millis = 1000 * 2 * 60 * 60 - 1001; // 2 hours - (1 second + 1 millisecond)

		// Act
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> TimeString.getTimeString((Locale) null, millis));

		// Assert
		assertEquals("The locale parameter cannot be null.", exception.getMessage());
	}

	@Test
	void getTimeString_null_parameter_languageTag() {
		// Arrange
		long millis = 1000 * 2 * 60 * 60 - 1001; // 2 hours - (1 second + 1 millisecond)

		// Act
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> TimeString.getTimeString((String) null, millis));

		// Assert
		assertEquals("The languageTag parameter cannot be null.", exception.getMessage());
	}

	@Test
	void getTimeString_nonexistent_languageTag() {
		// Arrange
		long millis = 1000 * 2 * 60 * 60 - 1001; // 2 hours - (1 second + 1 millisecond)

		// Act & Assert
		assertEquals("1 h, 59 min, 59 s", TimeString.getTimeString("This is not a valid IETF language tag.", millis));
	}

}
