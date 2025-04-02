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
import com.winterhavenmc.util.messagebuilder.messages.MessageId;

import com.winterhavenmc.util.messagebuilder.pipeline.processors.MacroProcessor;
import com.winterhavenmc.util.messagebuilder.pipeline.processors.ObjectProcessor;
import com.winterhavenmc.util.messagebuilder.pipeline.processors.ResultMap;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.RecordKey;
import org.bukkit.entity.Player;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class ObjectProcessorTest {

	@Mock Player playerMock;
	MacroProcessor macroProcessor;

	RecordKey recordKey = RecordKey.create(MessageId.ENABLED_MESSAGE).orElseThrow();


	@BeforeEach
	public void setUp() {
		macroProcessor = new ObjectProcessor();
	}

	@AfterEach
	public void tearDown() {
		macroProcessor = null;
	}


	@Test
	void resolveContext_integer() {
		// Arrange
		Integer value = 42;
		ContextMap contextMap = new ContextMap(playerMock, recordKey);
		contextMap.put(recordKey, value);

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(recordKey, contextMap);

		// Assert
		assertTrue(resultMap.containsKey(recordKey.toString()));
		assertEquals("42", resultMap.get(recordKey.toString()));
	}


}
