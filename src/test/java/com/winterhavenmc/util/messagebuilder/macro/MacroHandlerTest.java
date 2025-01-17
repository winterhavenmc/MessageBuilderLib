/*
 * Copyright (c) 2024 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.macro;

import com.winterhavenmc.util.messagebuilder.context.ContextContainer;
import com.winterhavenmc.util.messagebuilder.context.ContextMap;
import com.winterhavenmc.util.messagebuilder.context.NamespaceKey;
import com.winterhavenmc.util.messagebuilder.macro.processor.ResultMap;
import com.winterhavenmc.util.messagebuilder.messages.Macro;
import com.winterhavenmc.util.messagebuilder.resources.language.LanguageResourceManager;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlConfigurationSupplier;
import com.winterhavenmc.util.messagebuilder.macro.processor.ProcessorType;
import com.winterhavenmc.util.messagebuilder.resources.language.LanguageQueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlLanguageQueryHandler;
import com.winterhavenmc.util.messagebuilder.util.MockUtility;

import com.winterhavenmc.util.messagebuilder.util.Namespace;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class MacroHandlerTest {

	private Player player;
	private LanguageResourceManager languageResourceManager;

	private MacroHandler macroHandler;

	@BeforeEach
	public void setUp() {

		player = mock(Player.class, "MockPlayer");
		when(player.getUniqueId()).thenReturn(new UUID(1, 1));
		when(player.getName()).thenReturn("Player One");

		languageResourceManager = mock(LanguageResourceManager.class, "MockLanguageHandler");

		// real objects
		Configuration configuration = MockUtility.loadConfigurationFromResource("language/en-US.yml");
		YamlConfigurationSupplier configurationSupplier = new YamlConfigurationSupplier(configuration);
		LanguageQueryHandler queryHandler = new YamlLanguageQueryHandler(configurationSupplier);
		macroHandler = new MacroHandler(queryHandler);
	}

	@AfterEach
	public void tearDown() {
		player = null;
		languageResourceManager = null;
		macroHandler = null;
	}

//	@Nested
//	class DelimiterTests {
//		@Test
//		void setDelimiterTest_LEFT() {
//			MacroHandler.MacroDelimiter.LEFT.set('L');
//			assertEquals('L', MacroHandler.MacroDelimiter.LEFT.toChar());
//		}
//
//		@Test
//		void setDelimiterTest_RIGHT() {
//			MacroHandler.MacroDelimiter.RIGHT.set('R');
//			assertEquals('R', MacroHandler.MacroDelimiter.RIGHT.toChar());
//		}
//	}

	@Test
	void replaceMacrosTest() {
		ResultMap resultMap = new ResultMap();
		resultMap.put("TEST_ITEM", "§aTest Item");

		String resultString = macroHandler.replaceMacros(player, resultMap, "Replace this: %ITEM_NAME%");
		assertEquals("Replace this: §aTest Item", resultString);
	}

	@Test
	void replaceMacrosTest_item_already_in_map() {
		ContextMap contextMap = new ContextMap(player);
		String key = "MACRO:My_Item";
		contextMap.put(key, ContextContainer.of("TEST_STRING", ProcessorType.STRING));

		String resultString = macroHandler.replaceMacros(player, contextMap, "Replace this: %ITEM_NAME%");
		assertEquals("Replace this: §aTest Item", resultString);
	}

	@Test
	void replaceMacrosTest_item_no_delimiter() {
		ContextMap contextMap = new ContextMap(player);
		String resultString = macroHandler.replaceMacros(player, contextMap, "Replace this: ITEM_NAME");
		assertEquals("Replace this: ITEM_NAME", resultString);
	}

	@Test
	void replaceMacrosTest_recipient_is_entity() {
		Entity entity = mock(Entity.class);
		when(entity.getUniqueId()).thenReturn(new UUID(123,123));
		when(entity.getName()).thenReturn("player1");

		ContextMap contextMap = new ContextMap(player);
		String resultString = macroHandler.replaceMacros(player, contextMap, "Replace this: %ITEM_NAME%");
		assertEquals("Replace this: §aTest Item", resultString);
	}

}
