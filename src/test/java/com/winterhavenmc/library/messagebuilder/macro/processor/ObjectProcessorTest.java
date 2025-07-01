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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class ObjectProcessorTest {

	@Mock Plugin pluginMock;
	@Mock LanguageHandler languageHandlerMock;
	Processor processor;


	@BeforeEach
	public void setUp() {
		languageHandlerMock = new YamlLanguageHandler(pluginMock);
		processor = new ObjectProcessor(languageHandlerMock);
	}


	@Test
	void execute_integer() {
		String key = "SOME_INTEGER";
		Integer number = 42;

		MacroObjectMap macroObjectMap = new MacroObjectMap();
		macroObjectMap.put(key, number);

		ResultMap resultMap = processor.execute(macroObjectMap, key, number);
		assertTrue(resultMap.containsKey("SOME_INTEGER"));
		assertEquals("42", resultMap.get("SOME_INTEGER"));
	}

}