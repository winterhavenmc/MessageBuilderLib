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

import org.bukkit.entity.Player;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class NumberProcessorTest {

	@Mock private Player playerMock;

	private MacroProcessor macroProcessor;
	private ContextMap contextMap;


	@BeforeEach
	public void setUp() {
		macroProcessor = new NumberProcessor();
		contextMap = new ContextMap(playerMock);
	}

	@AfterEach
	public void tearDown() {
		playerMock = null;
		macroProcessor = null;
		contextMap = null;
	}


	@Test
	void execute_integer() {
		// Arrange
		String key = "SOME_INTEGER";
		Integer number = 42;
		contextMap.put(key, number);

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(key, contextMap);

		// Assert
		assertTrue(resultMap.containsKey("SOME_INTEGER"));
		assertEquals("42", resultMap.get("SOME_INTEGER"));
	}


	@Test
	void execute_null_integer() {
		// Arrange
		String key = "SOME_NULL_INTEGER";
		Integer number = null;
		contextMap.put(key, number);

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(key, contextMap);

		// Assert
		assert(resultMap.containsKey("SOME_NULL_INTEGER"));
	}

	@Test
	void execute_long() {
		// Arrange
		String key = "SOME_LONG";
		Long number = 420L;
		contextMap.put(key, number);

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(key, contextMap);

		// Assert
		assertTrue(resultMap.containsKey("SOME_LONG"));
		assertEquals("420", resultMap.get("SOME_LONG"));
	}

	@Test
	void execute_null_long() {
		// Arrange
		String key = "SOME_NULL_LONG";
		Long number = null;
		contextMap.put(key,number);

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(key, contextMap);

		// Assert
		assertTrue(resultMap.containsKey("SOME_NULL_LONG"));
		assertEquals("NULL", resultMap.get(key));
	}

}
