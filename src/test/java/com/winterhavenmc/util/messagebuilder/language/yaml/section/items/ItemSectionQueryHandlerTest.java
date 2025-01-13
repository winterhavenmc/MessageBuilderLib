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

package com.winterhavenmc.util.messagebuilder.language.yaml.section.items;

import com.winterhavenmc.util.messagebuilder.language.yaml.YamlConfigurationSupplier;
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


	@BeforeEach
	void setUp() {
		Configuration configuration = MockUtility.loadConfigurationFromResource("language/en-US.yml");
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
		assertEquals("The itemSection parameter cannot be null.", exception.getMessage());
	}

	@Test
	void TestGetSectionType() {
		assertEquals("ITEMS", queryHandler.getSectionType().name());
	}

	@Test
	void testGetHandledType() {
		assertEquals(ItemRecord.class, queryHandler.getHandledType());
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
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> queryHandler.getRecord(null));
		assertEquals("The keyPath parameter was null.", exception.getMessage());
	}

	@Test
	void testGetRecord_parameter_invalid() {
		Optional<ItemRecord> itemRecord = queryHandler.getRecord("NONEXISTENT_ITEM");
		assertFalse(itemRecord.isPresent());
	}

}
