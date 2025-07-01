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

import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class WorldProcessorTest
{
	@Mock Plugin pluginMock;
	@Mock World worldMock;


	@Test
	void execute()
	{
		LanguageHandler languageHandler = new YamlLanguageHandler(pluginMock);
		Processor processor = new WorldProcessor(languageHandler);

		String key = "SOME_WORLD";
		String value = "some word";

		when(worldMock.getName()).thenReturn("test-world");

		MacroObjectMap macroObjectMap = new MacroObjectMap();
		macroObjectMap.put(key, worldMock);

		ResultMap resultMap = processor.execute(macroObjectMap, key, worldMock);
		assertTrue(resultMap.containsKey("SOME_WORLD"));
		assertEquals("test_world", resultMap.get("SOME_WORLD"));
	}


	@Test
	void execute_with_null_world()
	{
		LanguageHandler languageHandler = new YamlLanguageHandler(pluginMock);
		Processor processor = new WorldProcessor(languageHandler);

		String key = "SOME_WORLD";
		String value = "some word";

		MacroObjectMap macroObjectMap = new MacroObjectMap();
		macroObjectMap.put(key, null);

		ResultMap resultMap = processor.execute(macroObjectMap, key, null);
		assertTrue(resultMap.isEmpty());
	}

}
