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

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;

import com.winterhavenmc.util.messagebuilder.LanguageHandler;
import com.winterhavenmc.util.messagebuilder.PluginMain;
import com.winterhavenmc.util.messagebuilder.YamlLanguageHandler;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MacroProcessorHandlerTest {

	private final Plugin mockPlugin = mock(Plugin.class);
	ServerMock server;
	PluginMain plugin;
	LanguageHandler languageHandler;
	private MacroProcessorHandler macroProcessorHandler;

	@BeforeEach
	public void setUp() {
		// Start the mock server
		server = MockBukkit.mock();

		// start the mock plugin
		plugin = MockBukkit.load(PluginMain.class);

		languageHandler = new YamlLanguageHandler(plugin);
		macroProcessorHandler = new MacroProcessorHandler(languageHandler);
	}

	@AfterEach
	public void tearDown() {
		// Stop the mock server
		MockBukkit.unmock();
	}

	@Nested
	class DelimiterTests {
		@Test
		void setDelimiterTest_LEFT() {
			MacroProcessorHandler.MacroDelimiter.LEFT.set('L');
			assertEquals('L', MacroProcessorHandler.MacroDelimiter.LEFT.toChar());
		}

		@Test
		void setDelimiterTest_RIGHT() {
			MacroProcessorHandler.MacroDelimiter.RIGHT.set('R');
			assertEquals('R', MacroProcessorHandler.MacroDelimiter.RIGHT.toChar());
		}
	}

	@Test
	void replaceMacrosTest() {
		MacroObjectMap macroObjectMap = new MacroObjectMap();
		String resultString = macroProcessorHandler.replaceMacros(server.getConsoleSender(), macroObjectMap, "Replace this: %ITEM_NAME%");
		assertEquals("Replace this: §aTest Item", resultString);
	}

	@Test
	void replaceMacrosTest_item_already_in_map() {
		MacroObjectMap macroObjectMap = new MacroObjectMap();
		macroObjectMap.put("ITEM_NAME", "item_name");
		String resultString = macroProcessorHandler.replaceMacros(server.getConsoleSender(), macroObjectMap, "Replace this: %ITEM_NAME%");
		assertEquals("Replace this: §aTest Item", resultString);
	}

	@Test
	void replaceMacrosTest_item_no_delimiter() {
		MacroObjectMap macroObjectMap = new MacroObjectMap();
		String resultString = macroProcessorHandler.replaceMacros(server.getConsoleSender(), macroObjectMap, "Replace this: ITEM_NAME");
		assertEquals("Replace this: ITEM_NAME", resultString);
	}

	@Test
	void replaceMacrosTest_recipient_is_entity() {

		Entity entity = mock(Entity.class);
		when(entity.getUniqueId()).thenReturn(new UUID(123,123));
		when(entity.getName()).thenReturn("player1");

		MacroObjectMap macroObjectMap = new MacroObjectMap();
		String resultString = macroProcessorHandler.replaceMacros(entity, macroObjectMap, "Replace this: %ITEM_NAME%");
		assertEquals("Replace this: §aTest Item", resultString);
	}

}
