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

import com.winterhavenmc.util.messagebuilder.macro.*;
import com.winterhavenmc.util.messagebuilder.messages.Macro;
import com.winterhavenmc.util.messagebuilder.namespace.NamespaceKey;
import com.winterhavenmc.util.messagebuilder.language.LanguageQueryHandler;

import com.winterhavenmc.util.messagebuilder.namespace.Namespace;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class OfflinePlayerProcessorTest {

	@Mock private LanguageQueryHandler languageQueryHandlerMock;
	@Mock private Player playerMock;

	@BeforeEach
	void setUp() {

	}

	@AfterEach
	public void tearDown() {
		languageQueryHandlerMock = null;
		playerMock = null;
	}

	@Test
	void resolveContextTest() {
		// Arrange
		String stringKey = "SOME_NAME";
		String key = NamespaceKey.create(Macro.OWNER, Namespace.Domain.MACRO);
		MacroProcessor macroProcessor = new StringProcessor(languageQueryHandlerMock);
		ContextMap contextMap = new ContextMap(playerMock);

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(key, contextMap, stringKey);

		// Assert
		assertTrue(resultMap.containsKey(key), "No match");
	}

	@Test
	void resolveContext_with_null_contextMap() {
		// Arrange
		String keyPath = "SOME_NAME";
		MacroProcessor macroProcessor = new StringProcessor(languageQueryHandlerMock);
		String namespacedKey = NamespaceKey.create(Macro.OWNER, Namespace.Domain.MACRO);

		// Act
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> macroProcessor.resolveContext(namespacedKey, null, keyPath));

		// Assert
		assertEquals("The contextMap cannot be null.", exception.getMessage());
	}

}
