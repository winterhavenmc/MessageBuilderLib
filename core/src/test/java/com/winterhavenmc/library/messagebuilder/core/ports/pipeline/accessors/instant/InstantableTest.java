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

import com.winterhavenmc.library.messagebuilder.core.context.AccessorCtx;
import com.winterhavenmc.library.messagebuilder.core.context.FormatterCtx;
import com.winterhavenmc.library.messagebuilder.core.maps.MacroStringMap;

import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.formatters.duration.DurationFormatter;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.formatters.number.NumberFormatter;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.itemname.ItemDisplayNameResolver;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.itemname.ItemNameResolver;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.itemname.ItemPluralNameResolver;
import com.winterhavenmc.library.messagebuilder.core.ports.pipeline.resolvers.worldname.WorldNameResolver;
import com.winterhavenmc.library.messagebuilder.models.configuration.ConfigRepository;
import com.winterhavenmc.library.messagebuilder.models.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidMacroKey;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.Optional;

import net.kyori.adventure.text.minimessage.MiniMessage;
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
	@Mock ConfigRepository configRepositoryMock;
	@Mock WorldNameResolver worldNameResolverMock;
	@Mock ItemNameResolver itemNameResolverMock;
	@Mock ItemDisplayNameResolver itemDisplayNameResolver;
	@Mock ItemPluralNameResolver itemPluralNameResolver;
	@Mock DurationFormatter durationFormatterMock;
	@Mock NumberFormatter numberFormatterMock;


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
		FormatterCtx formatterCtx = new FormatterCtx(configRepositoryMock, durationFormatterMock, numberFormatterMock, MiniMessage.miniMessage());
		AccessorCtx accessorCtx = new AccessorCtx(worldNameResolverMock, itemNameResolverMock, itemDisplayNameResolver,
				itemPluralNameResolver, formatterCtx);
		when(configRepositoryMock.zoneId()).thenReturn(ZoneId.of("UTC"));
		when(configRepositoryMock.dateLocale()).thenReturn(Locale.US);

		// Act
		MacroStringMap result = testObject.extractInstant(baseKey, FormatStyle.MEDIUM, accessorCtx);

		// Assert
		assertEquals("Jan 1, 1970, 12:00:00 AM", result.get(subKey));
	}


	@Test
	void formatInstant_returns_optional_string()
	{
		// Arrange
		ValidMacroKey macroKey = MacroKey.of("TEST").isValid().orElseThrow();
		TestObject testObject = new TestObject();
		when(configRepositoryMock.zoneId()).thenReturn(ZoneId.of("UTC"));
		when(configRepositoryMock.dateLocale()).thenReturn(Locale.US);

		// Act
		Optional<String> result = Instantable.formatInstant(testObject.getInstant(), FormatStyle.MEDIUM, configRepositoryMock);

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
		Optional<String> result = Instantable.formatInstant(null, FormatStyle.MEDIUM, configRepositoryMock);

		// Assert
		assertEquals(Optional.empty(), result);
	}

}
