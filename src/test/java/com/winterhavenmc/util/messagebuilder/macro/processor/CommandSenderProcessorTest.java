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
import be.seeseemelk.mockbukkit.entity.PlayerMock;

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
class CommandSenderProcessorTest {

	ServerMock server;
	PluginMain plugin;
	LanguageHandler languageHandler;
	Processor processor;


	@BeforeAll
	public void setUp() {
		// Start the mock server
		server = MockBukkit.mock();

		// start the mock plugin
		plugin = MockBukkit.load(PluginMain.class);

		languageHandler = new YamlLanguageHandler(plugin);
		processor = new CommandSenderProcessor(plugin, languageHandler);
	}

	@AfterAll
	public void tearDown() {
		// Stop the mock server
		MockBukkit.unmock();
	}


	@Test
	void execute() {
		String key = "SOME_SENDER";

		PlayerMock player = server.addPlayer("testy");

		MacroObjectMap macroObjectMap = new MacroObjectMap();
		macroObjectMap.put(key, player);

		ResultMap resultMap = processor.execute(macroObjectMap, key, player);
		assertTrue(resultMap.containsKey("SOME_SENDER"));
		assertEquals("testy", resultMap.get("SOME_SENDER"));
	}
}
