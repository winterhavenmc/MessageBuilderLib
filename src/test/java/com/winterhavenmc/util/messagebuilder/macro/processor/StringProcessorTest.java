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

import com.winterhavenmc.util.messagebuilder.context.*;

import org.bukkit.entity.Player;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class StringProcessorTest {

	@Mock Player playerMock;


	@AfterEach
	public void tearDown() {
		playerMock = null;
	}

	@Test
	void resolveContext() {

		String keyPath = "SOME_NAME";
		String stringObject = "some name";

		ContextMap contextMap = new ContextMap(playerMock);

		contextMap.put(keyPath, stringObject);

		MacroProcessor macroProcessor = new StringProcessor();

		ResultMap resultMap = macroProcessor.resolveContext(keyPath, contextMap);

		assertTrue(resultMap.containsKey(keyPath));
		assertEquals(stringObject, resultMap.get(keyPath));
	}

}
