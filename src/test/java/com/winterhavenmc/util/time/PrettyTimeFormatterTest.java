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

import com.winterhavenmc.util.messagebuilder.util.LocaleSupplier;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class PrettyTimeFormatterTest
{
	@Mock LocaleSupplier localeSupplierMock;

	private PrettyTimeFormatter formatter;


	@BeforeEach
	void setUp()
	{
		formatter = new PrettyTimeFormatter(localeSupplierMock);
	}


	@Test
	void testGetFormatted()
	{
		// Arrange
		long millis = 1000 * 2 * 60 * 60 - 1001; // 2 hours - (1 second + 1 millisecond)
		Duration duration = Duration.ofMillis(millis);

		// Act & Assert
		assertEquals("1 hour, 59 minutes, and 59 seconds", formatter.getFormatted(Locale.US, duration));
		assertEquals("1 hour, 59 minutes and 59 seconds", formatter.getFormatted(Locale.UK, duration));
		assertEquals("1 ora, 59 minuti e 59 secondi", formatter.getFormatted(Locale.ITALIAN, duration));
	}


	@Test
	void testGetFormatted_with_locale_supplier_german()
	{
		// Arrange
		long millis = 1000 * 2 * 60 * 60 - 1001; // 2 hours - (1 second + 1 millisecond)
		Duration duration = Duration.ofMillis(millis);
		when(localeSupplierMock.get()).thenReturn(Locale.GERMAN);

		// Act
		String result = formatter.getFormatted(duration);

		// Assert
		assertEquals("1 Stunde, 59 Minuten und 59 Sekunden", result);

		// Verify
		verify(localeSupplierMock, atLeastOnce()).get();
	}


	@Test
	void testGetFormatted_parameter_null_locale()
	{
		// Arrange
		long millis = 1000 * 2 * 60 * 60 - 1001; // 2 hours - (1 second + 1 millisecond)
		Duration duration = Duration.ofMillis(millis);

		// Act
		ValidationException exception = assertThrows(ValidationException.class,
				() -> formatter.getFormatted(null, duration));

		// Assert
		assertEquals("The parameter 'locale' cannot be null.", exception.getMessage());
	}


	@Test
	void testGetFormatted_parameter_null_duration()
	{
		// Arrange & Act
		ValidationException exception = assertThrows(ValidationException.class,
				() -> formatter.getFormatted(Locale.US, null));

		// Assert
		assertEquals("The parameter 'duration' cannot be null.", exception.getMessage());
	}


	@Test
	void testEnglishFormatting()
	{
		String formatted = formatter.getFormatted(Locale.ENGLISH, Duration.ofMinutes(10));
		assertTrue(formatted.contains("10 minutes"), "Expected '10 minutes' in: " + formatted);
	}


	@Test
	void testGermanFormatting()
	{
		String formatted = formatter.getFormatted(Locale.GERMAN, Duration.ofMinutes(1));
		assertTrue(formatted.toLowerCase(Locale.ROOT).contains("minute"), "Expected German word for 'minute'");
	}


	@Test
	void testFrenchFormatting()
	{
		String formatted = formatter.getFormatted(Locale.FRENCH, Duration.ofHours(2));
		assertTrue(formatted.toLowerCase(Locale.ROOT).contains("heures"), "Expected French word for 'hours'");
	}


	@Test
	void testEmptyDuration()
	{
		String formatted = formatter.getFormatted(Locale.ENGLISH, Duration.ZERO);
		assertNotNull(formatted);
		assertFalse(formatted.isBlank(), "Expected non-empty result for zero duration");
	}


	@Test
	void testValidation_nullLocale_throws()
	{
		ValidationException exception = assertThrows(ValidationException.class, () ->
				formatter.getFormatted(null, Duration.ofMinutes(5)));

		assertEquals("The parameter 'locale' cannot be null.", exception.getMessage());
	}


	@Test
	void testValidation_nullDuration_throws()
	{
		ValidationException exception = assertThrows(ValidationException.class, () ->
				formatter.getFormatted(Locale.ENGLISH, null));
		assertEquals("The parameter 'duration' cannot be null.", exception.getMessage());
	}

}
