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
import com.winterhavenmc.util.messagebuilder.resources.RecordKey;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;
import com.winterhavenmc.util.messagebuilder.util.MultiverseHelper;

import org.bukkit.World;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class WorldProcessorTest {

	@Mock Player playerMock;
	@Mock World worldMock;

	static MockedStatic<MultiverseHelper> mockStatic;


	@BeforeAll
	static void preSetup() {
		mockStatic = mockStatic(MultiverseHelper.class);
	}

	@AfterEach
	void tearDown() {
		playerMock = null;
		worldMock = null;
	}


	@Test
	void testResolveContext_parameter_null_context_map() {
		MacroProcessor macroProcessor = new WorldProcessor();
		RecordKey recordKey = RecordKey.of("KEY").orElseThrow();
		ValidationException exception = assertThrows(ValidationException.class,
				() -> macroProcessor.resolveContext(recordKey, null));

		assertEquals("The parameter 'contextMap' cannot be null.", exception.getMessage());
	}


	@Test
	void resolveContext_with_multiverse() {
		// Arrange
		when(worldMock.getName()).thenReturn("test_world");
		mockStatic.when(() -> MultiverseHelper.getAlias(worldMock)).thenReturn(Optional.of("MV Alias"));

		RecordKey recordKey = RecordKey.of("SOME_WORLD").orElseThrow();
		ContextMap contextMap = new ContextMap(playerMock, recordKey);
		MacroProcessor macroProcessor = new WorldProcessor();
		contextMap.put(recordKey, worldMock);

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(recordKey, contextMap);

		// Assert
		assertTrue(resultMap.containsKey(recordKey.toString()));
		assertEquals("MV Alias", resultMap.get(recordKey.toString()));
	}


	@Test
	void resolveContext_without_multiverse() {
		// Arrange
		when(worldMock.getName()).thenReturn("test_world");
		mockStatic.when(() -> MultiverseHelper.getAlias(worldMock)).thenReturn(Optional.empty());

		RecordKey recordKey = RecordKey.of("SOME_WORLD").orElseThrow();
		ContextMap contextMap = new ContextMap(playerMock, RecordKey.of(MessageId.ENABLED_MESSAGE).orElseThrow());
		MacroProcessor macroProcessor = new WorldProcessor();
		contextMap.put(recordKey, worldMock);

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(recordKey, contextMap);

		// Assert
		assertTrue(resultMap.containsKey(recordKey.toString()));
		assertEquals("test_world", resultMap.get(recordKey.toString()));
	}


	@Test
	void resolveContext_with_null_world() {
		// Arrange
		RecordKey recordKey = RecordKey.of("SOME_WORLD").orElseThrow();
		ContextMap contextMap = new ContextMap(playerMock, RecordKey.of(MessageId.ENABLED_MESSAGE).orElseThrow());
		MacroProcessor macroProcessor = new WorldProcessor();
		contextMap.put(recordKey, null);

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(recordKey, contextMap);

		// Assert
		assertNull(resultMap.get(recordKey.toString()));
	}

}
