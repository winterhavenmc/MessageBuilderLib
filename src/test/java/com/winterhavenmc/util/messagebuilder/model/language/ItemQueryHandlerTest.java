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

package com.winterhavenmc.util.messagebuilder.model.language;

import com.winterhavenmc.util.messagebuilder.keys.RecordKey;
import com.winterhavenmc.util.messagebuilder.model.language.item.InvalidItemRecord;
import com.winterhavenmc.util.messagebuilder.model.language.item.ItemRecord;
import com.winterhavenmc.util.messagebuilder.model.language.item.ValidItemRecord;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.SectionProvider;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;

import org.bukkit.configuration.ConfigurationSection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.winterhavenmc.util.messagebuilder.messages.MessageId.NONEXISTENT_ENTRY;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ItemQueryHandlerTest
{
	@Mock ConfigurationSection itemSectionMock;
	@Mock ConfigurationSection itemEntryMock;


	@Test
	void testConstructor_parameter_null()
	{
		// Arrange & Act
		ValidationException exception = assertThrows(ValidationException.class,
				() -> new ItemQueryHandler(null));

		// Assert
		assertEquals("The parameter 'sectionSupplier' cannot be null.", exception.getMessage());
	}


	@Test
	void testGetRecord_parameter_valid()
	{
		// Arrange
		RecordKey recordKey = RecordKey.of("TEST_ITEM_1").orElseThrow();

		when(itemSectionMock.getConfigurationSection(recordKey.toString())).thenReturn(itemEntryMock);

		when(itemEntryMock.getString(ItemRecord.Field.NAME_SINGULAR.toKey())).thenReturn("Item Name singular");
		when(itemEntryMock.getString(ItemRecord.Field.NAME_PLURAL.toKey())).thenReturn("Item Name plural");
		when(itemEntryMock.getString(ItemRecord.Field.INVENTORY_NAME_SINGULAR.toKey())).thenReturn("Inventory Item Name singular");
		when(itemEntryMock.getString(ItemRecord.Field.INVENTORY_NAME_PLURAL.toKey())).thenReturn("Inventory Item Name plural");
		when(itemEntryMock.getStringList(ItemRecord.Field.LORE.toKey())).thenReturn(List.of("lore line 1", "lore line 2"));

		SectionProvider mockProvider = () -> itemSectionMock;
		ItemQueryHandler handler = new ItemQueryHandler(mockProvider);
		// Act
		ValidItemRecord result = (ValidItemRecord) handler.getRecord(recordKey);

		// Assert
		assertInstanceOf(ValidItemRecord.class, result);
		assertEquals("Item Name singular", result.nameSingular());
	}


	@Test
	void testGetRecord_non_existent_entry()
	{
		// Arrange
		RecordKey recordKey = RecordKey.of(NONEXISTENT_ENTRY).orElseThrow();
		when(itemSectionMock.getConfigurationSection(NONEXISTENT_ENTRY.name())).thenReturn(null);

		SectionProvider mockProvider = () -> itemSectionMock;
		ItemQueryHandler handler = new ItemQueryHandler(mockProvider);

		// Act
		ItemRecord itemRecord = handler.getRecord(recordKey);

		// Assert
		assertInstanceOf(InvalidItemRecord.class, itemRecord);
	}

}
