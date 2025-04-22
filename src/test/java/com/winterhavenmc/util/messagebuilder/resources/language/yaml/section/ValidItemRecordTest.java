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

package com.winterhavenmc.util.messagebuilder.resources.language.yaml.section;

import com.winterhavenmc.util.messagebuilder.keys.RecordKey;

import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.item.ItemRecord;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.item.ValidItemRecord;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class ValidItemRecordTest
{
	RecordKey recordKey;
	ValidItemRecord testRecord;


	@BeforeEach
	void setUp()
	{
		// create record key
		recordKey = RecordKey.of("TEST_ITEM").orElseThrow();

		// create configuration section for item record entry
		ConfigurationSection itemEntry = new MemoryConfiguration();
		itemEntry.set(ItemRecord.Field.NAME_SINGULAR.getKeyPath(), "Test Item");
		itemEntry.set(ItemRecord.Field.NAME_PLURAL.getKeyPath(), "Test Items");
		itemEntry.set(ItemRecord.Field.INVENTORY_NAME_SINGULAR.getKeyPath(), "Inventory Test Item");
		itemEntry.set(ItemRecord.Field.INVENTORY_NAME_PLURAL.getKeyPath(), "Inventory Test Items");
		itemEntry.set(ItemRecord.Field.LORE.getKeyPath(), List.of("Lore line 1", "Lore line 2"));

		// create valid item record from record key, item configuration section
		testRecord = ValidItemRecord.create(recordKey, itemEntry);
	}


	@ParameterizedTest
	@EnumSource
	void testFields(ItemRecord.Field field)
	{
		String keyPath = field.getKeyPath();
		assertTrue(keyPath.matches("[A-Za-z][A-Za-z\\d_.]*"));
	}


	@Test
	public void testPluralized()
	{
		assertEquals("Test Items", testRecord.nameFor(0));
		assertEquals("Test Item", testRecord.nameFor(1));
		assertEquals("Test Items", testRecord.nameFor(2));
		assertEquals("Test Items", testRecord.nameFor(10));
	}

	@Test
	void testKey()
	{
		assertEquals("TEST_ITEM", testRecord.key().toString());
	}

	@Test
	void nameSingular()
	{
		assertEquals("Test Item", testRecord.nameSingular());
	}

	@Test
	void namePlural()
	{
		assertEquals("Test Items", testRecord.namePlural());
	}

}
