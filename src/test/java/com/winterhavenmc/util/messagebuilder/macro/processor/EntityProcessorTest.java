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
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class EntityProcessorTest {

	@Mock Player playerMock;
	@Mock Entity entityMock;


	@AfterEach
	void tearDown() {
		playerMock = null;
		entityMock = null;
	}

	@Test
	void testResolveContext_parameter_null_key() {
		ContextMap contextMap = new ContextMap(playerMock, MessageId.ENABLED_MESSAGE.name());
		MacroProcessor macroProcessor = new EntityProcessor();
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> macroProcessor.resolveContext(null, contextMap));

		assertEquals("The parameter 'key' cannot be null.", exception.getMessage());
	}


	@Test
	void testResolveContext_parameter_empty_key() {
		ContextMap contextMap = new ContextMap(playerMock, MessageId.ENABLED_MESSAGE.name());
		MacroProcessor macroProcessor = new EntityProcessor();
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> macroProcessor.resolveContext("", contextMap));

		assertEquals("The parameter 'key' cannot be empty.", exception.getMessage());
	}


	@Test
	void testResolveContext_parameter_null_context_map() {
		MacroProcessor macroProcessor = new EntityProcessor();
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> macroProcessor.resolveContext("KEY", null));

		assertEquals("The parameter 'contextMap' cannot be null.", exception.getMessage());
	}


	@Test
	void resolveContext() {
		// Arrange
		when(entityMock.getName()).thenReturn("Entity Name");
		String keyPath = "ENTITY";
		ContextMap contextMap = new ContextMap(playerMock, MessageId.ENABLED_MESSAGE.name());
		contextMap.put(keyPath, entityMock);

		MacroProcessor macroProcessor = new EntityProcessor();

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(keyPath, contextMap);

		// Assert
		assertTrue(resultMap.containsKey(keyPath));
		assertEquals("Entity Name", resultMap.get(keyPath));
	}

	@Test
	void resolveContext_not_entity() {
		// Arrange
		String keyPath = "ENTITY";
		ContextMap contextMap = new ContextMap(playerMock, MessageId.ENABLED_MESSAGE.name());
		contextMap.put(keyPath, "string");

		MacroProcessor macroProcessor = new EntityProcessor();

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(keyPath, contextMap);

		// Assert
		assertFalse(resultMap.containsKey(keyPath));
	}

}
