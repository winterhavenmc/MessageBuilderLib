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

package com.winterhavenmc.library.messagebuilder.pipeline.adapters.duration;

import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.AdapterContextContainer;
import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroStringMap;
import com.winterhavenmc.library.messagebuilder.pipeline.formatters.duration.DurationFormatter;
import com.winterhavenmc.library.messagebuilder.pipeline.resolvers.FormatterContainer;
import com.winterhavenmc.library.messagebuilder.resources.configuration.LocaleProvider;

import java.time.Duration;
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
	@Mock AdapterContextContainer ctxMock;
	@Mock FormatterContainer formatterContainerMock;
	@Mock LocaleProvider localeProviderMock;
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
		MacroKey baseKey = MacroKey.of("TEST").orElseThrow();
		MacroKey subKey = baseKey.append("DURATION").orElseThrow();
		TestObject testObject = new TestObject();
		when(ctxMock.formatterContainer()).thenReturn(formatterContainerMock);
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
		MacroKey macroKey = MacroKey.of("TEST").orElseThrow();
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
		MacroKey macroKey = MacroKey.of("TEST").orElseThrow();
		TestObject testObject = new TestObject();

		// Act
		Optional<String> result = Durationable.formatDuration(null, ChronoUnit.MINUTES, durationFormatterMock);

		// Assert
		assertEquals(Optional.empty(), result);
	}

}
