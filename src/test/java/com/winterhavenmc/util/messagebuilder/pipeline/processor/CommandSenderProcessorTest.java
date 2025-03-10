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
import com.winterhavenmc.util.messagebuilder.pipeline.processors.CommandSenderProcessor;
import com.winterhavenmc.util.messagebuilder.pipeline.processors.MacroProcessor;
import com.winterhavenmc.util.messagebuilder.pipeline.processors.ResultMap;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CommandSenderProcessorTest {

	@Mock Plugin pluginMock;
	@Mock Player playerMock;
	@Mock World worldMock;

	MacroProcessor macroProcessor;

	@BeforeEach
	public void setUp() {
		macroProcessor = new CommandSenderProcessor();
	}

	@AfterEach
	public void tearDown() {
		pluginMock = null;
		macroProcessor = null;
	}


	@Test
	void testResolveContext_parameter_null_key() {
		ContextMap contextMap = new ContextMap(playerMock, MessageId.ENABLED_MESSAGE.name());
		MacroProcessor macroProcessor = new CommandSenderProcessor();
		ValidationException exception = assertThrows(ValidationException.class,
				() -> macroProcessor.resolveContext(null, contextMap));

		assertEquals("The parameter 'key' cannot be null.", exception.getMessage());
	}


	@Test
	void testResolveContext_parameter_empty_key() {
		ContextMap contextMap = new ContextMap(playerMock, MessageId.ENABLED_MESSAGE.name());
		MacroProcessor macroProcessor = new CommandSenderProcessor();
		ValidationException exception = assertThrows(ValidationException.class,
				() -> macroProcessor.resolveContext("", contextMap));

		assertEquals("The parameter 'key' cannot be empty.", exception.getMessage());
	}


	@Test
	void testResolveContext_parameter_null_context_map() {
		MacroProcessor macroProcessor = new CommandSenderProcessor();
		ValidationException exception = assertThrows(ValidationException.class,
				() -> macroProcessor.resolveContext("KEY", null));

		assertEquals("The parameter 'contextMap' cannot be null.", exception.getMessage());
	}


	@Test
	void resolveContext() {
		// Arrange
		when(playerMock.getName()).thenReturn("player one");
		when(playerMock.getDisplayName()).thenReturn("&aPlayer One");
		when(playerMock.getUniqueId()).thenReturn(new UUID(42, 42));
		when(worldMock.getName()).thenReturn("test_world");
		Location location = new Location(worldMock, 10, 20, 30);
		when(playerMock.getLocation()).thenReturn(location);

		String key = "SOME_KEY";
		ContextMap contextMap = new ContextMap(playerMock, MessageId.ENABLED_MESSAGE.name());
		contextMap.put(key, playerMock);

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(key, contextMap);

		// Assert
		assertTrue(resultMap.containsKey(key));
		assertNotNull(resultMap.get(key));
	}


	@Test
	void resolveContext_not_command_sender() {
		// Arrange
		String key = "SOME_KEY";
		ContextMap contextMap = new ContextMap(playerMock, MessageId.ENABLED_MESSAGE.name());
		contextMap.put(key, 42);

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(key, contextMap);

		// Assert
		assertTrue(resultMap.isEmpty());
	}

}
