/*
 * Copyright (c) 2022-2025 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.pipeline.processor;

import com.winterhavenmc.util.messagebuilder.pipeline.ContextMap;
import com.winterhavenmc.util.messagebuilder.messages.MessageId;

import com.winterhavenmc.util.messagebuilder.pipeline.processors.MacroProcessor;
import com.winterhavenmc.util.messagebuilder.pipeline.processors.OfflinePlayerProcessor;
import com.winterhavenmc.util.messagebuilder.pipeline.processors.ResultMap;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class OfflinePlayerProcessorTest {

	@Mock Player playerMock;
	@Mock OfflinePlayer offlinePlayerMock;
	@Mock Location locationMock;

	@AfterEach
	public void tearDown() {
		offlinePlayerMock = null;
	}

	@Test
	void resolveContextTest() {
		// Arrange
		when(offlinePlayerMock.getName()).thenReturn("Offline Player");
		when(offlinePlayerMock.getUniqueId()).thenReturn(new UUID(42, 42));
		when(offlinePlayerMock.getLocation()).thenReturn(locationMock);

		String key = "KEY";
		MacroProcessor macroProcessor = new OfflinePlayerProcessor();
		ContextMap contextMap = new ContextMap(playerMock, MessageId.ENABLED_MESSAGE.name());

		// Act
		contextMap.put(key, offlinePlayerMock);
		ResultMap resultMap = macroProcessor.resolveContext(key, contextMap);

		// Assert
		assertFalse(resultMap.isEmpty());
		assertTrue(resultMap.containsKey(key));
	}


	@Test
	void resolveContext_with_null_key() {
		// Arrange
		MacroProcessor macroProcessor = new OfflinePlayerProcessor();
		ContextMap contextMap = new ContextMap(playerMock, MessageId.ENABLED_MESSAGE.name());

		// Act
		ValidationException exception = assertThrows(ValidationException.class,
				() -> macroProcessor.resolveContext(null, contextMap));

		// Assert
		assertEquals("The parameter 'key' cannot be null.", exception.getMessage());
	}

	@Test
	void resolveContext_with_empty_key() {
		// Arrange
		MacroProcessor macroProcessor = new OfflinePlayerProcessor();
		ContextMap contextMap = new ContextMap(playerMock, MessageId.ENABLED_MESSAGE.name());

		// Act
		ValidationException exception = assertThrows(ValidationException.class,
				() -> macroProcessor.resolveContext("", contextMap));

		// Assert
		assertEquals("The parameter 'key' cannot be empty.", exception.getMessage());
	}

	@Test
	void resolveContext_with_null_contextMap() {
		// Arrange
		MacroProcessor macroProcessor = new OfflinePlayerProcessor();

		// Act
		ValidationException exception = assertThrows(ValidationException.class,
				() -> macroProcessor.resolveContext("KEY", null));

		// Assert
		assertEquals("The parameter 'contextMap' cannot be null.", exception.getMessage());
	}


	@Test
	void resolveContext_mismatched_type() {

		String keyPath = "SOME_NAME";
		Duration duration  = Duration.ofMillis(2000);

		ContextMap contextMap = new ContextMap(playerMock, MessageId.ENABLED_MESSAGE.name());

		contextMap.put(keyPath, duration);

		MacroProcessor macroProcessor = new OfflinePlayerProcessor();

		ResultMap resultMap = macroProcessor.resolveContext(keyPath, contextMap);

		assertFalse(resultMap.containsKey(keyPath));
	}

}
