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

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import com.winterhavenmc.util.messagebuilder.LanguageHandler;
import com.winterhavenmc.util.messagebuilder.PluginMain;
import com.winterhavenmc.util.messagebuilder.YamlLanguageHandler;
import com.winterhavenmc.util.messagebuilder.macro.MacroObjectMap;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.bukkit.Bukkit.getWorld;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WorldProcessorTest {

	ServerMock server;
	PluginMain plugin;


	@BeforeAll
	public void setUp() {
		// Start the mock server
		server = MockBukkit.mock();

		// start the mock plugin
		plugin = MockBukkit.load(PluginMain.class);
	}

	@AfterAll
	public void tearDown() {
		// Stop the mock server
		MockBukkit.unmock();
	}


	@Test
	void execute() {

		LanguageHandler languageHandler = new YamlLanguageHandler(plugin);
		Processor processor = new WorldProcessor(plugin, languageHandler);

		String key = "SOME_WORLD";
		String value = "some word";

		WorldMock world = server.addSimpleWorld("test_world");

		MacroObjectMap macroObjectMap = new MacroObjectMap();
		macroObjectMap.put(key, world);

		ResultMap resultMap = processor.execute(macroObjectMap, key, world);
		assertTrue(resultMap.containsKey("SOME_WORLD"));
		assertEquals("test_world", resultMap.get("SOME_WORLD"));
	}

	@Test
	void execute_with_null_world() {

		LanguageHandler languageHandler = new YamlLanguageHandler(plugin);
		Processor processor = new WorldProcessor(plugin, languageHandler);

		String key = "SOME_WORLD";
		String value = "some word";

		MacroObjectMap macroObjectMap = new MacroObjectMap();
		macroObjectMap.put(key, null);

		ResultMap resultMap = processor.execute(macroObjectMap, key, null);
		assertTrue(resultMap.isEmpty());
	}

}
