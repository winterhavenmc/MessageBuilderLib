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

import com.winterhavenmc.util.messagebuilder.util.LocalizedException;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class StringProcessorTest {

	@Mock Player playerMock;


	@AfterEach
	public void tearDown() {
		playerMock = null;
	}


	@Test
	void testResolveContext_parameter_null_key() {
		ContextMap contextMap = new ContextMap(playerMock);
		MacroProcessor macroProcessor = new StringProcessor();
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> macroProcessor.resolveContext(null, contextMap));

		assertEquals("The parameter 'key' cannot be null.", exception.getMessage());
	}


	@Test
	void testResolveContext_parameter_empty_key() {
		ContextMap contextMap = new ContextMap(playerMock);
		MacroProcessor macroProcessor = new StringProcessor();
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> macroProcessor.resolveContext("", contextMap));

		assertEquals("The parameter 'key' cannot be empty.", exception.getMessage());
	}


	@Test
	void testResolveContext_parameter_null_context_map() {
		MacroProcessor macroProcessor = new StringProcessor();
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> macroProcessor.resolveContext("KEY", null));

		assertEquals("The parameter 'contextMap' cannot be null.", exception.getMessage());
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

	@Test
	void resolveContext_not_string() {

		String keyPath = "SOME_NAME";
		Duration duration  = Duration.ofMillis(2000);

		ContextMap contextMap = new ContextMap(playerMock);

		contextMap.put(keyPath, duration);

		MacroProcessor macroProcessor = new StringProcessor();

		ResultMap resultMap = macroProcessor.resolveContext(keyPath, contextMap);

		assertFalse(resultMap.containsKey(keyPath));
	}

}
