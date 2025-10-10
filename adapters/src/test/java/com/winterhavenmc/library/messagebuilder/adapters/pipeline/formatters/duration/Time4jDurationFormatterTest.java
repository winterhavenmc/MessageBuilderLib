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

package com.winterhavenmc.library.messagebuilder.adapters.pipeline.formatters.duration;

import com.winterhavenmc.library.messagebuilder.models.configuration.LocaleProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


class Time4jDurationFormatterTest
{
	@Mock
	LocaleProvider localeProviderMock;

	private Time4jDurationFormatter formatter;


	@BeforeEach
	void setUp()
	{
		MockitoAnnotations.openMocks(this);
		when(localeProviderMock.getLocale()).thenReturn(Locale.ENGLISH);
		formatter = new Time4jDurationFormatter(localeProviderMock);
	}


	@Test
	@DisplayName("Should format duration with default precision (MINUTES)")
	void testFormatWithDefaultPrecision()
	{
		Duration input = Duration.ofSeconds(90); // 1.5 minutes
		String result = formatter.format(input, ChronoUnit.MINUTES);
		assertNotNull(result);
		assertFalse(result.isBlank());
	}


	@Test
	@DisplayName("Should format duration above precision normally")
	void testFormatWithCustomPrecision()
	{
		Duration input = Duration.ofMinutes(2);
		String result = formatter.format(input, ChronoUnit.MINUTES);
		assertNotNull(result);
		assertFalse(result.isBlank());
	}


	@Test
	@DisplayName("Should clamp duration below precision to minimum")
	void testClampToPrecision()
	{
		Duration input = Duration.ofMillis(500); // below 1 second
		String result = formatter.format(input, ChronoUnit.SECONDS);
		assertNotNull(result);
		assertFalse(result.isBlank());
		assertTrue(result.contains("second")); // Should show at least "1 second"
	}

//TODO: Fix this test

//	@Test
//	@DisplayName("Should handle zero duration by clamping to precision")
//	void testZeroDuration()
//	{
//		// Arrange
//		Duration input = Duration.ZERO;
//
//		// Act
//		String result = formatter.format(input, ChronoUnit.MINUTES);
//
//		System.out.println(result);
//		// Assert
//		assertNotNull(result);
//		assertFalse(result.isBlank());
//		assertTrue(result.contains("1 minute")); // Should show "1 minute"
//	}


	@Test
	@DisplayName("Should fallback to defaults when null is passed")
	void testNullArgumentsAreHandled()
	{
		// null duration
		String result = formatter.format(null, ChronoUnit.MINUTES);
		assertNotNull(result);

		// null precision
		String result2 = formatter.format(Duration.ofMinutes(2), null);
		assertNotNull(result2);

		// both null
		String result3 = formatter.format(null, null);
		assertNotNull(result3);
	}


	@ParameterizedTest(name = "Duration {0} with precision {1}")
	@MethodSource("durationPrecisionCombinations")
	void testVariousDurations(Duration input, ChronoUnit precision)
	{
		String result = formatter.format(input, precision);
		assertNotNull(result);
		assertFalse(result.isBlank());
	}


	static Stream<org.junit.jupiter.params.provider.Arguments> durationPrecisionCombinations()
	{
		return Stream.of(
				org.junit.jupiter.params.provider.Arguments.of(Duration.ofMillis(999), ChronoUnit.SECONDS),
				org.junit.jupiter.params.provider.Arguments.of(Duration.ofSeconds(1), ChronoUnit.SECONDS),
				org.junit.jupiter.params.provider.Arguments.of(Duration.ofMinutes(1), ChronoUnit.MINUTES),
				org.junit.jupiter.params.provider.Arguments.of(Duration.ofHours(1), ChronoUnit.HOURS)
		);
	}

}
