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

package com.winterhavenmc.library.messagebuilder.macro.processor;

import com.winterhavenmc.library.messagebuilder.LanguageHandler;
import com.winterhavenmc.library.messagebuilder.YamlLanguageHandler;
import com.winterhavenmc.library.messagebuilder.macro.MacroObjectMap;

import org.bukkit.plugin.Plugin;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class StringProcessorTest
{
	@Mock Plugin pluginMock;
	@Mock LanguageHandler languageHandlerMock;


	@Test
	void execute()
	{
		LanguageHandler languageHandler = new YamlLanguageHandler(pluginMock);
		Processor processor = new StringProcessor(languageHandler);

		String key = "SOME_NAME";
		String value = "some name";

		MacroObjectMap macroObjectMap = new MacroObjectMap();
		macroObjectMap.put(key, value);

		ResultMap stringMap = processor.execute(macroObjectMap, "SOME_NAME", "some name");

		assertTrue(stringMap.containsKey("SOME_NAME"));
		assertEquals(value, stringMap.get(key));
	}


	@Test
	void executeWithItem() {

		LanguageHandler languageHandler = new YamlLanguageHandler(pluginMock);
		Processor processor = new StringProcessor(languageHandler);

		String key = "ITEM";
		String value = "some item string";

		MacroObjectMap macroObjectMap = new MacroObjectMap();
		macroObjectMap.put(key, value);

		ResultMap stringMap = processor.execute(macroObjectMap, "ITEM", "some item string");

		assertTrue(stringMap.containsKey(key));
		assertEquals("§aTest Item", stringMap.get(key));
		assertTrue(stringMap.containsKey("ITEM_NAME"));
		assertEquals("§aTest Item", stringMap.get("ITEM_NAME"));
	}

}
