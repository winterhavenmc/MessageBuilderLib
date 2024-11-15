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
import com.winterhavenmc.util.messagebuilder.LanguageHandler;
import com.winterhavenmc.util.messagebuilder.PluginMain;
import com.winterhavenmc.util.messagebuilder.YamlLanguageHandler;
import com.winterhavenmc.util.messagebuilder.macro.MacroObjectMap;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OfflinePlayerProcessorTest {

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

//	@Test
//	void execute() {
//		LanguageHandler languageHandler = new YamlLanguageHandler(plugin);
//		Processor processor = new StringProcessor(plugin, languageHandler);
//
//		String key = "SOME_NAME";
//		OfflinePlayer offlinePlayer = server.getOfflinePlayer( UUID.fromString("test_player"));
//
//		MacroObjectMap macroObjectMap = new MacroObjectMap();
//		macroObjectMap.put(key, offlinePlayer);
//
//		ResultMap resultMap = processor.execute(macroObjectMap, key, offlinePlayer);
//
//		assertTrue(resultMap.containsKey("SOME_NAME"));
//	}

	@Test
	void execute_with_null_offlinePlayer() {
		LanguageHandler languageHandler = new YamlLanguageHandler(plugin);
		Processor processor = new StringProcessor(plugin, languageHandler);

		String key = "SOME_Name";

		MacroObjectMap macroObjectMap = new MacroObjectMap();
		macroObjectMap.put(key, null);

		ResultMap resultMap = processor.execute(macroObjectMap, key, null);

		assertFalse(resultMap.containsKey("SOME_NAME"));
	}

}
