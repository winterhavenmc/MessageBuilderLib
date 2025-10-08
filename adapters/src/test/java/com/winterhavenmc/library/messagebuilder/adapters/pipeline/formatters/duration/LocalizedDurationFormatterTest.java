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

import com.winterhavenmc.library.messagebuilder.core.ports.resources.language.ConstantRepository;
import com.winterhavenmc.library.messagebuilder.models.keys.ConstantKey;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidConstantKey;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class LocalizedDurationFormatterTest
{
	@Mock Time4jDurationFormatter delegateMock;
	@Mock ConstantRepository constantRepositoryMock;

	LocalizedDurationFormatter durationFormatter;
	ValidConstantKey timeLessThanKey = ConstantKey.of("TIME.LESS_THAN").isValid().orElseThrow();
	ValidConstantKey timeUnlimitedKey = ConstantKey.of("TIME.UNLIMITED").isValid().orElseThrow();

	String configString = """
		CONSTANTS:
		  TIME:
		    LESS_THAN: "less than {DURATION}"
		    UNLIMITED: "unlimited"
		""";


	@BeforeEach
	void setUp()
	{
		durationFormatter = new LocalizedDurationFormatter(delegateMock, constantRepositoryMock);
	}


	@Test
	@DisplayName("Should use language file constant for UNLIMITED")
	void testFormatUnlimited()
	{
		// Arrange
		when(constantRepositoryMock.getString(timeUnlimitedKey)).thenReturn(Optional.of("UNLIMITED-TESTING"));

		// Act
		String result = durationFormatter.formatUnlimited();

		// Assert
		assertEquals("UNLIMITED-TESTING", result);

		// Verify
		verify(constantRepositoryMock, atLeastOnce()).getString(timeUnlimitedKey);
	}


	@Test
	@DisplayName("Should use constant and inject {DURATION} for LESS_THAN")
	void testFormatLessThan()
	{
		// Arrange
		when(delegateMock.format(Duration.of(1, ChronoUnit.SECONDS), ChronoUnit.SECONDS)).thenReturn("1 second");
		when(constantRepositoryMock.getString(timeLessThanKey)).thenReturn(Optional.of("less than {DURATION} (TESTING)"));

		// Act
		String result = durationFormatter.formatLessThan(ChronoUnit.SECONDS);

		// Assert
		assertEquals("less than 1 second (TESTING)", result);

		// Verify
		verify(delegateMock, atLeastOnce()).format(Duration.of(1, ChronoUnit.SECONDS), ChronoUnit.SECONDS);
		verify(constantRepositoryMock, atLeastOnce()).getString(timeLessThanKey);
	}


	@Test
	@DisplayName("format normal duration above lower bound threshold.")
	void testFormatNormal()
	{
		// Arrange
		Duration input = Duration.ofMinutes(5);
		when(delegateMock.format(input, ChronoUnit.MINUTES)).thenReturn("5 minutes");

		// Act
		String result = durationFormatter.formatNormal(input, ChronoUnit.MINUTES);

		// Assert
		assertEquals("5 minutes", result);

		// Verify
		verify(delegateMock, atLeastOnce()).format(input, ChronoUnit.MINUTES);
	}


	@Test
	@DisplayName("Should fallback to default if constant is missing or blank")
	void testFallbackWhenConstantMissing()
	{
		// Arrange
		Duration input = Duration.ofMinutes(3);
		when(delegateMock.format(input, ChronoUnit.MINUTES)).thenReturn("3 minutes");

		// Act
		String result = durationFormatter.formatNormal(input, ChronoUnit.MINUTES);

		// Assert
		assertEquals("3 minutes", result);

		// verify
		verify(delegateMock, atLeastOnce()).format(input, ChronoUnit.MINUTES);
	}


	@Test
	void format_normal()
	{
		// Arrange
		when(delegateMock.format(any(Duration.class), eq(ChronoUnit.MINUTES))).thenReturn("10 minutes");

		// Act
		String string = durationFormatter.format(Duration.ofMinutes(10), ChronoUnit.MINUTES);

		// Assert
		assertEquals("10 minutes", string);

		// Verify
		verify(delegateMock, atLeastOnce()).format(any(Duration.class), eq(ChronoUnit.MINUTES));
	}


	@Test
	void format_less_than()
	{
		// Arrange
		when(delegateMock.format(any(Duration.class), eq(ChronoUnit.MINUTES))).thenReturn("1 minute");
		when(constantRepositoryMock.getString(timeLessThanKey)).thenReturn(Optional.of("less than {DURATION}"));

		// Act
		String result = durationFormatter.format(Duration.ofSeconds(10), ChronoUnit.MINUTES);

		// Assert
		assertEquals("less than 1 minute", result);

		// Verify
		verify(constantRepositoryMock, atLeastOnce()).getString(timeLessThanKey);
	}


	@Test
	void format_unlimited()
	{
		// Arrange
		when(constantRepositoryMock.getString(timeUnlimitedKey)).thenReturn(Optional.of("UNLIMITED-TEST"));

		// Act
		String result = durationFormatter.format(Duration.ofSeconds(-1), ChronoUnit.MINUTES);

		// Assert
		assertEquals("UNLIMITED-TEST", result);

		// Verify
		verify(constantRepositoryMock, atLeastOnce()).getString(timeUnlimitedKey);
	}


	@Test
	void testGetTimeConstant_unlimited()
	{
		// Arrange
		when(constantRepositoryMock.getString(timeUnlimitedKey)).thenReturn(Optional.of("∞"));

		// Act
		String result = durationFormatter.getTimeConstant(timeUnlimitedKey, DurationType.UNLIMITED);

		// Assert
		assertEquals("∞", result);

		// Verify
		verify(constantRepositoryMock, atLeastOnce()).getString(timeUnlimitedKey);
	}


	@Test
	void testGetTimeConstant_less_than()
	{
		// Arrange
		when(constantRepositoryMock.getString(timeUnlimitedKey)).thenReturn(Optional.of("< {DURATION}"));

		// Act
		String result = durationFormatter.getTimeConstant(timeUnlimitedKey, DurationType.LESS_THAN);

		// Assert
		assertEquals("< {DURATION}", result);

		// Verify
		verify(constantRepositoryMock, atLeastOnce()).getString(timeUnlimitedKey);
	}

}
