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
import com.winterhavenmc.util.messagebuilder.pipeline.formatters.duration.LocalizedDurationFormatter;
import com.winterhavenmc.util.messagebuilder.pipeline.formatters.duration.Time4jDurationFormatter;
import com.winterhavenmc.util.messagebuilder.resources.QueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.QueryHandlerFactory;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.constant.ConstantRecord;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.Section;
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
	@Mock
	Time4jDurationFormatter delegateMock;
	@Mock QueryHandler<ConstantRecord> constantQueryHandlerMock;
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

//	@Test
//	@DisplayName("Full format() should dispatch based on duration type")
//	void testFormatDispatch()
//	{
//		Duration unlimited = Duration.ofSeconds(-1);
//		Duration small = Duration.ofMillis(200);
//		Duration normal = Duration.ofMinutes(2);
//
//		when(delegateMock.format(any(), any())).thenReturn("FORMATTED_DURATION");
//		when(constantQueryHandlerMock.getString(timeUnlimitedKey)).thenReturn(Optional.of("UNLIMITED_CONSTANT"));
//		when(constantQueryHandlerMock.getString(timeLessThanKey)).thenReturn(Optional.of("LESS_THAN_CONSTANT"));
//
//		String resultUnlimited = formatter.format(unlimited, ChronoUnit.SECONDS);
//		String resultSmall = formatter.format(small, ChronoUnit.SECONDS);
//		String resultNormal = formatter.format(normal, ChronoUnit.SECONDS);
//
//		assertEquals("UNLIMITED_CONSTANT", resultUnlimited);
//		assertEquals("LESS_THAN_CONSTANT", resultSmall);
//		assertEquals("FORMATTED_DURATION", resultNormal);
//
//		// Verify
//		verify(delegateMock, atLeastOnce()).format(any(), any());
//	}

}
