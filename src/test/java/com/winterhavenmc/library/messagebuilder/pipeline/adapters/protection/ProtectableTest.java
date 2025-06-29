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

package com.winterhavenmc.library.messagebuilder.pipeline.adapters.protection;

import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.AdapterContextContainer;
import com.winterhavenmc.library.messagebuilder.pipeline.maps.MacroStringMap;
import com.winterhavenmc.library.messagebuilder.pipeline.formatters.duration.DurationFormatter;
import com.winterhavenmc.library.messagebuilder.pipeline.formatters.FormatterContainer;
import com.winterhavenmc.library.messagebuilder.resources.configuration.LocaleProvider;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ProtectableTest
{
	@Mock Plugin pluginMock;
	@Mock AdapterContextContainer ctxMock;
	@Mock FormatterContainer formatterContainerMock;
	@Mock DurationFormatter durationFormatterMock;
	@Mock LocaleProvider localeProviderMock;


	static class TestObject implements Protectable
	{
		@Override
		public Instant getProtection()
		{
			return Instant.EPOCH;
		}
	}


	@Test
	void object_is_instance_of_Expirable()
	{
		// Arrange & Act
		TestObject testObject = new TestObject();

		// Assert
		assertInstanceOf(Protectable.class, testObject);
	}


	@Test
	void getExpiration_returns_Instant()
	{
		// Arrange
		TestObject testObject = new TestObject();

		// Act
		Instant result = testObject.getProtection();

		// Assert
		assertEquals(Instant.EPOCH, result);
	}


	@Test
	void extractExpiration_returns_populated_map()
	{
		// Arrange
		MacroKey baseKey = MacroKey.of("TEST").orElseThrow();
		MacroKey expirationKey = baseKey.append("PROTECTION").orElseThrow();
		MacroKey instantKey = expirationKey.append("INSTANT").orElseThrow();
		MacroKey durationKey = expirationKey.append("DURATION").orElseThrow();
		TestObject testObject = new TestObject();

		when(ctxMock.formatterContainer()).thenReturn(formatterContainerMock);
		when(formatterContainerMock.durationFormatter()).thenReturn(durationFormatterMock);
		when(formatterContainerMock.localeProvider()).thenReturn(localeProviderMock);
		when(localeProviderMock.getZoneId()).thenReturn(ZoneId.of("UTC"));
		when(durationFormatterMock.format(any(), eq(ChronoUnit.MINUTES))).thenReturn("valid duration string");

		// Act
		MacroStringMap result = testObject.extractProtection(baseKey, ChronoUnit.MINUTES, FormatStyle.MEDIUM, ctxMock);

		// Assert
		assertEquals("Jan 1, 1970, 12:00:00 AM", result.get(instantKey));
		assertEquals("valid duration string", result.get(durationKey));
	}

}
