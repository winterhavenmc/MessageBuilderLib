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

package com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.items;

import com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlConfigurationSupplier;
import com.winterhavenmc.util.messagebuilder.util.LocalizedException;
import com.winterhavenmc.util.messagebuilder.util.MockUtility;

import org.bukkit.configuration.Configuration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


class ItemSectionQueryHandlerTest {

	YamlConfigurationSupplier configurationSupplier;
	ItemSectionQueryHandler queryHandler;
	Configuration configuration;

	@BeforeEach
	void setUp() {
		configuration = MockUtility.loadConfigurationFromResource("language/en-US.yml");
		configurationSupplier = new YamlConfigurationSupplier(configuration);
		queryHandler = new ItemSectionQueryHandler(configurationSupplier);
	}

	@AfterEach
	void tearDown() {
		configurationSupplier = null;
		queryHandler = null;
	}


	@Test
	void testNotNull() {
		assertNotNull(queryHandler);
	}

	@Test
	void testConstructor_parameter_null() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> new ItemSectionQueryHandler(null));
		assertEquals("The parameter 'configurationSupplier' cannot be null.", exception.getMessage());
	}

	@Test
	void testConstructor_supplier_contains_null() {
		// Arrange
		configuration.set("ITEMS", null);
		YamlConfigurationSupplier supplier = new YamlConfigurationSupplier(configuration);

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() ->  new ItemSectionQueryHandler(supplier));
		assertEquals("The configuration section returned by the configuration supplier was an invalid 'ITEMS' section.", exception.getMessage());
	}

	@Test
	void TestGetSectionType() {
		assertEquals("ITEMS", queryHandler.getSectionType().name());
	}

	@Test
	void testGetPrimaryType() {
		assertEquals(ItemRecord.class, queryHandler.getPrimaryType());
	}

	@Test
	void testListHandledTypes() {
		assertEquals(List.of(ItemRecord.class), queryHandler.listHandledTypes());
	}

	@Test
	void testGetRecord_parameter_valid() {
		// Arrange & Act
		Optional<ItemRecord> itemRecord = queryHandler.getRecord("TEST_ITEM_1");

		// Assert
		assertTrue(itemRecord.isPresent());
		assertEquals(Optional.of("&aTest Item"), itemRecord.get().nameSingular());
	}

	@Test
	void testGetRecord_parameter_null() {
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> queryHandler.getRecord(null));
		assertEquals("The parameter 'keyPath' cannot be null.", exception.getMessage());
	}

	@Test
	void testGetRecord_parameter_invalid() {
		Optional<ItemRecord> itemRecord = queryHandler.getRecord("NONEXISTENT_ITEM");
		assertFalse(itemRecord.isPresent());
	}

}
