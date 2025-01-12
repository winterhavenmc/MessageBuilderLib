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

import com.winterhavenmc.util.messagebuilder.language.LanguageResourceHandler;
import com.winterhavenmc.util.messagebuilder.macro.processor.ProcessorType;
import com.winterhavenmc.util.messagebuilder.language.LanguageQueryHandler;
import com.winterhavenmc.util.messagebuilder.language.yaml.YamlLanguageQueryHandler;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class MacroHandlerTest {

	private Plugin plugin;
	private Player player;
	private LanguageResourceHandler languageResourceHandler;

	private MacroHandler macroHandler;

	@BeforeEach
	public void setUp() {

		plugin = mock(Plugin.class, "MockPlugin");
		player = mock(Player.class, "MockPlayer");
		when(player.getUniqueId()).thenReturn(new UUID(1, 1));
		when(player.getName()).thenReturn("Player One");

		languageResourceHandler = mock(LanguageResourceHandler.class, "MockLanguageHandler");

		// real objects
		LanguageQueryHandler queryHandler = new YamlLanguageQueryHandler(languageResourceHandler.getConfiguration());
		macroHandler = new MacroHandler(queryHandler);
	}

	@AfterEach
	public void tearDown() {
		plugin = null;
		player = null;
		languageResourceHandler = null;
		macroHandler = null;
	}

	@Nested
	class DelimiterTests {
		@Test
		void setDelimiterTest_LEFT() {
			MacroHandler.MacroDelimiter.LEFT.set('L');
			assertEquals('L', MacroHandler.MacroDelimiter.LEFT.toChar());
		}

		@Test
		void setDelimiterTest_RIGHT() {
			MacroHandler.MacroDelimiter.RIGHT.set('R');
			assertEquals('R', MacroHandler.MacroDelimiter.RIGHT.toChar());
		}
	}

	@Test
	void replaceMacrosTest() {
		ContextMap contextMap = new ContextMap(player);

		String resultString = macroHandler.replaceMacros(player, contextMap, "Replace this: %ITEM_NAME%");
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
