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
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NumberProcessorTest {

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
		processor = new NumberProcessor(plugin, languageHandler);
	}

	@AfterAll
	public void tearDown() {
		// Stop the mock server
		MockBukkit.unmock();
	}


	@Test
	void doReplacements_integer() {
		String key = "SOME_INTEGER";
		Integer number = 42;

		MacroObjectMap macroObjectMap = new MacroObjectMap();
		macroObjectMap.put(key, number);

		ResultMap resultMap = processor.doReplacements(macroObjectMap, key, number);
		assertTrue(resultMap.containsKey("SOME_INTEGER"));
		assertEquals("42", resultMap.get("SOME_INTEGER"));
	}

	@Test
	void doReplacements_null_integer() {
		String key = "SOME_NULL_INTEGER";
		Integer number = null;

		MacroObjectMap macroObjectMap = new MacroObjectMap();
		macroObjectMap.put(key, number);

		ResultMap resultMap = processor.doReplacements(macroObjectMap, key, number);
		assertFalse(resultMap.containsKey("SOME_NULL_INTEGER"));
	}

	@Test
	void doReplacements_long() {
		String key = "SOME_LONG";
		Long number = 420L;

		MacroObjectMap macroObjectMap = new MacroObjectMap();
		macroObjectMap.put(key, number);

		ResultMap resultMap = processor.doReplacements(macroObjectMap, key, number);
		assertTrue(resultMap.containsKey("SOME_LONG"));
		assertEquals("420", resultMap.get("SOME_LONG"));
	}

	@Test
	void doReplacements_null_long() {
		String key = "SOME_NULL_LONG";
		Long number = null;

		MacroObjectMap macroObjectMap = new MacroObjectMap();
		macroObjectMap.put(key, number);

		ResultMap resultMap = processor.doReplacements(macroObjectMap, key, number);
		assertFalse(resultMap.containsKey("SOME_NULL_LONG"));
	}

	@Test
	void doReplacements_duration() {
		String key = "SOME_DURATION";
		Long number = 12000L;

		MacroObjectMap macroObjectMap = new MacroObjectMap();
		macroObjectMap.put(key, number);

		ResultMap resultMap = processor.doReplacements(macroObjectMap, key, number);
		assertTrue(resultMap.containsKey("SOME_DURATION"));
		assertEquals("12 seconds", resultMap.get("SOME_DURATION"));
	}

	@Test
	void doReplacements_duration_minutes() {
		String key = "SOME_DURATION_MINUTES";
		Long number = 61_000L;

		MacroObjectMap macroObjectMap = new MacroObjectMap();
		macroObjectMap.put(key, number);

		ResultMap resultMap = processor.doReplacements(macroObjectMap, key, number);
		assertTrue(resultMap.containsKey("SOME_DURATION_MINUTES"));
		assertEquals("1 minute", resultMap.get("SOME_DURATION_MINUTES"));
	}

}
