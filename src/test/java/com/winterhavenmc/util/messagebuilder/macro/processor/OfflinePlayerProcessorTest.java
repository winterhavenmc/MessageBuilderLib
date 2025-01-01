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
import com.winterhavenmc.util.messagebuilder.query.QueryHandler;

import com.winterhavenmc.util.messagebuilder.util.MockUtility;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.*;

import java.util.UUID;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OfflinePlayerProcessorTest {

	private Plugin mockPlugin;
	private QueryHandler mockQueryHandler;
	private OfflinePlayer mockOfflinePlayer;

	@BeforeEach
	void setUp() {
		// mock plugin
		mockPlugin = MockUtility.createMockPlugin();

		mockQueryHandler = mock(QueryHandler.class, "MockQueryHandler");

		// mock player for message recipient
		mockOfflinePlayer = mock(Player.class, "MockPlayer");
		when(mockOfflinePlayer.getName()).thenReturn("Player One");
		when(mockOfflinePlayer.getUniqueId()).thenReturn(new UUID(1,1));
	}

	@AfterEach
	public void tearDown() {
		mockPlugin = null;
		mockQueryHandler = null;
		mockOfflinePlayer = null;
	}

	@Disabled
	@Test
	void resolveContextTest() {
		String stringKey = "SOME_NAME";
		String key = NamespaceKey.create(Macro.OWNER, Namespace.Domain.MACRO);
		MacroProcessor macroProcessor = new StringProcessor(mockQueryHandler);
		ContextMap contextMap = new ContextMap();
		ResultMap resultMap = macroProcessor.resolveContext(key, contextMap, stringKey);
		assertTrue(resultMap.containsKey(key), "No match");
	}

	@Test
	void resolveContext_with_null_contextMap() {
		//TODO: pretty sure this is going to throw IllegalArgumentException
		String keyPath = "SOME_NAME";
		MacroProcessor macroProcessor = new StringProcessor(mockQueryHandler);
		String namespacedKey = NamespaceKey.create(Macro.OWNER, Namespace.Domain.MACRO);
		ResultMap resultMap = macroProcessor.resolveContext(namespacedKey, null, keyPath);
		assertFalse(resultMap.containsKey(namespacedKey));
	}

}
