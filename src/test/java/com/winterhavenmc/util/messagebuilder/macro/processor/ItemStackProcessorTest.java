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

import com.winterhavenmc.util.messagebuilder.context.ContextMap;
import com.winterhavenmc.util.messagebuilder.util.LocalizedException;

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


	@AfterEach
	public void tearDown() {
		playerMock = null;
	}


	@Test
	void testResolveContext_parameter_null_key() {
		ContextMap contextMap = new ContextMap(playerMock);
		MacroProcessor macroProcessor = new ItemStackProcessor();
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> macroProcessor.resolveContext(null, contextMap));

		assertEquals("The parameter 'key' cannot be null.", exception.getMessage());
	}


	@Test
	void testResolveContext_parameter_empty_key() {
		ContextMap contextMap = new ContextMap(playerMock);
		MacroProcessor macroProcessor = new ItemStackProcessor();
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> macroProcessor.resolveContext("", contextMap));

		assertEquals("The parameter 'key' cannot be empty.", exception.getMessage());
	}


	@Test
	void testResolveContext_parameter_null_context_map() {
		MacroProcessor macroProcessor = new ItemStackProcessor();
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> macroProcessor.resolveContext("KEY", null));

		assertEquals("The parameter 'contextMap' cannot be null.", exception.getMessage());
	}


	@Test
	void resolveContext_no_metadata() {
		// Arrange
		String keyPath = "ITEM_STACK";
		ItemStack itemStack = new ItemStack(Material.GOLDEN_AXE);

		ContextMap contextMap = new ContextMap(playerMock);
		contextMap.put(keyPath, itemStack);
		MacroProcessor macroProcessor = new ItemStackProcessor();

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(keyPath, contextMap);

		// Assert
		assertTrue(resultMap.containsKey(keyPath));
		assertEquals("GOLDEN_AXE", resultMap.get(keyPath));
	}


	@Test
	void resolveContext_not_an_itemstack() {
		// Arrange
		String keyPath = "ITEM_STACK";
		int value = 42;
		ContextMap contextMap = new ContextMap(playerMock);
		contextMap.put(keyPath, value);
		MacroProcessor macroProcessor = new ItemStackProcessor();

		// Act
		ResultMap resultMap = macroProcessor.resolveContext(keyPath, contextMap);

		// Assert
		assertFalse(resultMap.containsKey(keyPath));
	}




//	@Disabled
//	@Test
//	void execute_with_item_meta() {
//
//		ItemStack itemStack = new ItemStack(Material.STONE);
//		ProcessorType processorType = ProcessorType.matchType(itemStack);
//		String stringKey = "SOME_ITEM";
//
//		ItemMeta itemMeta = itemStack.getItemMeta();
//		itemMeta.setDisplayName("Some Item Display Name");
//		itemStack.setItemMeta(itemMeta);
//
//		ContextMap contextMap = new ContextMap();
//		ContextKey compositeKey = new CompositeKey(processorType, stringKey);
//		contextMap.put(compositeKey, itemStack);
//
//		ResultMap resultMap = macroProcessor.execute(stringKey, itemStack, contextMap);
//
//		assertFalse(resultMap.isEmpty());
//		assertTrue(resultMap.containsKey("SOME_ITEM"));
//		assertEquals("Some Item Display Name", resultMap.get("SOME_ITEM"));
//	}

//	@Disabled
//	@Test
//	void execute_with_item_material() {
//		String key = "SOME_ITEM";
//
//		ItemStack itemStack = new ItemStack(Material.STONE);
//
//		ContextMap contextMap = new ContextMap();
//
//		MacroObjectMap macroObjectMap = new MacroObjectMap();
//		macroObjectMap.put(key, itemStack);
//
//		ResultMap resultMap = macroProcessor.execute(key, itemStack, contextMap);
//
//		assertFalse(resultMap.isEmpty());
//		assertTrue(resultMap.containsKey("SOME_ITEM"));
//		assertEquals("STONE", resultMap.get("SOME_ITEM"));
//	}

}
