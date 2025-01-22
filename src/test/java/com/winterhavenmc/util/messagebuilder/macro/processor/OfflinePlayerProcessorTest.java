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
import com.winterhavenmc.util.messagebuilder.messages.Macro;
import com.winterhavenmc.util.messagebuilder.context.NamespaceKey;
import com.winterhavenmc.util.messagebuilder.resources.language.LanguageQueryHandler;
import com.winterhavenmc.util.messagebuilder.util.LocalizedException;
import com.winterhavenmc.util.messagebuilder.util.Namespace;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class OfflinePlayerProcessorTest {

	@Mock LanguageQueryHandler languageQueryHandlerMock;
	@Mock Player playerMock;
	@Mock OfflinePlayer offlinePlayerMock;

	@BeforeEach
	void setUp() {

	}

	@AfterEach
	public void tearDown() {
		languageQueryHandlerMock = null;
		offlinePlayerMock = null;
	}

	@Test
	void resolveContextTest() {
		// Arrange
		String namespacedKey = NamespaceKey.create(Macro.OWNER, Namespace.Domain.MACRO);
		MacroProcessor macroProcessor = new OfflinePlayerProcessor(languageQueryHandlerMock);
		ContextMap contextMap = new ContextMap(playerMock);

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(namespacedKey, contextMap, offlinePlayerMock);

		// Assert
		assertFalse(resultMap.isEmpty());
		assertTrue(resultMap.containsKey(namespacedKey));
	}


	@Test
	void resolveContext_with_null_key() {
		// Arrange
		MacroProcessor macroProcessor = new OfflinePlayerProcessor(languageQueryHandlerMock);
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
		MacroProcessor macroProcessor = new OfflinePlayerProcessor(languageQueryHandlerMock);
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
		MacroProcessor macroProcessor = new OfflinePlayerProcessor(languageQueryHandlerMock);
		String namespacedKey = NamespaceKey.create(Macro.OWNER, Namespace.Domain.MACRO);

		// Act
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> macroProcessor.resolveContext(namespacedKey, null, offlinePlayerMock));

		// Assert
		assertEquals("The parameter 'contextMap' cannot be null.", exception.getMessage());
	}

	@Test
	void resolveContext_with_null_value() {
		// Arrange
		MacroProcessor macroProcessor = new OfflinePlayerProcessor(languageQueryHandlerMock);
		String namespacedKey = NamespaceKey.create(Macro.OWNER, Namespace.Domain.MACRO);
		ContextMap contextMap = new ContextMap(playerMock);

		// Act
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> macroProcessor.resolveContext(namespacedKey, contextMap, null));

		// Assert
		assertEquals("The parameter 'value' cannot be null.", exception.getMessage());
	}

	@Test
	void resolveContext_with_value_wrong_type() {
		// Arrange
		MacroProcessor macroProcessor = new OfflinePlayerProcessor(languageQueryHandlerMock);
		String namespacedKey = NamespaceKey.create(Macro.OWNER, Namespace.Domain.MACRO);
		ContextMap contextMap = new ContextMap(playerMock);

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(namespacedKey, contextMap, new ItemStack(Material.STONE));

		// Assert
		assertTrue(resultMap.isEmpty());
	}

}
