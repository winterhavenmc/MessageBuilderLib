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

package com.winterhavenmc.util.messagebuilder.pipeline.processors;

import com.winterhavenmc.util.messagebuilder.pipeline.ContextMap;
import com.winterhavenmc.util.messagebuilder.messages.MessageId;

import com.winterhavenmc.util.messagebuilder.pipeline.processors.MacroProcessor;
import com.winterhavenmc.util.messagebuilder.pipeline.processors.OfflinePlayerProcessor;
import com.winterhavenmc.util.messagebuilder.pipeline.processors.ResultMap;
import com.winterhavenmc.util.messagebuilder.resources.RecordKey;
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

	RecordKey macroKey = RecordKey.of(MessageId.ENABLED_MESSAGE).orElseThrow();

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

		MacroProcessor macroProcessor = new OfflinePlayerProcessor();
		ContextMap contextMap = new ContextMap(playerMock, macroKey);

		// Act
		contextMap.put(macroKey, offlinePlayerMock);
		ResultMap resultMap = macroProcessor.resolveContext(macroKey, contextMap);

		// Assert
		assertFalse(resultMap.isEmpty());
		assertTrue(resultMap.containsKey(macroKey.toString()));
	}


	@Test
	void resolveContext_with_null_contextMap() {
		// Arrange
		MacroProcessor macroProcessor = new OfflinePlayerProcessor();

		// Act
		ValidationException exception = assertThrows(ValidationException.class,
				() -> macroProcessor.resolveContext(macroKey, null));

		// Assert
		assertEquals("The parameter 'contextMap' cannot be null.", exception.getMessage());
	}


	@Test
	void resolveContext_mismatched_type() {

		Duration duration  = Duration.ofMillis(2000);

		ContextMap contextMap = new ContextMap(playerMock, macroKey);

		contextMap.put(macroKey, duration);

		MacroProcessor macroProcessor = new OfflinePlayerProcessor();

		ResultMap resultMap = macroProcessor.resolveContext(macroKey, contextMap);

		assertFalse(resultMap.containsKey(macroKey.toString()));
	}

}
