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

package com.winterhavenmc.library.messagebuilder.query;

import com.winterhavenmc.library.messagebuilder.keys.InvalidItemKey;
import com.winterhavenmc.library.messagebuilder.keys.InvalidKeyReason;
import com.winterhavenmc.library.messagebuilder.keys.ItemKey;
import com.winterhavenmc.library.messagebuilder.keys.ValidItemKey;
import com.winterhavenmc.library.messagebuilder.model.language.InvalidItemRecord;
import com.winterhavenmc.library.messagebuilder.model.language.InvalidRecordReason;
import com.winterhavenmc.library.messagebuilder.model.language.ItemRecord;
import com.winterhavenmc.library.messagebuilder.model.language.ValidItemRecord;
import com.winterhavenmc.library.messagebuilder.resources.language.SectionProvider;
import com.winterhavenmc.library.messagebuilder.validation.ValidationException;

import org.bukkit.configuration.ConfigurationSection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ItemQueryHandlerTest
{
	@Mock ConfigurationSection itemSectionMock;
	@Mock ConfigurationSection itemEntryMock;


	@Test
	void constructor_with_null_parameter_throws_ValidationException()
	{
		// Arrange & Act
		ValidationException exception = assertThrows(ValidationException.class,
				() -> new ItemQueryHandler(null));

		// Assert
		assertEquals("The parameter 'sectionSupplier' cannot be null.", exception.getMessage());
	}


	@Test
	void getRecord_with_valid_parameter_returns_ValidItemRecord()
	{
		// Arrange
		ValidItemKey recordKey = ItemKey.of("TEST_ITEM_1").isValid().orElseThrow();

		when(itemSectionMock.getConfigurationSection(recordKey.toString())).thenReturn(itemEntryMock);

		when(itemEntryMock.getString(ItemRecord.Field.MATERIAL.toKey())).thenReturn("GOLDEN_PICKAXE");
		when(itemEntryMock.getString(ItemRecord.Field.NAME_SINGULAR.toKey())).thenReturn("Item Name singular");
		when(itemEntryMock.getString(ItemRecord.Field.NAME_PLURAL.toKey())).thenReturn("Item Name plural");
		when(itemEntryMock.getString(ItemRecord.Field.INVENTORY_NAME.toKey())).thenReturn("Inventory Item Name singular");
		when(itemEntryMock.getStringList(ItemRecord.Field.LORE.toKey())).thenReturn(List.of("lore line 1", "lore line 2"));

		SectionProvider mockProvider = () -> itemSectionMock;
		ItemQueryHandler handler = new ItemQueryHandler(mockProvider);

		// Act
		ValidItemRecord result = (ValidItemRecord) handler.getRecord(recordKey);

		// Assert
		assertInstanceOf(ValidItemRecord.class, result);
		assertEquals("Item Name singular", result.nameSingular());

		// Verify
		verify(itemSectionMock, atLeastOnce()).getConfigurationSection("TEST_ITEM_1");
		verify(itemEntryMock, times(4)).getString(anyString());
	}


	@Test
	void getRecord_with_non_existent_entry_returns_InvalidItemRecord()
	{
		// Arrange
		ValidItemKey recordKey = ItemKey.of("NONEXISTENT_ENTRY").isValid().orElseThrow();

		SectionProvider mockProvider = () -> itemSectionMock;
		ItemQueryHandler handler = new ItemQueryHandler(mockProvider);

		// Act
		ItemRecord itemRecord = handler.getRecord(recordKey);

		// Assert
		assertInstanceOf(InvalidItemRecord.class, itemRecord);

		// Verify
		verify(itemSectionMock, atLeastOnce()).getConfigurationSection("NONEXISTENT_ENTRY");
	}


	@Test
	void getRecord_with_invalid_key_returns_InvalidItemRecord()
	{
		// Arrange
		InvalidItemKey invalidItemKey = new InvalidItemKey("invalid-key", InvalidKeyReason.KEY_INVALID);
		when(itemSectionMock.getConfigurationSection(invalidItemKey.toString())).thenReturn(null);

		SectionProvider mockProvider = () -> itemSectionMock;
		ItemQueryHandler handler = new ItemQueryHandler(mockProvider);

		// Act
		ItemRecord itemRecord = handler.getRecord(invalidItemKey);

		// Assert
		assertInstanceOf(InvalidItemRecord.class, itemRecord);
		assertEquals(InvalidRecordReason.ITEM_KEY_INVALID, ((InvalidItemRecord) itemRecord).reason());

		// Verify
		verify(itemSectionMock, atLeastOnce()).getConfigurationSection(invalidItemKey.toString());
	}

}
