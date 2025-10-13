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

package com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.instant;

import com.winterhavenmc.library.messagebuilder.core.context.AdapterCtx;
import com.winterhavenmc.library.messagebuilder.core.context.FormatterCtx;
import com.winterhavenmc.library.messagebuilder.core.maps.MacroStringMap;

import com.winterhavenmc.library.messagebuilder.models.configuration.LocaleProvider;
import com.winterhavenmc.library.messagebuilder.models.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidMacroKey;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.FormatStyle;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class InstantableTest
{
	@Mock AdapterCtx ctxMock;
	@Mock FormatterCtx formatterContainerMock;
	@Mock LocaleProvider localeProviderMock;

	static class TestObject implements Instantable
	{
		@Override
		public Instant getInstant()
		{
			return Instant.EPOCH;
		}
	}


	@Test
	void object_is_instance_of_Instantable()
	{
		// Arrange & Act
		TestObject testObject = new TestObject();

		// Assert
		assertInstanceOf(Instantable.class, testObject);
	}


	@Test
	void getInstant_returns_Instant()
	{
		// Arrange
		TestObject testObject = new TestObject();

		// Act
		Instant result = testObject.getInstant();

		// Assert
		assertEquals(Instant.EPOCH, result);
	}


	@Test
	void extractInstant_returns_populated_map()
	{
		// Arrange
		ValidMacroKey baseKey = MacroKey.of("TEST").isValid().orElseThrow();
		ValidMacroKey subKey = baseKey.append("INSTANT").isValid().orElseThrow();
		TestObject testObject = new TestObject();
		when(ctxMock.formatterCtx()).thenReturn(formatterContainerMock);
		when(formatterContainerMock.localeProvider()).thenReturn(localeProviderMock);
		when(localeProviderMock.getZoneId()).thenReturn(ZoneId.of("UTC"));

		// Act
		MacroStringMap result = testObject.extractInstant(baseKey, FormatStyle.MEDIUM, ctxMock);

		// Assert
		assertEquals("Jan 1, 1970, 12:00:00 AM", result.get(subKey));
	}


	@Test
	void formatInstant_returns_optional_string()
	{
		// Arrange
		ValidMacroKey macroKey = MacroKey.of("TEST").isValid().orElseThrow();
		TestObject testObject = new TestObject();
		when(localeProviderMock.getZoneId()).thenReturn(ZoneId.of("UTC"));

		// Act
		Optional<String> result = Instantable.formatInstant(testObject.getInstant(), FormatStyle.MEDIUM, localeProviderMock);

		// Assert
		assertEquals(Optional.of("Jan 1, 1970, 12:00:00 AM"), result);
	}


	@Test
	void formatInstant_with_null_name_returns_empty_optional()
	{
		// Arrange
		ValidMacroKey macroKey = MacroKey.of("TEST").isValid().orElseThrow();
		TestObject testObject = new TestObject();

		// Act
		Optional<String> result = Instantable.formatInstant(null, FormatStyle.MEDIUM, localeProviderMock);

		// Assert
		assertEquals(Optional.empty(), result);
	}

}
