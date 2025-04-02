/*
 * Copyright (c) 2022-2025 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.pipeline.processor;

import com.winterhavenmc.util.messagebuilder.pipeline.ContextMap;
import com.winterhavenmc.util.messagebuilder.pipeline.processors.MacroProcessor;
import com.winterhavenmc.util.messagebuilder.pipeline.processors.ResultMap;
import com.winterhavenmc.util.messagebuilder.pipeline.processors.StringProcessor;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.RecordKey;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;
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

	RecordKey macroKey = RecordKey.create("KEY").orElseThrow();

	@AfterEach
	public void tearDown() {
		playerMock = null;
	}


	@Test
	void testResolveContext_parameter_null_context_map() {
		MacroProcessor macroProcessor = new StringProcessor();
		ValidationException exception = assertThrows(ValidationException.class,
				() -> macroProcessor.resolveContext(macroKey, null));

		assertEquals("The parameter 'contextMap' cannot be null.", exception.getMessage());
	}


	@Test
	void resolveContext()
	{
		String stringObject = "some name";

		ContextMap contextMap = new ContextMap(playerMock, macroKey);

		contextMap.put(macroKey, stringObject);

		MacroProcessor macroProcessor = new StringProcessor();

		ResultMap resultMap = macroProcessor.resolveContext(macroKey, contextMap);

		assertTrue(resultMap.containsKey(macroKey.toString()));
		assertEquals(stringObject, resultMap.get(macroKey.toString()));
	}

	@Test
	void resolveContext_not_string() {

		Duration duration  = Duration.ofMillis(2000);

		ContextMap contextMap = new ContextMap(playerMock, macroKey);

		contextMap.put(macroKey, duration);

		MacroProcessor macroProcessor = new StringProcessor();

		ResultMap resultMap = macroProcessor.resolveContext(macroKey, contextMap);

		assertFalse(resultMap.containsKey(macroKey.toString()));
	}

}
