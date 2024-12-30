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

import com.winterhavenmc.util.messagebuilder.macro.ContextContainer;
import com.winterhavenmc.util.messagebuilder.macro.ContextMap;
import com.winterhavenmc.util.messagebuilder.macro.Namespace;
import com.winterhavenmc.util.messagebuilder.macro.NamespaceKey;
import com.winterhavenmc.util.messagebuilder.query.QueryHandler;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ObjectProcessorTest {

	private QueryHandler mockQueryHandler;
	private MacroProcessor macroProcessor;


	@BeforeEach
	public void setUp() {
		mockQueryHandler = mock(QueryHandler.class, "MockQueryHandler");
		macroProcessor = new ObjectProcessor(mockQueryHandler);
	}

	@AfterEach
	public void tearDown() {
		mockQueryHandler = null;
		macroProcessor = null;
	}


	@Test
	void resolveContext_integer() {
		String keyPath = "SOME_INTEGER";
		Integer number = 42;

		ContextMap contextMap = new ContextMap();
		String nameSpacedKey = NamespaceKey.create(keyPath, Namespace.Category.MACRO);
		contextMap.put(nameSpacedKey, ContextContainer.of(number, ProcessorType.NUMBER));

		ResultMap resultMap = macroProcessor.resolveContext(nameSpacedKey, contextMap, number);
		assertTrue(resultMap.containsKey(nameSpacedKey));
		assertEquals("42", resultMap.get(nameSpacedKey));
	}

}
