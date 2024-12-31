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
import com.winterhavenmc.util.messagebuilder.macro.Namespace;
import com.winterhavenmc.util.messagebuilder.macro.NamespaceKey;
import com.winterhavenmc.util.messagebuilder.query.QueryHandler;

import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.*;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WorldProcessorTest {

	Plugin mockPlugin;
	QueryHandler mockQueryHandler;
	World mockWorld;

	@BeforeEach
	public void setUp() {
		mockPlugin = mock(Plugin.class, "MockPlugin");
		when(mockPlugin.getLogger()).thenReturn(Logger.getLogger("WorldProcessorTest"));
		mockQueryHandler = mock(QueryHandler.class, "MockQueryHandler");
		mockWorld = mock(World.class, "MockWorld");
		when(mockWorld.getName()).thenReturn("test_world");
	}

	@AfterEach
	public void tearDown() {
		mockPlugin = null;
		mockWorld = null;
	}

	@Disabled
	@Test
	void resolveContext() {
		String keyPath = "SOME_WORLD";
		String nameSpacedKey = NamespaceKey.create(keyPath, Namespace.Domain.MACRO);
		ContextMap contextMap = new ContextMap();
		MacroProcessor macroProcessor = new WorldProcessor(mockQueryHandler);
		contextMap.put(nameSpacedKey, ContextContainer.of(mockWorld, ProcessorType.WORLD));
		ResultMap resultMap = macroProcessor.resolveContext(nameSpacedKey, contextMap, keyPath);

		assertTrue(resultMap.containsKey(nameSpacedKey));
		assertEquals("test_world", resultMap.get(nameSpacedKey));
	}

	@Test
	void resolveContext_with_null_world() {
		String keyPath = "SOME_WORLD";
		String nameSpacedKey = NamespaceKey.create(keyPath, Namespace.Domain.MACRO);
		ContextMap contextMap = new ContextMap();
		MacroProcessor macroProcessor = new WorldProcessor(mockQueryHandler);
		contextMap.put(nameSpacedKey, ContextContainer.of(mockWorld, ProcessorType.WORLD));
		ResultMap resultMap = macroProcessor.resolveContext(nameSpacedKey, contextMap, keyPath);

		assertTrue(resultMap.containsKey(nameSpacedKey));
		assertEquals("test_world", resultMap.get(nameSpacedKey));
		assertTrue(resultMap.isEmpty());
	}

}
