/*
 * Copyright (c) 2022 Tim Savage.
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

import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import com.winterhavenmc.util.messagebuilder.util.LocalizedException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CommandSenderProcessorTest {

	@Mock Plugin pluginMock;
	@Mock Player playerMock;

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
		ContextMap<MessageId> contextMap = new ContextMap<>(playerMock, MessageId.ENABLED_MESSAGE);
		MacroProcessor macroProcessor = new CommandSenderProcessor();
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> macroProcessor.resolveContext(null, contextMap));

		assertEquals("The parameter 'key' cannot be null.", exception.getMessage());
	}


	@Test
	void testResolveContext_parameter_empty_key() {
		ContextMap<MessageId> contextMap = new ContextMap<>(playerMock, MessageId.ENABLED_MESSAGE);
		MacroProcessor macroProcessor = new CommandSenderProcessor();
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> macroProcessor.resolveContext("", contextMap));

		assertEquals("The parameter 'key' cannot be empty.", exception.getMessage());
	}


	@Test
	void testResolveContext_parameter_null_context_map() {
		MacroProcessor macroProcessor = new CommandSenderProcessor();
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> macroProcessor.resolveContext("KEY", null));

		assertEquals("The parameter 'contextMap' cannot be null.", exception.getMessage());
	}


	@Test
	void resolveContext() {
		// Arrange
		when(playerMock.getName()).thenReturn("player one");
		String keyPath = "SOME_KEY";
		ContextMap<MessageId> contextMap = new ContextMap<>(playerMock, MessageId.ENABLED_MESSAGE);
		contextMap.put(keyPath, playerMock);

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(keyPath, contextMap);

		// Assert
		assertTrue(resultMap.containsKey(keyPath));
		assertNotNull(resultMap.get(keyPath));
	}

	@Test
	void resolveContext_not_command_sender() {
		// Arrange
		String keyPath = "SOME_KEY";
		ContextMap<MessageId> contextMap = new ContextMap<>(playerMock, MessageId.ENABLED_MESSAGE);
		contextMap.put(keyPath, 42);

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(keyPath, contextMap);

		// Assert
		assertTrue(resultMap.isEmpty());
	}

}
