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


import com.winterhavenmc.util.messagebuilder.language.LanguageQueryHandler;

import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class CommandSenderProcessorTest {

	@Mock Plugin pluginMock;
	@Mock
	LanguageQueryHandler queryHandlerMock;

	MacroProcessor macroProcessor;

	@BeforeEach
	public void setUp() {
		macroProcessor = new CommandSenderProcessor(queryHandlerMock);
	}

	@AfterEach
	public void tearDown() {
		pluginMock = null;
		queryHandlerMock = null;
		macroProcessor = null;
	}


	@Disabled
	@Test
	void resolveContext() {

	}
}
