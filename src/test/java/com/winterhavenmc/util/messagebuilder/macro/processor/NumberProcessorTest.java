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
import com.winterhavenmc.util.messagebuilder.language.LanguageQueryHandler;

import org.bukkit.entity.Player;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class NumberProcessorTest {

	@Mock private LanguageQueryHandler queryHandlerMock;
	@Mock private Player playerMock;

	private MacroProcessor macroProcessor;
	private ContextMap contextMap;


	@BeforeEach
	public void setUp() {
		macroProcessor = new NumberProcessor(queryHandlerMock);
		contextMap = new ContextMap(playerMock);
	}

	@AfterEach
	public void tearDown() {
		queryHandlerMock = null;
		macroProcessor = null;
	}


	@Test
	void execute_integer() {
		// Arrange
		String key = "SOME_INTEGER";
		Integer number = 42;
		contextMap.put(key, ContextContainer.of(number, ProcessorType.NUMBER));

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(key, contextMap, number);

		// Assert
		assertTrue(resultMap.containsKey("SOME_INTEGER"));
		assertEquals("42", resultMap.get("SOME_INTEGER"));
	}


	@Test
	void execute_null_integer() {
		// Arrange
		String key = "SOME_NULL_INTEGER";
		Integer number = null;
		contextMap.put(key, ContextContainer.of(number, ProcessorType.NUMBER));

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(key, contextMap, number);

		// Assert
		assertFalse(resultMap.containsKey("SOME_NULL_INTEGER"));
	}

	@Test
	void execute_long() {
		// Arrange
		String key = "SOME_LONG";
		Long number = 420L;
		contextMap.put(key, ContextContainer.of(number, ProcessorType.NUMBER));

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(key, contextMap, number);

		// Assert
		assertTrue(resultMap.containsKey("SOME_LONG"));
		assertEquals("420", resultMap.get("SOME_LONG"));
	}

	@Test
	void execute_null_long() {
		// Arrange
		String key = "SOME_NULL_LONG";
		Long number = null;
		contextMap.put(key, ContextContainer.of(number, ProcessorType.NUMBER));

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(key, contextMap, number);

		// Assert
		assertFalse(resultMap.containsKey("SOME_NULL_LONG"));
	}

	@Test
	void execute_duration() {
		// Arrange
		String key = "SOME_DURATION";
		Long number = 12000L;
		contextMap.put(key, ContextContainer.of(number, ProcessorType.NUMBER));

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(key, contextMap, number);

		// Assert
		assertTrue(resultMap.containsKey("SOME_DURATION"));
		assertEquals("12 seconds", resultMap.get("SOME_DURATION"));
	}

	@Test
	void execute_duration_minutes() {
		// Arrange
		String key = "SOME_DURATION_MINUTES";
		Long number = 61_000L;
		contextMap.put(key, ContextContainer.of(number, ProcessorType.NUMBER));

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(key, contextMap, number);

		// Assert
		assertTrue(resultMap.containsKey("SOME_DURATION_MINUTES"));
		assertEquals("1 minute", resultMap.get("SOME_DURATION_MINUTES"));
	}

}
