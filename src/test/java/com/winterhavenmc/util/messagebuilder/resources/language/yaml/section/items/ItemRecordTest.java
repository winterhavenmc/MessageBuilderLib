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

package com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.items;

import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.Section;
import com.winterhavenmc.util.messagebuilder.util.MockUtility;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


class ItemRecordTest {

	private final static String TEST_ITEM = "TEST_ITEM_1";

	private ConfigurationSection itemSection;

	@BeforeEach
	void setUp() {
		// create real configuration from resource
		Configuration configuration = MockUtility.loadConfigurationFromResource("language/en-US.yml");

		// get item section of configuration
		itemSection = configuration.getConfigurationSection(Section.ITEMS.name());
	}

	@AfterEach
	void tearDown() {
		itemSection = null;
	}

	//TODO: Each test should have its own distinct test entries in the language configuration resource
	// that are only used for that test, so changes to entries will not effect other tests

	@Test
	void constructorTest() {

		ItemRecord testRecord = new ItemRecord(
				TEST_ITEM,
				Optional.of("Test Item"),
				Optional.of("Test Items"),
				Optional.of("Inventory Test Item"),
				Optional.of("Inventory Test Items"),
				List.of("Lore line 1", "Lore line 2"));

		assertNotNull(testRecord, "the newly created record is null.");
	}

	@Test
	void testGetRecord_parameter_valid() {
		Optional<ItemRecord> itemRecord = ItemRecord.getRecord("TEST_ITEM_1", itemSection);
		assertTrue(itemRecord.isPresent());
	}

	@Test
	void testGetRecord_parameter_keyPath_null() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() ->  ItemRecord.getRecord(null, itemSection));

		assertEquals("The keyPath parameter was null.", exception.getMessage());
	}

	@Test
	void testGetRecord_parameter_keyPath_invalid() {
		Optional<ItemRecord> itemRecord = ItemRecord.getRecord("INVALID_ITEM", itemSection);
		assertTrue(itemRecord.isEmpty());
	}

	@Test
	void testGetRecord_parameter_itemSection_null() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() ->  ItemRecord.getRecord("TEST_ITEM_1", null));

		assertEquals("The itemSection returned by the configuration supplier was an invalid 'ITEMS' section.", exception.getMessage());
	}

	@ParameterizedTest
	@EnumSource
	void testFields(ItemRecord.Field field) {
		String keyPath = field.getKeyPath();
		assertTrue(keyPath.matches("[A-Z0-9_.]+"));
	}

	@Test
	void testPluralized() {
		ItemRecord testRecord = new ItemRecord(
				TEST_ITEM,
				Optional.of("Test Item"),
				Optional.of("Test Items"),
				Optional.of("Inventory Test Item"),
				Optional.of("Inventory Test Items"),
				List.of("Lore line 1", "Lore line 2"));

		assertEquals(Optional.of("Test Items"), testRecord.getPluralized(0));
		assertEquals(Optional.of("Test Item"), testRecord.getPluralized(1));
		assertEquals(Optional.of("Test Items"), testRecord.getPluralized(2));
	}

}
