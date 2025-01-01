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


import com.winterhavenmc.util.messagebuilder.query.QueryHandler;

import com.winterhavenmc.util.messagebuilder.util.MockUtility;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.*;

import java.util.logging.Logger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommandSenderProcessorTest {

	Plugin mockPlugin;
	QueryHandler mockQueryHandler;
	MacroProcessor macroProcessor;

	@BeforeEach
	public void setUp() {
		mockPlugin = MockUtility.createMockPlugin();
		mockQueryHandler = mock(QueryHandler.class, "MockQueryHandler");

		macroProcessor = new CommandSenderProcessor(mockQueryHandler);
	}

	@AfterEach
	public void tearDown() {
		mockPlugin = null;
	}


	@Disabled
	@Test
	void resolveContext() {
//		String key = "SOME_SENDER";
//
//		PlayerMock player = serverMock.addPlayer("testy");
//
//		MacroObjectMap macroObjectMap = new MacroObjectMap();
//		macroObjectMap.put(key, player);
//
//		ResultMap resultMap = macroProcessor.execute(macroObjectMap, key, player);
//		assertTrue(resultMap.containsKey("SOME_SENDER"));
//		assertEquals("testy", resultMap.get("SOME_SENDER"));
	}
}
