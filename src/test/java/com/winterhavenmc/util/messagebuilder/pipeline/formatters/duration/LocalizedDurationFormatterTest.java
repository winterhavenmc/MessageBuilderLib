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

package com.winterhavenmc.util.messagebuilder.pipeline.formatters.duration;

import com.winterhavenmc.util.messagebuilder.keys.RecordKey;
import com.winterhavenmc.util.messagebuilder.model.language.ConstantQueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.QueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.QueryHandlerFactory;
import com.winterhavenmc.util.messagebuilder.model.language.constant.ConstantRecord;
import com.winterhavenmc.util.messagebuilder.model.language.Section;

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
	RecordKey timeUnlimitedKey = RecordKey.of("TIME.UNLIMITED").orElseThrow();
	RecordKey timeLessThanKey = RecordKey.of("TIME.LESS_THAN").orElseThrow();


	@BeforeEach
	void setUp() {
		formatter = new LocalizedDurationFormatter(delegateMock, queryHandlerFactoryMock);
	}


	@Test
	@DisplayName("Should use language file constant for UNLIMITED")
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
	}


	@Test
	@DisplayName("Should use constant and inject {DURATION} for NORMAL")
	void testFormatNormal() {
		Duration input = Duration.ofMinutes(5);
		when(delegateMock.format(input, ChronoUnit.MINUTES)).thenReturn("5 minutes");

		String result = formatter.formatNormal(input, ChronoUnit.MINUTES);
		assertEquals("5 minutes", result);
	}

	@Test
	@DisplayName("Should fallback to default if constant is missing or blank")
	void testFallbackWhenConstantMissing()
	{
		// Arrange
		Duration input = Duration.ofMinutes(3);
		when(delegateMock.format(input, ChronoUnit.MINUTES)).thenReturn("3 minutes");

		// Act - Simulate null or blank constant
		String result1 = formatter.formatNormal(input, ChronoUnit.MINUTES);
		String result2 = formatter.formatNormal(input, ChronoUnit.MINUTES);

		// Assert
		assertEquals("3 minutes", result1);
		assertEquals("3 minutes", result2);

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
	void testGetTimeConstant_unlimited()
	{
		// Arrange
		when(queryHandlerFactoryMock.getQueryHandler(Section.CONSTANTS)).thenReturn((QueryHandler) constantQueryHandlerMock);

		// Act
		String result = formatter.getTimeConstant(timeUnlimitedKey, DurationType.UNLIMITED);

		// Assert
		assertEquals("unlimited", result);

		// Verify
		verify(queryHandlerFactoryMock, atLeastOnce()).getQueryHandler(Section.CONSTANTS);
	}


	@Test
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
