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

import com.winterhavenmc.util.messagebuilder.context.ContextMap;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


@ExtendWith(MockitoExtension.class)
class LocationProcessorTest {

	@Mock private World worldMock;
	@Mock private Location locationMock;
	@Mock private Player playerMock;

	// real location processor
	private LocationProcessor locationProcessor;
	private ContextMap contextMap;


	@BeforeEach
	void setUp() {
		// real context map
		contextMap = new ContextMap(playerMock);

		// Initialize the processor
		locationProcessor = new LocationProcessor();
	}

	@Test
	void testResolveContext_ValidLocation() {
		// Arrange
		when(worldMock.getName()).thenReturn("test_world");
		when(locationMock.getWorld()).thenReturn(worldMock);
		when(locationMock.getBlockX()).thenReturn(123);
		when(locationMock.getBlockY()).thenReturn(64);
		when(locationMock.getBlockZ()).thenReturn(-789);

		// Act
		ResultMap result = locationProcessor.resolveContext("HOME", contextMap, locationMock);

		// Assert
		assertThat(result, is(notNullValue()));
		assertThat(result.get("HOME_LOCATION_WORLD"), is("test_world"));
		assertThat(result.get("HOME_LOCATION_X"), is("123"));
		assertThat(result.get("HOME_LOCATION_Y"), is("64"));
		assertThat(result.get("HOME_LOCATION_Z"), is("-789"));
		assertThat(result.get("HOME_LOCATION"), is("test_world [123, 64, -789]"));
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
		when(locationMock.getWorld()).thenReturn(null);
		when(locationMock.getBlockX()).thenReturn(123);
		when(locationMock.getBlockY()).thenReturn(64);
		when(locationMock.getBlockZ()).thenReturn(-789);

		// Act
		ResultMap result = locationProcessor.resolveContext("HOME", contextMap, locationMock);

		// Assert
		assertThat(result.get("HOME_LOCATION_WORLD"), is("???"));
		assertThat(result.get("HOME_LOCATION"), is("??? [123, 64, -789]"));
	}

	@Test
	void testResolveContext_EmptyKey() {
		// Act & Assert
		assertThrows(IllegalArgumentException.class,
				() -> locationProcessor.resolveContext("", contextMap, locationMock),
				"Expected resolveContext to throw IllegalArgumentException for empty key"
		);
	}

	@Test
	void testResolveContext_KeyWithoutLocationSuffix() {
		// Arrange
		when(worldMock.getName()).thenReturn("test_world");
		when(locationMock.getWorld()).thenReturn(worldMock);
		when(locationMock.getBlockX()).thenReturn(123);
		when(locationMock.getBlockY()).thenReturn(64);
		when(locationMock.getBlockZ()).thenReturn(-789);

		// Act
		ResultMap result = locationProcessor.resolveContext("HOME", contextMap, locationMock);

		// Assert
		assertThat(result.get("HOME_LOCATION_WORLD"), is("test_world"));
		assertThat(result.get("HOME_LOCATION_X"), is("123"));
		assertThat(result.get("HOME_LOCATION_Y"), is("64"));
		assertThat(result.get("HOME_LOCATION_Z"), is("-789"));
		assertThat(result.get("HOME_LOCATION"), is("test_world [123, 64, -789]"));
	}

	@Disabled
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
