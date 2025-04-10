/*
 * Copyright (c) 2024-2025 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.pipeline.processors;

import com.winterhavenmc.util.messagebuilder.recipient.InvalidRecipient;
import com.winterhavenmc.util.messagebuilder.recipient.RecipientResult;
import com.winterhavenmc.util.messagebuilder.recipient.ValidRecipient;
import com.winterhavenmc.util.messagebuilder.pipeline.context.ContextMap;
import com.winterhavenmc.util.messagebuilder.resources.RecordKey;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;

import static com.winterhavenmc.util.messagebuilder.validation.ErrorMessageKey.PARAMETER_INVALID;
import static com.winterhavenmc.util.messagebuilder.validation.Parameter.RECIPIENT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


@ExtendWith(MockitoExtension.class)
class LocationProcessorTest
{
	@Mock World worldMock;
	@Mock Location locationMock;
	@Mock Player playerMock;

	// real location processor
	LocationProcessor locationProcessor;
	ContextMap contextMap;
	ValidRecipient recipient;
	RecordKey key;


	@BeforeEach
	public void setUp()
	{
		recipient = switch (RecipientResult.from(playerMock)) {
			case ValidRecipient validRecipient -> validRecipient;
			case InvalidRecipient ignored -> throw new ValidationException(PARAMETER_INVALID, RECIPIENT);
		};
		key = RecordKey.of("HOME").orElseThrow();
		contextMap = ContextMap.of(recipient, key).orElseThrow();
		locationProcessor = new LocationProcessor();
	}


	@Test
	void testResolveContext_ValidLocation()
	{
		// Arrange
		when(worldMock.getName()).thenReturn("test_world");
		when(locationMock.getWorld()).thenReturn(worldMock);
		when(locationMock.getBlockX()).thenReturn(123);
		when(locationMock.getBlockY()).thenReturn(64);
		when(locationMock.getBlockZ()).thenReturn(-789);

		contextMap.put(key, locationMock);

		// Act
		ResultMap result = locationProcessor.resolveContext(key, contextMap);

		// Assert
		assertNotNull(result);
		assertEquals("test_world", result.get("HOME.LOCATION.WORLD"));
		assertEquals("123", result.get("HOME.LOCATION.X"));
		assertEquals("64", result.get("HOME.LOCATION.Y"));
		assertEquals("-789", result.get("HOME.LOCATION.Z"));
		assertEquals("test_world [123, 64, -789]", result.get("HOME.LOCATION"));
	}


	@Test
	void testResolveContext_ValidLocation2()
	{
		// Arrange
		RecordKey playerKey = RecordKey.of("PLAYER").orElseThrow();
		when(worldMock.getName()).thenReturn("test_world");
		when(locationMock.getWorld()).thenReturn(worldMock);
		when(locationMock.getBlockX()).thenReturn(123);
		when(locationMock.getBlockY()).thenReturn(64);
		when(locationMock.getBlockZ()).thenReturn(-789);

		contextMap.put(playerKey, locationMock);

		// Act
		ResultMap result = locationProcessor.resolveContext(playerKey, contextMap);

		// Assert
		assertNotNull(result);
		assertEquals("test_world", result.get("PLAYER.LOCATION.WORLD"));
		assertEquals("123", result.get("PLAYER.LOCATION.X"));
		assertEquals("64", result.get("PLAYER.LOCATION.Y"));
		assertEquals("-789", result.get("PLAYER.LOCATION.Z"));
		assertEquals("test_world [123, 64, -789]", result.get("PLAYER.LOCATION"));
	}


	@Test
	void testResolveContext_MissingWorld()
	{
		// Arrange
		when(locationMock.getWorld()).thenReturn(null);
		when(locationMock.getBlockX()).thenReturn(123);
		when(locationMock.getBlockY()).thenReturn(64);
		when(locationMock.getBlockZ()).thenReturn(-789);

		contextMap.put(key, locationMock);

		// Act
		ResultMap result = locationProcessor.resolveContext(key, contextMap);

		// Assert
		assertThat(result.get("HOME.LOCATION.WORLD"), is("???"));
		assertThat(result.get("HOME.LOCATION"), is("??? [123, 64, -789]"));
	}


	@Test
	void testResolveContext_KeyWithoutLocationSuffix()
	{
		// Arrange
		when(worldMock.getName()).thenReturn("test_world");
		when(locationMock.getWorld()).thenReturn(worldMock);
		when(locationMock.getBlockX()).thenReturn(123);
		when(locationMock.getBlockY()).thenReturn(64);
		when(locationMock.getBlockZ()).thenReturn(-789);

		contextMap.put(key, locationMock);

		// Act
		ResultMap result = locationProcessor.resolveContext(key, contextMap);

		// Assert
		assertThat(result.get("HOME.LOCATION.WORLD"), is("test_world"));
		assertThat(result.get("HOME.LOCATION.X"), is("123"));
		assertThat(result.get("HOME.LOCATION.Y"), is("64"));
		assertThat(result.get("HOME.LOCATION.Z"), is("-789"));
		assertThat(result.get("HOME.LOCATION"), is("test_world [123, 64, -789]"));
	}


	@Test
	void resolveContext_mismatched_type()
	{
		// Arrange
		Duration duration  = Duration.ofMillis(2000);
		ContextMap contextMap = ContextMap.of(recipient, key).orElseThrow();
		contextMap.put(key, duration);
		MacroProcessor macroProcessor = new LocationProcessor();

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(key, contextMap);

		// Assert
		assertFalse(resultMap.containsKey(key.toString()));
	}

}
