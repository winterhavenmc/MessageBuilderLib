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

package com.winterhavenmc.library.messagebuilder.pipeline.adapters.location;

import com.winterhavenmc.library.messagebuilder.keys.MacroKey;
import com.winterhavenmc.library.messagebuilder.pipeline.adapters.AdapterContextContainer;
import com.winterhavenmc.library.messagebuilder.pipeline.containers.MacroStringMap;
import com.winterhavenmc.library.messagebuilder.pipeline.formatters.number.LocaleNumberFormatter;
import com.winterhavenmc.library.messagebuilder.pipeline.resolvers.FormatterContainer;
import com.winterhavenmc.library.messagebuilder.pipeline.resolvers.worldname.WorldNameResolver;
import org.bukkit.Location;
import org.bukkit.World;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class LocatableTest
{
	@Mock AdapterContextContainer ctxMock;
	@Mock FormatterContainer formatterContainerMock;
	@Mock WorldNameResolver worldNameResolverMock;
	@Mock LocaleNumberFormatter localeNumberFormatterMock;
	@Mock Location locationMock;
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
		when(ctxMock.formatterContainer()).thenReturn(formatterContainerMock);
		when(formatterContainerMock.localeNumberFormatter()).thenReturn(localeNumberFormatterMock);
		when(localeNumberFormatterMock.getFormatted(11)).thenReturn("11");
		when(localeNumberFormatterMock.getFormatted(12)).thenReturn("12");
		when(localeNumberFormatterMock.getFormatted(13)).thenReturn("13");
		MacroKey baseKey = MacroKey.of("TEST").orElseThrow();
		MacroKey locationKey = baseKey.append("LOCATION").orElseThrow();
		TestObject testObject = new TestObject();

		// Act
		MacroStringMap result = testObject.extractLocation(baseKey, ctxMock);

		// Assert
		assertEquals("- [11, 12, 13]", result.get(locationKey));
	}


	@Test
	void formatLocation_returns_optional_string()
	{
		// Arrange
		when(ctxMock.formatterContainer()).thenReturn(formatterContainerMock);
		when(ctxMock.worldNameResolver()).thenReturn(worldNameResolverMock);
		when(formatterContainerMock.localeNumberFormatter()).thenReturn(localeNumberFormatterMock);
		when(worldNameResolverMock.resolveWorldName(any())).thenReturn("test_world");
		when(locationMock.getBlockX()).thenReturn(11);
		when(locationMock.getBlockY()).thenReturn(12);
		when(locationMock.getBlockZ()).thenReturn(13);
		when(locationMock.getWorld()).thenReturn(worldMock);
		when(worldMock.getName()).thenReturn("test_world");
		when(localeNumberFormatterMock.getFormatted(11)).thenReturn("11");
		when(localeNumberFormatterMock.getFormatted(12)).thenReturn("12");
		when(localeNumberFormatterMock.getFormatted(13)).thenReturn("13");
		MacroKey baseKey = MacroKey.of("TEST").orElseThrow();
		MacroKey locationKey = baseKey.append("LOCATION").orElseThrow();
		TestObject testObject = new TestObject();

		// Act
		Optional<String> result = Locatable.formatLocation(locationMock, ctxMock);

		// Assert
		assertEquals(Optional.of("test_world [11, 12, 13]"), result);
	}


	@Test
	void formatLocation_with_null_location_returns_empty_optional()
	{
		// Arrange
		TestObject testObject = new TestObject();
		when(ctxMock.formatterContainer()).thenReturn(formatterContainerMock);
		when(formatterContainerMock.localeNumberFormatter()).thenReturn(localeNumberFormatterMock);

		// Act
		Optional<String> result = Locatable.formatLocation(null, ctxMock);

		// Assert
		assertEquals(Optional.empty(), result);
	}


	// 	static Optional<String> getLocationWorldName(final Location location, final AdapterContextContainer ctx)
	@Test
	void getLocationWorldName_with_null_world_returns_empty_optional()
	{
		// Act
		Optional<String> result = Locatable.getLocationWorldName(location, ctxMock);

		// Assert
		assertEquals(Optional.empty(), result);
	}


	@Test
	void getLocationWorldName_with_valid_world_returns_optional_string()
	{
		// Arrange
		when(locationMock.getWorld()).thenReturn(worldMock);
		when(worldMock.getName()).thenReturn("test_world");
		when(ctxMock.worldNameResolver()).thenReturn(worldNameResolverMock);
		when(worldNameResolverMock.resolveWorldName(any())).thenReturn("test_world");

		// Act
		Optional<String> result = Locatable.getLocationWorldName(locationMock, ctxMock);

		// Assert
		assertEquals(Optional.of("test_world"), result);
	}

}
