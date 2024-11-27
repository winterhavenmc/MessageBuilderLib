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
import com.winterhavenmc.util.messagebuilder.PluginMain;
import com.winterhavenmc.util.messagebuilder.YamlLanguageHandler;
import com.winterhavenmc.util.messagebuilder.macro.MacroObjectMap;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.*;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemStackProcessorTest {

	ServerMock server;
	PluginMain plugin;
	LanguageHandler languageHandler;
	Processor processor;


	@BeforeAll
	public void setUp() {
		// Start the mock server
		server = MockBukkit.mock();

		// start the mock plugin
		plugin = MockBukkit.load(PluginMain.class);

		languageHandler = new YamlLanguageHandler(plugin);
		processor = new ItemStackProcessor(plugin, languageHandler);
	}


	@AfterAll
	public void tearDown() {
		// Stop the mock server
		MockBukkit.unmock();
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
