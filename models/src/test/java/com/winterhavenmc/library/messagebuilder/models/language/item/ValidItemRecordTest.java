/*
 * Copyright (c) 2024-2025 Tim Savage.
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

package com.winterhavenmc.library.messagebuilder.models.language.item;

import com.winterhavenmc.library.messagebuilder.models.keys.ItemKey;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidItemKey;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class ValidItemRecordTest
{
	ValidItemKey recordKey;
	ValidItemRecord testRecord;


	@BeforeEach
	void setUp()
	{
		// create record string
		recordKey = ItemKey.of("TEST_ITEM").isValid().orElseThrow();

		// create configuration section for item record entry
		ConfigurationSection itemEntry = new MemoryConfiguration();
		itemEntry.set(ItemRecord.Field.NAME.toKey(), "Test Item");
		itemEntry.set(ItemRecord.Field.PLURAL_NAME.toKey(), "Test Items");
		itemEntry.set(ItemRecord.Field.DISPLAY_NAME.toKey(), "Inventory Test Item");
		itemEntry.set(ItemRecord.Field.LORE.toKey(), List.of("Lore line 1", "Lore line 2"));
		itemEntry.set(ItemRecord.Field.MATERIAL.toKey(), "GOLDEN_PICKAXE");

		// create valid item record from record string, item configuration section
		testRecord = ValidItemRecord.create(recordKey, itemEntry);
	}


	@ParameterizedTest
	@EnumSource
	void testFields(ItemRecord.Field field)
	{
		String keyPath = field.toKey();
		assertTrue(keyPath.matches("[A-Za-z][A-Za-z\\d_.]*"));
	}


	@Test
	void testKey()
	{
		assertEquals("TEST_ITEM", testRecord.key().toString());
	}


	@Test
	void name()
	{
		assertEquals("Test Item", testRecord.name());
	}


	@Test
	void pluralName()
	{
		assertEquals("Test Items", testRecord.pluralName());
	}


	@Test
	void getMaterial()
	{
		assertEquals("GOLDEN_PICKAXE", testRecord.material());
	}


	@Test
	void getLore()
	{
		assertEquals(List.of("Lore line 1", "Lore line 2"), testRecord.lore());
	}
}
