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

import com.winterhavenmc.util.messagebuilder.macro.ContextContainer;
import com.winterhavenmc.util.messagebuilder.macro.ContextMap;
import com.winterhavenmc.util.messagebuilder.namespace.Namespace;
import com.winterhavenmc.util.messagebuilder.namespace.NamespaceKey;
import com.winterhavenmc.util.messagebuilder.language.LanguageQueryHandler;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class WorldProcessorTest {

	@Mock private Plugin pluginMock;
	@Mock private LanguageQueryHandler languageQueryHandlerMock;
	@Mock private World worldMock;
	@Mock private Player playerMock;


	@BeforeEach
	public void setUp() {
		// return logger for mock plugin
		when(pluginMock.getLogger()).thenReturn(Logger.getLogger("WorldProcessorTest"));

		// return name for mock world
		when(worldMock.getName()).thenReturn("test_world");

//		playerMock = mock(Player.class, "MockPlayer");
		when(playerMock.getName()).thenReturn("Player One");
		when(playerMock.getUniqueId()).thenReturn(new UUID(0,1));
	}

	@AfterEach
	public void tearDown() {
		pluginMock = null;
		worldMock = null;
	}

	@Disabled
	@Test
	void resolveContext() {
		String keyPath = "SOME_WORLD";
		String nameSpacedKey = NamespaceKey.create(keyPath, Namespace.Domain.MACRO);
		ContextMap contextMap = new ContextMap(playerMock);
		MacroProcessor macroProcessor = new WorldProcessor(languageQueryHandlerMock);
		contextMap.put(nameSpacedKey, ContextContainer.of(worldMock, ProcessorType.WORLD));
		ResultMap resultMap = macroProcessor.resolveContext(nameSpacedKey, contextMap, keyPath);

		assertTrue(resultMap.containsKey(nameSpacedKey));
		assertEquals("test_world", resultMap.get(nameSpacedKey));
	}

	@Disabled
	@Test
	void resolveContext_with_null_world() {
		String keyPath = "SOME_WORLD";
		String nameSpacedKey = NamespaceKey.create(keyPath, Namespace.Domain.MACRO);
		ContextMap contextMap = new ContextMap(playerMock);
		MacroProcessor macroProcessor = new WorldProcessor(languageQueryHandlerMock);
		contextMap.put(nameSpacedKey, ContextContainer.of(worldMock, ProcessorType.WORLD));
		ResultMap resultMap = macroProcessor.resolveContext(nameSpacedKey, contextMap, keyPath);

		assertTrue(resultMap.containsKey(nameSpacedKey));
		assertEquals("test_world", resultMap.get(nameSpacedKey));
		assertTrue(resultMap.isEmpty());
	}

}
