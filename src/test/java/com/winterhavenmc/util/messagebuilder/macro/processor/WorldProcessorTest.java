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

import org.bukkit.World;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class WorldProcessorTest {

	@Mock Player playerMock;
	@Mock World worldMock;


	@AfterEach
	public void tearDown() {
		playerMock = null;
		worldMock = null;
	}


	@Test
	void resolveContext() {
		// Arrange
		when(worldMock.getName()).thenReturn("test_world");

		String keyPath = "SOME_WORLD";
		ContextMap contextMap = new ContextMap(playerMock);
		MacroProcessor macroProcessor = new WorldProcessor();
		contextMap.put(keyPath, ContextContainer.of(worldMock, ProcessorType.WORLD));

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(keyPath, contextMap);

		// Assert
		assertTrue(resultMap.containsKey(keyPath));
		assertEquals("test_world", resultMap.get(keyPath));
	}


	@Test
	void resolveContext_with_null_world() {
		// Arrange
		String key = "SOME_WORLD";
		ContextMap contextMap = new ContextMap(playerMock);
		MacroProcessor macroProcessor = new WorldProcessor();
		contextMap.put(key, ContextContainer.of(null, ProcessorType.WORLD));

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(key, contextMap);

		// Assert
		assertNull(resultMap.get(key));
	}

}
