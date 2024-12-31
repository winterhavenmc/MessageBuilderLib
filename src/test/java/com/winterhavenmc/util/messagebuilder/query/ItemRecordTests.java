/*
 * Copyright (c) 2024 Tim Savage.
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

package com.winterhavenmc.util.messagebuilder.query;

import com.winterhavenmc.util.messagebuilder.util.MockUtility;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.*;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


class ItemRecordTests {

	private final static String TEST_ITEM = "TEST_ITEM_1";

	private ConfigurationSection itemSection;

	@BeforeEach
	void setUp() {
		// create new yaml configuration
		FileConfiguration fileConfiguration = new YamlConfiguration();

		// create a real configuration object from a resource
		try {
			fileConfiguration = YamlConfiguration.loadConfiguration(MockUtility.getResourceFile("language/en-US.yml"));
		} catch (IllegalArgumentException | URISyntaxException e) {
			System.out.println("a problem was encountered while trying to load the test configuration from resource");
		}

		// get item section of configuration
		itemSection = fileConfiguration.getConfigurationSection("ITEMS");
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

//	@Nested
//	class GetTests {
//		@Test
//		void getTest() {
//			assertTrue(ItemRecord.get(TEST_ITEM, itemSection).isPresent());
//		}
//
//		@Test
//		void getTest_null_itemKey() {
//			IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
//					() -> ItemRecord.get(null, itemSection));
//			assertEquals("the itemKey parameter was null.", exception.getMessage());
//		}
//
//		@Test
//		void getTest_null_itemSection() {
//			IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
//					() -> ItemRecord.get(TEST_ITEM, null));
//			assertEquals("the itemSection parameter was null.", exception.getMessage());
//		}
//
//		@Test
//		void getTest_nonexistent_entry() {
//			assertTrue(ItemRecord.get("NONEXISTENT_ITEM", itemSection).isEmpty());
//		}
//	}

//	@Nested
//	class FetchItemNameTests {
//		@Test
//		void fetchItemNameTest() {
//			assertEquals(Optional.of("&aTest Item"), ItemRecord.fetchItemNameSingular(getEntry(TEST_ITEM, itemSection)));
//		}
//
//		@Test
//		void fetchItemNameTest_undefined_field() {
//			assertEquals(Optional.empty(), ItemRecord.fetchItemNameSingular(getEntry("UNDEFINED_ITEM_NAME", itemSection)));
//		}
//
//		@Test
//		void fetchItemTest_nonexistent() {
//			assertEquals(Optional.empty(), ItemRecord.fetchItemNameSingular(getEntry("NON_EXISTENT_ITEM", itemSection)));
//		}
//
//		@Test
//		void fetchItemTest_null_parameter() {
//			assertEquals(Optional.empty(), ItemRecord.fetchItemNameSingular(null));
//		}
//	}

//	@Nested
//	class FetchItemPluralNameTests {
//		@Test
//		void fetchItemNamePluralTest() {
//			assertEquals(Optional.of("&aTest Items"), ItemRecord.fetchItemNamePlural(getEntry(TEST_ITEM, itemSection)));
//		}
//
//		@Test
//		void fetchItemNamePluralTest_undefined_field() {
//			assertEquals(Optional.empty(), ItemRecord.fetchItemNamePlural(getEntry("UNDEFINED_ITEM_NAME_PLURAL", itemSection)));
//		}
//
//		@Test
//		void fetchItemNamePluralTest_nonexistent() {
//			assertEquals(Optional.empty(), ItemRecord.fetchItemNamePlural(getEntry("NON_EXISTENT_ITEM", itemSection)));
//		}
//
//		@Test
//		void fetchItemNamePluralTest_null_parameter() {
//			assertEquals(Optional.empty(), ItemRecord.fetchItemNamePlural(null));
//		}
//	}

//	@Nested
//	class FetchItemInventoryNameTests {
//		@Test
//		void fetchItemInventoryNameTest() {
//			assertEquals(Optional.of("&aInventory Item"), ItemRecord.fetchInventoryItemNameSingular(getEntry(TEST_ITEM, itemSection)));
//		}
//
//		@Test
//		void fetchItemInventoryNameTest_undefined_field() {
//			assertEquals(Optional.empty(), ItemRecord.fetchInventoryItemNameSingular(getEntry("UNDEFINED_INVENTORY_ITEM_NAME", itemSection)));
//		}
//
//		@Test
//		void fetchInventoryItemNameTest_nonexistent() {
//			assertEquals(Optional.empty(), ItemRecord.fetchInventoryItemNameSingular(getEntry("NON_EXISTENT_ITEM", itemSection)));
//		}
//
//		@Test
//		void fetchItemInventoryNameTest_null_parameter() {
//			assertEquals(Optional.empty(), ItemRecord.fetchInventoryItemNameSingular(null));
//		}
//	}

//	@Nested
//	class FetchItemLoreTests {
//		@Test
//		void fetchItemLoreTest() {
//			assertEquals(List.of("&etest1 lore line 1", "&etest1 lore line 2"), ItemRecord.fetchItemLore(getEntry(TEST_ITEM, itemSection)));
//		}
//
//		@Test
//		void fetchItemLoreTest_undefined_field() {
//			assertEquals(Collections.emptyList(), ItemRecord.fetchItemLore(getEntry("UNDEFINED_ITEM_LORE", itemSection)));
//		}
//
//		@Test
//		void fetchItemTest_nonexistent() {
//			assertEquals(Collections.emptyList(), ItemRecord.fetchItemLore(getEntry("NON_EXISTENT_ITEM", itemSection)));
//		}
//
//		@Test
//		void fetchItemLoreTest_null_parameter() {
//			assertEquals(Collections.emptyList(), ItemRecord.fetchItemLore(null));
//		}
//	}

//	@Nested
//	class NullParameterTests {
//		@Test
//		void getEntryTest_null_itemKey_parameter() {
//			IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
//					() -> ItemRecord.getEntry(null, itemSection));
//			assertEquals("the itemKey parameter was null.", exception.getMessage());
//		}
//
//		@Test
//		void getEntryTest_null_itemSection_parameter() {
//			IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
//					() -> ItemRecord.getEntry(TEST_ITEM, null));
//			assertEquals("the itemSection parameter was null.", exception.getMessage());
//		}
//	}

}
