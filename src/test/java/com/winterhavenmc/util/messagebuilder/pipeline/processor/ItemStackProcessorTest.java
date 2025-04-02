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

import com.winterhavenmc.util.messagebuilder.pipeline.processors.ItemStackProcessor;
import com.winterhavenmc.util.messagebuilder.pipeline.processors.MacroProcessor;
import com.winterhavenmc.util.messagebuilder.pipeline.processors.ResultMap;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.RecordKey;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class ItemStackProcessorTest {

	@Mock Player playerMock;

	RecordKey recordKey = RecordKey.create(MessageId.ENABLED_MESSAGE).orElseThrow();

	@AfterEach
	public void tearDown() {
		playerMock = null;
	}


	@Test
	void testResolveContext_parameter_null_context_map() {
		MacroProcessor macroProcessor = new ItemStackProcessor();
		ValidationException exception = assertThrows(ValidationException.class,
				() -> macroProcessor.resolveContext(recordKey, null));

		assertEquals("The parameter 'contextMap' cannot be null.", exception.getMessage());
	}


	@Test
	void resolveContext_no_metadata() {
		// Arrange
		ItemStack itemStack = new ItemStack(Material.GOLDEN_AXE);

		ContextMap contextMap = new ContextMap(playerMock, recordKey);
		contextMap.put(recordKey, itemStack);
		MacroProcessor macroProcessor = new ItemStackProcessor();

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(recordKey, contextMap);

		// Assert
		assertTrue(resultMap.containsKey(recordKey.toString()));
		assertEquals("GOLDEN_AXE", resultMap.get(recordKey.toString()));
	}


	@Test
	void resolveContext_not_an_itemstack() {
		// Arrange
		int value = 42;
		ContextMap contextMap = new ContextMap(playerMock, recordKey);
		contextMap.put(recordKey, value);
		MacroProcessor macroProcessor = new ItemStackProcessor();

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(recordKey, contextMap);

		// Assert
		assertFalse(resultMap.containsKey(recordKey.toString()));
	}

}
