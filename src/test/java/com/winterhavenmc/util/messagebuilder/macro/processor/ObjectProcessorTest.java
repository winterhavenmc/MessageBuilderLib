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

import com.winterhavenmc.util.messagebuilder.context.ContextContainer;
import com.winterhavenmc.util.messagebuilder.context.ContextMap;
import com.winterhavenmc.util.messagebuilder.context.NamespaceKey;
import com.winterhavenmc.util.messagebuilder.messages.Macro;
import com.winterhavenmc.util.messagebuilder.resources.language.LanguageQueryHandler;

import org.bukkit.entity.Player;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


@ExtendWith(MockitoExtension.class)
class ObjectProcessorTest {

	@Mock private LanguageQueryHandler queryHandlerMock;
	@Mock private Player playerMock;
	private MacroProcessor macroProcessor;


	@BeforeEach
	public void setUp() {

		macroProcessor = new ObjectProcessor(queryHandlerMock);
	}

	@AfterEach
	public void tearDown() {
		queryHandlerMock = null;
		macroProcessor = null;
	}


	@Test
	void resolveContext_integer() {
		String keyPath = "SOME_INTEGER";
		Integer number = 42;

		ContextMap contextMap = new ContextMap(playerMock);
		String nameSpacedKey = NamespaceKey.create(Macro.DURATION);
		contextMap.put(nameSpacedKey, ContextContainer.of(number, ProcessorType.NUMBER));

		ResultMap resultMap = macroProcessor.resolveContext(nameSpacedKey, contextMap, number);
		assertTrue(resultMap.containsKey(nameSpacedKey));
		assertEquals("42", resultMap.get(nameSpacedKey));
	}

}
