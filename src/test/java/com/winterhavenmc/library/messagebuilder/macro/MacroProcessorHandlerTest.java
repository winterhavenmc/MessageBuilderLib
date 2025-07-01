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

package com.winterhavenmc.library.messagebuilder.macro;

import com.winterhavenmc.library.messagebuilder.LanguageHandler;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class MacroProcessorHandlerTest {

	@Mock Plugin pluginMock;
	@Mock ConsoleCommandSender consoleCommandSenderMock;
	@Mock LanguageHandler languageHandlerMock;
	private MacroProcessorHandler macroProcessorHandler;

	@BeforeEach
	public void setUp() {
		macroProcessorHandler = new MacroProcessorHandler(languageHandlerMock);
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
		String resultString = macroProcessorHandler.replaceMacros(consoleCommandSenderMock, macroObjectMap, "Replace this: %ITEM_NAME%");
		assertEquals("Replace this: §aTest Item", resultString);
	}

	@Test
	void replaceMacrosTest_item_already_in_map() {
		MacroObjectMap macroObjectMap = new MacroObjectMap();
		macroObjectMap.put("ITEM_NAME", "item_name");
		String resultString = macroProcessorHandler.replaceMacros(consoleCommandSenderMock, macroObjectMap, "Replace this: %ITEM_NAME%");
		assertEquals("Replace this: §aTest Item", resultString);
	}

	@Test
	void replaceMacrosTest_item_no_delimiter() {
		MacroObjectMap macroObjectMap = new MacroObjectMap();
		String resultString = macroProcessorHandler.replaceMacros(consoleCommandSenderMock, macroObjectMap, "Replace this: ITEM_NAME");
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
