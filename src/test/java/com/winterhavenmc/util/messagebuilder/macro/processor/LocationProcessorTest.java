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
import com.winterhavenmc.util.messagebuilder.util.LocalizedException;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


@ExtendWith(MockitoExtension.class)
class LocationProcessorTest {

	@Mock World worldMock;
	@Mock Location locationMock;
	@Mock Player playerMock;

	// real location processor
	LocationProcessor locationProcessor;
	ContextMap contextMap;


	@BeforeEach
	public void setUp() {
		// real context map
		contextMap = new ContextMap(playerMock);

		// Initialize the processor
		locationProcessor = new LocationProcessor();
	}


	@Test
	void testResolveContext_parameter_null_key() {
		ContextMap contextMap = new ContextMap(playerMock);
		MacroProcessor macroProcessor = new LocationProcessor();
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> macroProcessor.resolveContext(null, contextMap));

		assertEquals("The parameter 'key' cannot be null.", exception.getMessage());
	}


	@Test
	void testResolveContext_parameter_empty_key() {
		ContextMap contextMap = new ContextMap(playerMock);
		MacroProcessor macroProcessor = new LocationProcessor();
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> macroProcessor.resolveContext("", contextMap));

		assertEquals("The parameter 'key' cannot be empty.", exception.getMessage());
	}


	@Test
	void testResolveContext_parameter_null_context_map() {
		MacroProcessor macroProcessor = new LocationProcessor();
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> macroProcessor.resolveContext("KEY", null));

		assertEquals("The parameter 'contextMap' cannot be null.", exception.getMessage());
	}


	@Test
	void testResolveContext_ValidLocation() {
		// Arrange
		when(worldMock.getName()).thenReturn("test_world");
		when(locationMock.getWorld()).thenReturn(worldMock);
		when(locationMock.getBlockX()).thenReturn(123);
		when(locationMock.getBlockY()).thenReturn(64);
		when(locationMock.getBlockZ()).thenReturn(-789);

		contextMap.put("HOME",locationMock);

		// Act
		ResultMap result = locationProcessor.resolveContext("HOME", contextMap);

		// Assert
		assertThat(result, is(notNullValue()));
		assertThat(result.get("HOME.LOCATION.WORLD"), is("test_world"));
		assertThat(result.get("HOME.LOCATION.X"), is("123"));
		assertThat(result.get("HOME.LOCATION.Y"), is("64"));
		assertThat(result.get("HOME.LOCATION.Z"), is("-789"));
		assertThat(result.get("HOME.LOCATION"), is("test_world [123, 64, -789]"));
	}

	@Test
	void testResolveContext_ValidLocation2() {
		// Arrange
		when(worldMock.getName()).thenReturn("test_world");
		when(locationMock.getWorld()).thenReturn(worldMock);
		when(locationMock.getBlockX()).thenReturn(123);
		when(locationMock.getBlockY()).thenReturn(64);
		when(locationMock.getBlockZ()).thenReturn(-789);

		String key = "PLAYER.LOCATION";
		contextMap.put(key, locationMock);

		// Act
		ResultMap result = locationProcessor.resolveContext(key, contextMap);

		// Assert
		assertThat(result, is(notNullValue()));
		assertThat(result.get("PLAYER.LOCATION.WORLD"), is("test_world"));
		assertThat(result.get("PLAYER.LOCATION.X"), is("123"));
		assertThat(result.get("PLAYER.LOCATION.Y"), is("64"));
		assertThat(result.get("PLAYER.LOCATION.Z"), is("-789"));
		assertThat(result.get("PLAYER.LOCATION"), is("test_world [123, 64, -789]"));
	}

	@Test
	void testResolveContext_MissingWorld() {
		// Arrange
		when(locationMock.getWorld()).thenReturn(null);
		when(locationMock.getBlockX()).thenReturn(123);
		when(locationMock.getBlockY()).thenReturn(64);
		when(locationMock.getBlockZ()).thenReturn(-789);

		contextMap.put("HOME", locationMock);

		// Act
		ResultMap result = locationProcessor.resolveContext("HOME", contextMap);

		// Assert
		assertThat(result.get("HOME.LOCATION.WORLD"), is("???"));
		assertThat(result.get("HOME.LOCATION"), is("??? [123, 64, -789]"));
	}

	@Test
	void testResolveContext_EmptyKey() {
		// Act & Assert
		assertThrows(IllegalArgumentException.class,
				() -> locationProcessor.resolveContext("", contextMap),
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

		contextMap.put("HOME", locationMock);

		// Act
		ResultMap result = locationProcessor.resolveContext("HOME", contextMap);

		// Assert
		assertThat(result.get("HOME.LOCATION.WORLD"), is("test_world"));
		assertThat(result.get("HOME.LOCATION.X"), is("123"));
		assertThat(result.get("HOME.LOCATION.Y"), is("64"));
		assertThat(result.get("HOME.LOCATION.Z"), is("-789"));
		assertThat(result.get("HOME.LOCATION"), is("test_world [123, 64, -789]"));
	}

	@Test
	void resolveContext_mismatched_type() {
		// Arrange
		String keyPath = "SOME_NAME";
		Duration duration  = Duration.ofMillis(2000);
		ContextMap contextMap = new ContextMap(playerMock);
		contextMap.put(keyPath, duration);
		MacroProcessor macroProcessor = new LocationProcessor();

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(keyPath, contextMap);

		// Assert
		assertFalse(resultMap.containsKey(keyPath));
	}

}
