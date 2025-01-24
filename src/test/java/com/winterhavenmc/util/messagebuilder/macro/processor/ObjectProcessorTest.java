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

import org.bukkit.entity.Player;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class ObjectProcessorTest {

	@Mock private Player playerMock;
	private MacroProcessor macroProcessor;


	@BeforeEach
	public void setUp() {
		macroProcessor = new ObjectProcessor();
	}

	@AfterEach
	public void tearDown() {
		macroProcessor = null;
	}


	@Test
	void resolveContext_integer() {
		// Arrange
		String keyPath = "SOME_INTEGER";
		Integer number = 42;
		ContextMap contextMap = new ContextMap(playerMock);
		String contextKey = SourceKey.create(Source.MACRO, keyPath);
		contextMap.put(contextKey, ContextContainer.of(number, ProcessorType.NUMBER));

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(contextKey, contextMap, number);

		// Assert
		assertTrue(resultMap.containsKey(contextKey));
		assertEquals("42", resultMap.get(contextKey));
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
