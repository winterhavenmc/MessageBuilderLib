/*
 * Copyright (c) 2025 Tim Savage.
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
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.item.InvalidItemRecord;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.item.ItemRecord;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.item.ValidItemRecord;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class ItemRecordTest
{
	@Test
	void from_valid_section()
	{
		// Arrange
		RecordKey itemKey = RecordKey.of("TEST_ITEM_1").orElseThrow();

		ConfigurationSection itemEntrySection = new MemoryConfiguration();
		itemEntrySection.set(ItemRecord.Field.NAME_SINGULAR.getKeyPath(), "Item Name");
		itemEntrySection.set(ItemRecord.Field.NAME_PLURAL.getKeyPath(), "Item Names");
		itemEntrySection.set(ItemRecord.Field.INVENTORY_NAME_SINGULAR.getKeyPath(), "Inventory Item Name");
		itemEntrySection.set(ItemRecord.Field.INVENTORY_NAME_PLURAL.getKeyPath(), "Inventory Item Names");
		itemEntrySection.set(ItemRecord.Field.LORE.getKeyPath(), List.of("Lore Line 1", "Lore Line 2"));

		// Act
		ItemRecord testRecord = ItemRecord.from(itemKey, itemEntrySection);

		// Assert
		assertInstanceOf(ValidItemRecord.class, testRecord);
	}


	@Test
	void from_null_section()
	{
		// Arrange
		RecordKey itemKey = RecordKey.of("TEST_ITEM_1").orElseThrow();

		// Act
		ItemRecord testRecord = ItemRecord.from(itemKey, null);

		// Assert
		assertInstanceOf(InvalidItemRecord.class, testRecord);
	}

}
