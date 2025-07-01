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
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class OfflinePlayerProcessorTest {

	@Mock Plugin pluginMock;

//	@Disabled
//	@Test
//	void execute() {
//		LanguageHandler languageHandler = new YamlLanguageHandler(pluginMock);
//		Processor processor = new StringProcessor(languageHandler);
//
//		String key = "SOME_NAME";
//		OfflinePlayer offlinePlayer = server.getOfflinePlayer(UUID.randomUUID());
//		assertNotNull(offlinePlayer);
//
//		MacroObjectMap macroObjectMap = new MacroObjectMap();
//		macroObjectMap.put(key, offlinePlayer);
//
//		ResultMap resultMap = processor.execute(macroObjectMap, key, offlinePlayer);
//
//		assertTrue(resultMap.containsKey(key), "No match: " + key);
//	}

	@Test
	void execute_with_null_offlinePlayer() {
		LanguageHandler languageHandler = new YamlLanguageHandler(pluginMock);
		Processor processor = new StringProcessor(languageHandler);

		String key = "SOME_NAME";

		MacroObjectMap macroObjectMap = new MacroObjectMap();
		macroObjectMap.put(key, null);

		ResultMap resultMap = processor.execute(macroObjectMap, key, null);

		assertFalse(resultMap.containsKey("SOME_NAME"));
	}

}
