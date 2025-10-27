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

package com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.duration;

import com.winterhavenmc.library.messagebuilder.core.context.AccessorCtx;
import com.winterhavenmc.library.messagebuilder.core.context.FormatterCtx;
import com.winterhavenmc.library.messagebuilder.core.maps.MacroStringMap;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.formatters.duration.DurationFormatter;

import com.winterhavenmc.library.messagebuilder.models.configuration.ConfigRepository;
import com.winterhavenmc.library.messagebuilder.models.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidMacroKey;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class DurationableTest
{
	@Mock AccessorCtx ctxMock;
	@Mock FormatterCtx formatterContainerMock;
	@Mock ConfigRepository configRepository;
	@Mock DurationFormatter durationFormatterMock;


	static class TestObject implements Durationable
	{
		@Override
		public Duration getDuration()
		{
			return Duration.ofMinutes(67);
		}
	}


	@Test
	void object_is_instance_of_Durationable()
	{
		// Arrange & Act
		TestObject testObject = new TestObject();

		// Assert
		assertInstanceOf(Durationable.class, testObject);
	}


	@Test
	void getDuration_returns_Duration()
	{
		// Arrange
		TestObject testObject = new TestObject();

		// Act
		Duration result = testObject.getDuration();

		// Assert
		assertEquals(Duration.ofMinutes(67), result);
	}


	@Test
	void extractDuration_returns_populated_map()
	{
		// Arrange
		ValidMacroKey baseKey = MacroKey.of("TEST").isValid().orElseThrow();
		ValidMacroKey subKey = baseKey.append("DURATION").isValid().orElseThrow();
		TestObject testObject = new TestObject();
		when(ctxMock.formatterCtx()).thenReturn(formatterContainerMock);
		when(formatterContainerMock.durationFormatter()).thenReturn(durationFormatterMock);
		when(durationFormatterMock.format(any(), eq(ChronoUnit.MINUTES))).thenReturn("valid duration string");

		// Act
		MacroStringMap result = testObject.extractDuration(baseKey, ChronoUnit.MINUTES, ctxMock);

		// Assert
		assertEquals("valid duration string", result.get(subKey));
	}


	@Test
	void formatDuration_returns_optional_string()
	{
		// Arrange
		ValidMacroKey macroKey = MacroKey.of("TEST").isValid().orElseThrow();
		TestObject testObject = new TestObject();
		when(durationFormatterMock.format(any(), eq(ChronoUnit.MINUTES))).thenReturn("valid duration string");

		// Act
		Optional<String> result = Durationable.formatDuration(testObject.getDuration(), ChronoUnit.MINUTES, durationFormatterMock);

		// Assert
		assertEquals(Optional.of("valid duration string"), result);
	}


	@Test
	void formatDuration_with_null_returns_empty_optional()
	{
		// Arrange
		ValidMacroKey macroKey = MacroKey.of("TEST").isValid().orElseThrow();
		TestObject testObject = new TestObject();

		// Act
		Optional<String> result = Durationable.formatDuration(null, ChronoUnit.MINUTES, durationFormatterMock);

		// Assert
		assertEquals(Optional.empty(), result);
	}


	@Test
	void durationUntil_returns_duration_until_future_instant()
	{
		// Arrange
		Instant futureInstant = Instant.now().plus(10, ChronoUnit.MINUTES);

		// Act
		Duration result = Durationable.durationUntil(futureInstant);

		// Assert
		// add millis and truncate to seconds to account for processing time
		assertEquals(Duration.ofMinutes(10), result.plusMillis(999).truncatedTo(ChronoUnit.SECONDS));
	}


	@Test
	void durationUntil_with_null_parameter_returns_zero_duration()
	{
		// Arrange & Act
		Duration result = Durationable.durationUntil(null);

		// Assert
		assertEquals(Duration.ZERO, result);
	}

}
