/*
 * Copyright (c) 2024 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.macro.processor;

import com.winterhavenmc.util.messagebuilder.macro.ContextMap;
import com.winterhavenmc.util.messagebuilder.query.QueryHandler;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.stubbing.OngoingStubbing;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


class LocationProcessorTest {

	private LocationProcessor locationProcessor;
	private ContextMap contextMap;
	private QueryHandler queryHandler;
	private World mockWorld;
	private Location mockLocation;

	@BeforeEach
	void setUp() {
		// Mock dependencies
		queryHandler = mock(QueryHandler.class);
		contextMap = mock(ContextMap.class);
		mockWorld = mock(World.class);
		mockLocation = mock(Location.class);

		// Initialize the processor
		locationProcessor = new LocationProcessor(queryHandler);

		// Mock Bukkit's PluginManager and WorldNameUtility
		try (MockedStatic<Bukkit> mockedBukkit = mockStatic(Bukkit.class)) {
			PluginManager mockPluginManager = mock(PluginManager.class);
			mockedBukkit.when(Bukkit::getPluginManager).thenReturn(mockPluginManager);
			when(mockWorld.getName()).thenReturn("world");
		}
	}

	@Test
	void testResolveContext_ValidLocation() {
		// Arrange
		when(mockLocation.getWorld()).thenReturn(mockWorld);
		when(mockLocation.getBlockX()).thenReturn(123);
		when(mockLocation.getBlockY()).thenReturn(64);
		when(mockLocation.getBlockZ()).thenReturn(-789);

		// Act
		ResultMap result = locationProcessor.resolveContext("HOME", contextMap, mockLocation);

		// Assert
		assertThat(result, is(notNullValue()));
		assertThat(result.get("HOME_LOCATION_WORLD"), is("world"));
		assertThat(result.get("HOME_LOCATION_X"), is("123"));
		assertThat(result.get("HOME_LOCATION_Y"), is("64"));
		assertThat(result.get("HOME_LOCATION_Z"), is("-789"));
		assertThat(result.get("HOME_LOCATION"), is("world [123, 64, -789]"));
	}

	@Test
	void testResolveContext_NullLocation() {
		// Act & Assert
		assertThrows(IllegalArgumentException.class,
				() -> locationProcessor.resolveContext("HOME", contextMap, null),
				"Expected resolveContext to throw IllegalArgumentException for null location"
		);
	}

	@Test
	void testResolveContext_MissingWorld() {
		// Arrange
		when(mockLocation.getWorld()).thenReturn(null);
		when(mockLocation.getBlockX()).thenReturn(123);
		when(mockLocation.getBlockY()).thenReturn(64);
		when(mockLocation.getBlockZ()).thenReturn(-789);

		// Act
		ResultMap result = locationProcessor.resolveContext("HOME", contextMap, mockLocation);

		// Assert
		assertThat(result.get("HOME_LOCATION_WORLD"), is("UNKNOWN_VALUE"));
		assertThat(result.get("HOME_LOCATION"), is("UNKNOWN_VALUE [123, 64, -789]"));
	}

	@Test
	void testResolveContext_EmptyKey() {
		// Act & Assert
		assertThrows(IllegalArgumentException.class,
				() -> locationProcessor.resolveContext("", contextMap, mockLocation),
				"Expected resolveContext to throw IllegalArgumentException for empty key"
		);
	}

	@Test
	void testResolveContext_KeyWithoutLocationSuffix() {
		// Arrange
		when(mockLocation.getWorld()).thenReturn(mockWorld);
		when(mockLocation.getBlockX()).thenReturn(123);
		when(mockLocation.getBlockY()).thenReturn(64);
		when(mockLocation.getBlockZ()).thenReturn(-789);

		// Act
		ResultMap result = locationProcessor.resolveContext("HOME", contextMap, mockLocation);

		// Assert
		assertThat(result.get("HOME_LOCATION_WORLD"), is("world"));
		assertThat(result.get("HOME_LOCATION_X"), is("123"));
		assertThat(result.get("HOME_LOCATION_Y"), is("64"));
		assertThat(result.get("HOME_LOCATION_Z"), is("-789"));
		assertThat(result.get("HOME_LOCATION"), is("world [123, 64, -789]"));
	}

	@Test
	void testResolveContext_InvalidValueType() {
		// Arrange
		Object invalidValue = "NotALocation";

		// Act & Assert
		assertThrows(IllegalArgumentException.class,
				() -> locationProcessor.resolveContext("HOME", contextMap, invalidValue),
				"Expected resolveContext to throw IllegalArgumentException for invalid value type"
		);
	}
}
