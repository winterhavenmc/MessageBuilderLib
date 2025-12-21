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

package com.winterhavenmc.library.messagebuilder.core.ports.pipeline.accessors.location;

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

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class LocatableTest
{
	@Mock WorldNameResolver worldNameResolverMock;
	@Mock NumberFormatter localeNumberFormatterMock;
	@Mock ConfigRepository configRepositoryMock;
	@Mock DurationFormatter durationFormatterMock;
	@Mock NumberFormatter numberFormatterMock;
	@Mock ItemNameResolver itemNameResolverMock;
	@Mock ItemDisplayNameResolver itemDisplayNameResolverMock;
	@Mock ItemPluralNameResolver itemPluralNameResolver;
	@Mock World worldMock;

	Location location = new Location(worldMock, 11, 12, 13);


	class TestObject implements Locatable
	{
		@Override
		public Location getLocation()
		{
			return location;
		}
	}


	@Test
	void object_is_instance_of_Locatable()
	{
		// Arrange & Act
		TestObject testObject = new TestObject();

		// Assert
		assertInstanceOf(Locatable.class, testObject);
	}


	@Test
	void getLocation_returns_location()
	{
		// Arrange
		TestObject testObject = new TestObject();

		// Act
		Location result = testObject.getLocation();

		// Assert
		assertEquals(location, result);
	}


	@Test
	void extractLocation_returns_populated_map()
	{
		// Arrange
		UUID worldUid = new UUID(42, 42);
		ValidMacroKey baseKey = MacroKey.of("TEST").isValid().orElseThrow();
		ValidMacroKey locationKey = baseKey.append("LOCATION").isValid().orElseThrow();

		when(numberFormatterMock.format(11)).thenReturn("11");
		when(numberFormatterMock.format(12)).thenReturn("12");
		when(numberFormatterMock.format(13)).thenReturn("13");

		TestObject testObject = new TestObject();
		FormatterCtx formatterCtx = new FormatterCtx(configRepositoryMock, durationFormatterMock, numberFormatterMock, MiniMessage.miniMessage());
		AccessorCtx accessorCtx = new AccessorCtx(worldNameResolverMock, itemNameResolverMock,
				itemDisplayNameResolverMock, itemPluralNameResolver, formatterCtx);

		// Act
		MacroStringMap result = testObject.extractLocation(baseKey, accessorCtx);

		// Assert
		assertTrue(result.containsKey(locationKey));
		assertEquals("- [11, 12, 13]", result.get(locationKey));

		// Verify
		verify(numberFormatterMock, atLeastOnce()).format(11);
		verify(numberFormatterMock, atLeastOnce()).format(12);
		verify(numberFormatterMock, atLeastOnce()).format(13);
	}


	@Test
	void formatLocation_returns_optional_string()
	{
		// Arrange
		UUID worldUid = new UUID(42, 42);
		Location testLocation = new Location(worldMock, 11, 12, 13);
		ValidMacroKey baseKey = MacroKey.of("TEST").isValid().orElseThrow();
		ValidMacroKey locationKey = baseKey.append("LOCATION").isValid().orElseThrow();

		when(worldMock.getName()).thenReturn("test_world");
		when(worldMock.getUID()).thenReturn(worldUid);
		when(worldNameResolverMock.resolve(worldUid)).thenReturn("test_world");
		when(numberFormatterMock.format(11)).thenReturn("11");
		when(numberFormatterMock.format(12)).thenReturn("12");
		when(numberFormatterMock.format(13)).thenReturn("13");

		TestObject testObject = new TestObject();
		FormatterCtx formatterCtx = new FormatterCtx(configRepositoryMock, durationFormatterMock, numberFormatterMock, MiniMessage.miniMessage());
		AccessorCtx accessorCtx = new AccessorCtx(worldNameResolverMock, itemNameResolverMock,
				itemDisplayNameResolverMock, itemPluralNameResolver, formatterCtx);

		// Act
		Optional<String> result = Locatable.formatLocation(testLocation, accessorCtx);

		// Assert
		assertTrue(result.isPresent());
		assertEquals("test_world [11, 12, 13]", result.get());

		// Verify
		verify(worldMock, atLeastOnce()).getName();
		verify(worldMock, atLeastOnce()).getUID();
		verify(worldNameResolverMock, atLeastOnce()).resolve(worldUid);
		verify(numberFormatterMock, atLeastOnce()).format(11);
		verify(numberFormatterMock, atLeastOnce()).format(12);
		verify(numberFormatterMock, atLeastOnce()).format(13);
	}


	@Test
	void formatLocation_with_null_location_returns_empty_optional()
	{
		// Arrange
		TestObject testObject = new TestObject();
		FormatterCtx formatterCtx = new FormatterCtx(configRepositoryMock, durationFormatterMock, numberFormatterMock, MiniMessage.miniMessage());
		AccessorCtx accessorCtx = new AccessorCtx(worldNameResolverMock, itemNameResolverMock,
				itemDisplayNameResolverMock, itemPluralNameResolver, formatterCtx);

		// Act
		Optional<String> result = Locatable.formatLocation(null, accessorCtx);

		// Assert
		assertEquals(Optional.empty(), result);
	}


	// 	static Optional<Section> getLocationWorldName(final Location location, final AdapterContextContainer ctx)
	@Test
	void getLocationWorldName_with_null_world_returns_empty_optional()
	{
		// Arrange
		Location testLocation = new Location(null, 11, 12, 13);

		FormatterCtx formatterCtx = new FormatterCtx(configRepositoryMock, durationFormatterMock, numberFormatterMock, MiniMessage.miniMessage());
		AccessorCtx accessorCtx = new AccessorCtx(worldNameResolverMock, itemNameResolverMock,
				itemDisplayNameResolverMock, itemPluralNameResolver, formatterCtx);

		// Act
		Optional<String> result = Locatable.getLocationWorldName(testLocation, accessorCtx);

		// Assert
		assertEquals(Optional.empty(), result);
	}


	@Test
	void getLocationWorldName_with_valid_world_returns_optional_string()
	{
		// Arrange
		Location testLocation = new Location(worldMock, 11, 12, 13);

		when(worldMock.getName()).thenReturn("test_world");
		when(worldNameResolverMock.resolve(any())).thenReturn("test_world");

		FormatterCtx formatterCtx = new FormatterCtx(configRepositoryMock, durationFormatterMock, numberFormatterMock, MiniMessage.miniMessage());
		AccessorCtx accessorCtx = new AccessorCtx(worldNameResolverMock, itemNameResolverMock,
				itemDisplayNameResolverMock, itemPluralNameResolver, formatterCtx);

		// Act
		Optional<String> result = Locatable.getLocationWorldName(testLocation, accessorCtx);

		// Assert
		assertEquals(Optional.of("test_world"), result);
	}

}
