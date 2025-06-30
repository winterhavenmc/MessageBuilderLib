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

import com.winterhavenmc.util.messagebuilder.LanguageHandler;
import com.winterhavenmc.util.messagebuilder.YamlLanguageHandler;
import com.winterhavenmc.util.messagebuilder.macro.MacroObjectMap;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class ItemStackProcessorTest
{
	@Mock Plugin pluginMock;
	@Mock LanguageHandler languageHandlerMock;
	Processor processor;


	@BeforeEach
	public void setUp() {
		languageHandlerMock = new YamlLanguageHandler(pluginMock);
		processor = new ItemStackProcessor(languageHandlerMock);
	}


	@Disabled
	@Test
	void execute_with_item_meta() {

		String key = "SOME_ITEM";

		ItemStack itemStack = new ItemStack(Material.STONE);

		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName("Some Item Display Name");
		itemStack.setItemMeta(itemMeta);

		MacroObjectMap macroObjectMap = new MacroObjectMap();
		macroObjectMap.put(key, itemStack);

		ResultMap resultMap = processor.execute(macroObjectMap, key, itemStack);

		assertFalse(resultMap.isEmpty());
		assertTrue(resultMap.containsKey("SOME_ITEM"));
		assertEquals("Some Item Display Name", resultMap.get("SOME_ITEM"));
	}

	@Disabled
	@Test
	void execute_with_item_material() {
		String key = "SOME_ITEM";

		ItemStack itemStack = new ItemStack(Material.STONE);

		MacroObjectMap macroObjectMap = new MacroObjectMap();
		macroObjectMap.put(key, itemStack);

		ResultMap resultMap = processor.execute(macroObjectMap, key, itemStack);

		assertFalse(resultMap.isEmpty());
		assertTrue(resultMap.containsKey("SOME_ITEM"));
		assertEquals("STONE", resultMap.get("SOME_ITEM"));
	}

}
