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

}
