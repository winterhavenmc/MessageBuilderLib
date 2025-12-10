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

package com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.protection;

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

import org.bukkit.plugin.Plugin;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

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
class ProtectableTest
{
	@Mock Plugin pluginMock;
	@Mock DurationFormatter durationFormatterMock;
	@Mock ConfigRepository configRepositoryMock;
	@Mock NumberFormatter numberFormatterMock;
	@Mock WorldNameResolver worldNameResolverMock;
	@Mock ItemNameResolver itemNameResolverMock;
	@Mock ItemDisplayNameResolver itemDisplayNameResolver;
	@Mock ItemPluralNameResolver itemPluralNameResolver;


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
		ValidMacroKey baseKey = MacroKey.of("TEST").isValid().orElseThrow();
		ValidMacroKey expirationKey = baseKey.append("PROTECTION").isValid().orElseThrow();
		ValidMacroKey instantKey = expirationKey.append("INSTANT").isValid().orElseThrow();
		ValidMacroKey durationKey = expirationKey.append("DURATION").isValid().orElseThrow();
		TestObject testObject = new TestObject();

		FormatterCtx formatterCtx = new FormatterCtx(configRepositoryMock, durationFormatterMock, numberFormatterMock);
		AccessorCtx accessorCtx = new AccessorCtx(worldNameResolverMock, itemNameResolverMock, itemDisplayNameResolver,
				itemPluralNameResolver, formatterCtx);

		when(configRepositoryMock.zoneId()).thenReturn(ZoneId.of("UTC"));
		when(configRepositoryMock.dateLocale()).thenReturn(Locale.US);
		when(durationFormatterMock.format(any(), eq(ChronoUnit.MINUTES))).thenReturn("valid duration string");

		// Act
		MacroStringMap result = testObject.extractProtection(baseKey, ChronoUnit.MINUTES, FormatStyle.MEDIUM, accessorCtx);

		// Assert
		assertEquals("Jan 1, 1970, 12:00:00 AM", result.get(instantKey));
		assertEquals("valid duration string", result.get(durationKey));
	}

}
