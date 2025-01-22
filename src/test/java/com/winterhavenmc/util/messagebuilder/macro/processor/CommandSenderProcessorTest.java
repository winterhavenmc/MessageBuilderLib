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


import com.winterhavenmc.util.messagebuilder.context.ContextContainer;
import com.winterhavenmc.util.messagebuilder.context.ContextMap;
import com.winterhavenmc.util.messagebuilder.context.Source;
import com.winterhavenmc.util.messagebuilder.context.SourceKey;
import com.winterhavenmc.util.messagebuilder.resources.language.LanguageQueryHandler;

import com.winterhavenmc.util.messagebuilder.util.LocalizedException;
import org.bukkit.command.CommandSender;
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
	@Mock LanguageQueryHandler queryHandlerMock;
	@Mock Player playerMock;

	MacroProcessor macroProcessor;

	@BeforeEach
	public void setUp() {
		macroProcessor = new CommandSenderProcessor(queryHandlerMock);
	}

	@AfterEach
	public void tearDown() {
		pluginMock = null;
		queryHandlerMock = null;
		macroProcessor = null;
	}


	@Test
	void testConstructor_parameter_valid() {
		// Arrange & Act
		CommandSenderProcessor processor = new CommandSenderProcessor(queryHandlerMock);

		// Assert
		assertNotNull(processor);
	}

	@Test
	void testConstructor_parameter_null() {
		// Arrange & Act
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> new CommandSenderProcessor(null));

		// Assert
		assertEquals("The parameter 'queryHandler' cannot be null.", exception.getMessage());
	}



	@Test
	void testResolveContext() {


	}


	@Test
	void resolveContext_integer() {
		// Arrange
		when(playerMock.getName()).thenReturn("player one");
		String keyPath = "SOME_KEY";
		ContextMap contextMap = new ContextMap(playerMock);
		String contextKey = SourceKey.create(Source.MACRO, keyPath);
		contextMap.put(contextKey, ContextContainer.of(playerMock, ProcessorType.COMMAND_SENDER));

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(contextKey, contextMap, playerMock);

		// Assert
		assertTrue(resultMap.containsKey(contextKey));
		assertNotNull(resultMap.get(contextKey));

	}

	@Test
	void resolveContext_null() {
		// Arrange
		String keyPath = "SOME_INTEGER";
		Integer nullValue = null;
		ContextMap contextMap = new ContextMap(playerMock);
		String contextKey = SourceKey.create(Source.MACRO, keyPath);
		contextMap.put(contextKey, ContextContainer.of(nullValue, ProcessorType.NUMBER));

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(contextKey, contextMap, nullValue);

		// Assert
		assertFalse(resultMap.containsKey(contextKey));
	}

}
