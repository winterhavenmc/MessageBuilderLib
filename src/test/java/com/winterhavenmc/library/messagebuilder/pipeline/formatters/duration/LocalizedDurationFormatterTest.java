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

package com.winterhavenmc.library.messagebuilder.pipeline.formatters.duration;

import com.winterhavenmc.library.messagebuilder.keys.ConstantKey;
import com.winterhavenmc.library.messagebuilder.keys.ValidConstantKey;
import com.winterhavenmc.library.messagebuilder.query.ConstantQueryHandler;
import com.winterhavenmc.library.messagebuilder.query.QueryHandler;
import com.winterhavenmc.library.messagebuilder.query.QueryHandlerFactory;
import com.winterhavenmc.library.messagebuilder.model.language.ConstantRecord;
import com.winterhavenmc.library.messagebuilder.model.language.Section;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

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
	@Mock ConstantQueryHandler constantQueryHandlerMock;
	@Mock QueryHandlerFactory queryHandlerFactoryMock;

	LocalizedDurationFormatter formatter;
	ValidConstantKey timeUnlimitedKey = ConstantKey.of("TIME.UNLIMITED").isValid().orElseThrow();
	ValidConstantKey timeLessThanKey = ConstantKey.of("TIME.LESS_THAN").isValid().orElseThrow();


	@BeforeEach
	void setUp()
	{
		formatter = new LocalizedDurationFormatter(delegateMock, queryHandlerFactoryMock);
	}


	@Test
	@DisplayName("Should use language file constant for UNLIMITED")
	@SuppressWarnings({"unchecked", "rawtypes"})
	void testFormatUnlimited()
	{
		// Arrange
		ConstantRecord constantRecord = ConstantRecord.from(timeUnlimitedKey, "UNLIMITED-TESTING");
		when(queryHandlerFactoryMock.getQueryHandler(Section.CONSTANTS)).thenReturn((QueryHandler) constantQueryHandlerMock);
		when(constantQueryHandlerMock.getRecord(timeUnlimitedKey)).thenReturn(constantRecord);

		// Act
		String result = formatter.formatUnlimited();

		// Assert
		assertEquals("UNLIMITED-TESTING", result);

		// Verify
		verify(queryHandlerFactoryMock, atLeastOnce()).getQueryHandler(Section.CONSTANTS);
	}


	@Test
	@DisplayName("Should use constant and inject {DURATION} for LESS_THAN")
	@SuppressWarnings({"unchecked", "rawtypes"})
	void testFormatLessThan()
	{
		// Arrange
		ConstantRecord constantRecord = ConstantRecord.from(timeLessThanKey, "less than {DURATION} (TESTING)");
		when(delegateMock.format(Duration.of(1, ChronoUnit.SECONDS), ChronoUnit.SECONDS)).thenReturn("1 second");
		when(queryHandlerFactoryMock.getQueryHandler(Section.CONSTANTS)).thenReturn((QueryHandler) constantQueryHandlerMock);
		when(constantQueryHandlerMock.getRecord(timeLessThanKey)).thenReturn(constantRecord);

		// Act
		String result = formatter.formatLessThan(ChronoUnit.SECONDS);

		// Assert
		assertEquals("less than 1 second (TESTING)", result);

		// Verify
		verify(delegateMock, atLeastOnce()).format(Duration.of(1, ChronoUnit.SECONDS), ChronoUnit.SECONDS);
		verify(queryHandlerFactoryMock, atLeastOnce()).getQueryHandler(Section.CONSTANTS);
		verify(constantQueryHandlerMock, atLeastOnce()).getRecord(timeLessThanKey);
	}


	@Test
	@DisplayName("format normal duration above lower bound threshold.")
	void testFormatNormal()
	{
		// Arrange
		Duration input = Duration.ofMinutes(5);
		when(delegateMock.format(input, ChronoUnit.MINUTES)).thenReturn("5 minutes");

		// Act
		String result = formatter.formatNormal(input, ChronoUnit.MINUTES);

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
		String result = formatter.formatNormal(input, ChronoUnit.MINUTES);

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
		String string = formatter.format(Duration.ofMinutes(10), ChronoUnit.MINUTES);

		// Assert
		assertEquals("10 minutes", string);

		// Verify
		verify(delegateMock, atLeastOnce()).format(any(Duration.class), eq(ChronoUnit.MINUTES));
	}


	@Test
	@SuppressWarnings({"unchecked", "rawtypes"})
	void format_less_than()
	{
		// Arrange
		ConstantRecord record = ConstantRecord.from(timeLessThanKey, "less than {DURATION}");
		when(delegateMock.format(any(Duration.class), eq(ChronoUnit.MINUTES))).thenReturn("1 minute");
		when(queryHandlerFactoryMock.getQueryHandler(Section.CONSTANTS)).thenReturn((QueryHandler) constantQueryHandlerMock);
		when(constantQueryHandlerMock.getRecord(timeLessThanKey)).thenReturn(record);

		// Act
		String result = formatter.format(Duration.ofSeconds(10), ChronoUnit.MINUTES);

		// Assert
		assertEquals("less than 1 minute", result);

		// Verify
//		verify(delegateMock.format(any(Duration.class), eq(ChronoUnit.MINUTES)));
		verify(queryHandlerFactoryMock, atLeastOnce()).getQueryHandler(Section.CONSTANTS);
		verify(constantQueryHandlerMock, atLeastOnce()).getRecord(timeLessThanKey);
	}


	@Test
	@SuppressWarnings({"unchecked", "rawtypes"})
	void format_unlimited()
	{
		// Arrange
		ConstantRecord record = ConstantRecord.from(timeUnlimitedKey, "UNLIMITED-TEST");
		when(queryHandlerFactoryMock.getQueryHandler(Section.CONSTANTS)).thenReturn((QueryHandler) constantQueryHandlerMock);
		when(constantQueryHandlerMock.getRecord(timeUnlimitedKey)).thenReturn(record);

		// Act
		String result = formatter.format(Duration.ofSeconds(-1), ChronoUnit.MINUTES);

		// Assert
		assertEquals("UNLIMITED-TEST", result);

		// Verify
		verify(queryHandlerFactoryMock, atLeastOnce()).getQueryHandler(Section.CONSTANTS);
		verify(constantQueryHandlerMock, atLeastOnce()).getRecord(timeUnlimitedKey);
	}


	@Test
	@SuppressWarnings({"unchecked", "rawtypes"})
	void testGetTimeConstant_unlimited()
	{
		// Arrange
		when(queryHandlerFactoryMock.getQueryHandler(Section.CONSTANTS)).thenReturn((QueryHandler) constantQueryHandlerMock);

		// Act
		String result = formatter.getTimeConstant(timeUnlimitedKey, DurationType.UNLIMITED);

		// Assert
		assertEquals("∞", result);

		// Verify
		verify(queryHandlerFactoryMock, atLeastOnce()).getQueryHandler(Section.CONSTANTS);
	}


	@Test
	@SuppressWarnings({"unchecked", "rawtypes"})
	void testGetTimeConstant_less_than()
	{
		// Arrange
		when(queryHandlerFactoryMock.getQueryHandler(Section.CONSTANTS)).thenReturn((QueryHandler) constantQueryHandlerMock);

		// Act
		String result = formatter.getTimeConstant(timeUnlimitedKey, DurationType.LESS_THAN);

		// Assert
		assertEquals("< {DURATION}", result);

		// Verify
		verify(queryHandlerFactoryMock, atLeastOnce()).getQueryHandler(Section.CONSTANTS);
	}

}
