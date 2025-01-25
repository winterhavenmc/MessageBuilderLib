/*
 * Copyright (c) 2025 Tim Savage.
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

import org.bukkit.entity.Player;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class NullProcessorTest {

	@Mock Player playerMock;

	@AfterEach
	void tearDown() {
		playerMock = null;
	}


	@Test
	void testResolveContext_parameter_null_key() {
		ContextMap contextMap = new ContextMap(playerMock);
		MacroProcessor macroProcessor = new NullProcessor();
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> macroProcessor.resolveContext(null, contextMap));

		assertEquals("The parameter 'key' cannot be null.", exception.getMessage());
	}


	@Test
	void testResolveContext_parameter_empty_key() {
		ContextMap contextMap = new ContextMap(playerMock);
		MacroProcessor macroProcessor = new NullProcessor();
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> macroProcessor.resolveContext("", contextMap));

		assertEquals("The parameter 'key' cannot be empty.", exception.getMessage());
	}


	@Test
	void testResolveContext_parameter_null_context_map() {
		MacroProcessor macroProcessor = new NullProcessor();

		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> macroProcessor.resolveContext("KEY", null));

		assertEquals("The parameter 'contextMap' cannot be null.", exception.getMessage());
	}


	@Test
	void resolveContext() {
		// Arrange
		String key = "KEY";
		NullProcessor nullProcessor = new NullProcessor();
		ContextMap contextMap = new ContextMap(playerMock);
		contextMap.put(key, null);

		// Act
		ResultMap resultMap = nullProcessor.resolveContext(key, contextMap);

		// Assert
		assertEquals("NULL", resultMap.get(key));
	}


	@Test
	void resolveContext_not_null() {
		// Arrange
		String key = "KEY";
		NullProcessor nullProcessor = new NullProcessor();
		ContextMap contextMap = new ContextMap(playerMock);
		contextMap.put(key, 42);

		// Act
		ResultMap resultMap = nullProcessor.resolveContext(key, contextMap);

		// Assert
		assertEquals("NULL", resultMap.get(key));
	}

}
