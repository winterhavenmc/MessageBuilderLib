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

import com.winterhavenmc.util.messagebuilder.util.LocalizedException;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class PrettyTimeFormatterTest {

	@Test
	void testGetFormatted() {
		// Arrange
		long millis = 1000 * 2 * 60 * 60 - 1001; // 2 hours - (1 second + 1 millisecond)
		Duration duration = Duration.ofMillis(millis);

		// Act & Assert
		assertEquals("1 hour, 59 minutes, and 59 seconds", new PrettyTimeFormatter().getFormatted(Locale.US, duration));
		assertEquals("1 hour, 59 minutes and 59 seconds", new PrettyTimeFormatter().getFormatted(Locale.UK, duration));
		assertEquals("1 ora, 59 minuti e 59 secondi", new PrettyTimeFormatter().getFormatted(Locale.ITALIAN, duration));
	}

	@Test
	void testGetFormatted_parameter_null_locale() {
		// Arrange
		long millis = 1000 * 2 * 60 * 60 - 1001; // 2 hours - (1 second + 1 millisecond)
		Duration duration = Duration.ofMillis(millis);

		// Act
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> new PrettyTimeFormatter().getFormatted(null, duration));

		// Assert
		assertEquals("The parameter 'locale' cannot be null.", exception.getMessage());
	}

	@Test
	void testGetFormatted_parameter_null_duration() {
		// Arrange & Act
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> new PrettyTimeFormatter().getFormatted(Locale.US, null));

		// Assert
		assertEquals("The parameter 'duration' cannot be null.", exception.getMessage());
	}

}
