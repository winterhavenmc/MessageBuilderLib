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
import com.winterhavenmc.util.messagebuilder.context.Source;
import com.winterhavenmc.util.messagebuilder.context.SourceKey;
import com.winterhavenmc.util.messagebuilder.messages.Macro;
import com.winterhavenmc.util.messagebuilder.util.LocalizedException;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
		String contextKey = SourceKey.create(Source.MACRO, Macro.OWNER.name());
		MacroProcessor macroProcessor = new OfflinePlayerProcessor();
		ContextMap contextMap = new ContextMap(playerMock);

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(contextKey, contextMap, offlinePlayerMock);

		// Assert
		assertFalse(resultMap.isEmpty());
		assertTrue(resultMap.containsKey(contextKey));
	}


	@Test
	void resolveContext_with_null_key() {
		// Arrange
		MacroProcessor macroProcessor = new OfflinePlayerProcessor();
		ContextMap contextMap = new ContextMap(playerMock);

		// Act
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> macroProcessor.resolveContext(null, contextMap, offlinePlayerMock));

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
				() -> macroProcessor.resolveContext("", contextMap, offlinePlayerMock));

		// Assert
		assertEquals("The parameter 'keyPath' cannot be empty.", exception.getMessage());
	}

	@Test
	void resolveContext_with_null_contextMap() {
		// Arrange
		MacroProcessor macroProcessor = new OfflinePlayerProcessor();
		String contextKey = SourceKey.create(Source.MACRO, Macro.OWNER.name());

		// Act
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> macroProcessor.resolveContext(contextKey, null, offlinePlayerMock));

		// Assert
		assertEquals("The parameter 'contextMap' cannot be null.", exception.getMessage());
	}

	@Test
	void resolveContext_with_null_value() {
		// Arrange
		MacroProcessor macroProcessor = new OfflinePlayerProcessor();
		String contextKey = SourceKey.create(Source.MACRO, Macro.OWNER.name());
		ContextMap contextMap = new ContextMap(playerMock);

		// Act
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> macroProcessor.resolveContext(contextKey, contextMap, null));

		// Assert
		assertEquals("The parameter 'value' cannot be null.", exception.getMessage());
	}

	@Test
	void resolveContext_with_value_wrong_type() {
		// Arrange
		MacroProcessor macroProcessor = new OfflinePlayerProcessor();
		String contextKey = SourceKey.create(Source.MACRO, Macro.OWNER.name());
		ContextMap contextMap = new ContextMap(playerMock);

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(contextKey, contextMap, new ItemStack(Material.STONE));

		// Assert
		assertTrue(resultMap.isEmpty());
	}

}
