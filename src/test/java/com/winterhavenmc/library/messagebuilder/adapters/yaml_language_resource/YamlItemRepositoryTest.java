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

package com.winterhavenmc.library.messagebuilder.adapters.yaml_language_resource;

import com.winterhavenmc.library.messagebuilder.keys.ItemKey;
import com.winterhavenmc.library.messagebuilder.keys.ValidItemKey;
import com.winterhavenmc.library.messagebuilder.model.language.*;
import com.winterhavenmc.library.messagebuilder.resources.language.LanguageSectionProvider;

import com.winterhavenmc.library.messagebuilder.resources.language.SectionProvider;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;


class YamlItemRepositoryTest
{
	FileConfiguration languageConfig;
	SectionProvider languageSectionProvider;
	String configString = """
		ITEMS:
		  TEST_ITEM:
		    MATERIAL: NETHER_STAR
		    NAME: "Test Item"
		    PLURAL_NAME: "Test Items"
		    DISPLAY_NAME: "Lodestar | {DESTINATION}"
		    LORE:
		      - "test item lore line 1"
		      - "test item lore line 2"
		""";


	@BeforeEach
	void setUp() throws InvalidConfigurationException
	{
		languageConfig = new YamlConfiguration();
		languageConfig.loadFromString(configString);

		Supplier<Configuration> configurationSupplier = () -> languageConfig;
		languageSectionProvider = new LanguageSectionProvider(configurationSupplier, Section.ITEMS);
	}


	@Test
	void getItemRecord_returns_invalid_record_when_no_entry_for_key()
	{
		// Arrange
		ValidItemKey validItemKey = ItemKey.of("KEY").isValid().orElseThrow();

		// Act
		YamlItemRepository itemRepository = new YamlItemRepository(languageSectionProvider);
		ItemRecord result = itemRepository.getItemRecord(validItemKey);

		// Assert
		assertInstanceOf(InvalidItemRecord.class, result);
		assertEquals(InvalidRecordReason.ITEM_ENTRY_MISSING, ((InvalidItemRecord) result).reason());
	}


	@Test
	void getItemRecord_with_valid_parameter_returns_ValidItemRecord()
	{
		// Arrange
		ValidItemKey recordKey = ItemKey.of("TEST_ITEM").isValid().orElseThrow();

		// Act
		YamlItemRepository itemRepository = new YamlItemRepository(languageSectionProvider);
		ItemRecord result = itemRepository.getItemRecord(recordKey);

		// Assert
		assertInstanceOf(ValidItemRecord.class, result);
		assertEquals("Test Item", ((ValidItemRecord) result).name());
		assertEquals("Test Items", ((ValidItemRecord) result).pluralName());
		assertEquals("Lodestar | {DESTINATION}", ((ValidItemRecord) result).displayName());
	}

}
