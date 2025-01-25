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
import com.winterhavenmc.util.messagebuilder.util.LocalizedException;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
class OfflinePlayerProcessorTest {

	@Mock Player playerMock;
	@Mock OfflinePlayer offlinePlayerMock;

	@AfterEach
	public void tearDown() {
		offlinePlayerMock = null;
	}

	@Test
	void resolveContextTest() {
		// Arrange
		String key = "KEY";
		MacroProcessor macroProcessor = new OfflinePlayerProcessor();
		ContextMap contextMap = new ContextMap(playerMock);

		contextMap.put(key, offlinePlayerMock);

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(key, contextMap);

		// Assert
		assertFalse(resultMap.isEmpty());
		assertTrue(resultMap.containsKey(key));
	}


	@Test
	void resolveContext_with_null_key() {
		// Arrange
		MacroProcessor macroProcessor = new OfflinePlayerProcessor();
		ContextMap contextMap = new ContextMap(playerMock);

		// Act
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> macroProcessor.resolveContext(null, contextMap));

		// Assert
		assertEquals("The parameter 'keyPath' cannot be null.", exception.getMessage());
	}

	@Test
	void resolveContext_with_empty_key() {
		// Arrange
		MacroProcessor macroProcessor = new OfflinePlayerProcessor();
		ContextMap contextMap = new ContextMap(playerMock);

		// Act
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> macroProcessor.resolveContext("", contextMap));

		// Assert
		assertEquals("The parameter 'keyPath' cannot be empty.", exception.getMessage());
	}

	@Test
	void resolveContext_with_null_contextMap() {
		// Arrange
		MacroProcessor macroProcessor = new OfflinePlayerProcessor();

		// Act
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> macroProcessor.resolveContext("KEY", null));

		// Assert
		assertEquals("The parameter 'contextMap' cannot be null.", exception.getMessage());
	}


	@Test
	void resolveContext_mismatched_type() {

		String keyPath = "SOME_NAME";
		Duration duration  = Duration.ofMillis(2000);

		ContextMap contextMap = new ContextMap(playerMock);

		contextMap.put(keyPath, duration);

		MacroProcessor macroProcessor = new OfflinePlayerProcessor();

		ResultMap resultMap = macroProcessor.resolveContext(keyPath, contextMap);

		assertFalse(resultMap.containsKey(keyPath));
	}

}
