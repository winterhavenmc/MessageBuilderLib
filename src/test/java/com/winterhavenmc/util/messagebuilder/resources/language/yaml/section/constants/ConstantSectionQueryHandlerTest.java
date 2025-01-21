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

package com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.constants;

import com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlConfigurationSupplier;
import com.winterhavenmc.util.messagebuilder.util.MockUtility;

import org.bukkit.configuration.Configuration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


class ConstantSectionQueryHandlerTest {

	Configuration configuration;
	YamlConfigurationSupplier configurationSupplier;
	ConstantSectionQueryHandler queryHandler;

	@BeforeEach
	void setUp() {
		configuration = MockUtility.loadConfigurationFromResource("language/en-US.yml");
		configurationSupplier = new YamlConfigurationSupplier(configuration);
		queryHandler = new ConstantSectionQueryHandler(configurationSupplier);
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
				() -> new ConstantSectionQueryHandler(null));
		assertEquals("The configurationSupplier parameter was null.", exception.getMessage());
	}

	@Test
	void testConstructor_supplier_contains_null() {
		// Arrange
		configuration.set("CONSTANTS", null);
		YamlConfigurationSupplier supplier = new YamlConfigurationSupplier(configuration);

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() ->  new ConstantSectionQueryHandler(supplier));
		assertEquals("The constantSection returned by the configuration supplier was an invalid 'CONSTANTS' section.", exception.getMessage());
	}

	@Test
	void testGetSectionType() {
		assertEquals("CONSTANTS", queryHandler.getSectionType().name());
	}

	@Test
	void getPrimaryType() {
		assertEquals(String.class, queryHandler.getPrimaryType());
	}

	@Test
	void listHandledTypes() {
		assertEquals(List.of(String.class, List.class, Integer.class), queryHandler.listHandledTypes());
	}

	@Test
	void getString_keyPath_valid() {
		assertEquals(Optional.of("&aSpawn"), queryHandler.getString("SPAWN.DISPLAY_NAME"));
	}

	@Test
	void getString_keyPath_invalid() {
		assertEquals(Optional.empty(), queryHandler.getString("INVALID_PATH"));
	}

	@Test
	void getString_keyPath_null() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> queryHandler.getString(null));
		assertEquals("The keyPath parameter cannot be null.", exception.getMessage());
	}

	@Test
	void getStringList_keyPath_valid() {
		assertEquals(List.of("item 1", "item 2", "item 3"), queryHandler.getStringList("TEST_LIST"));
	}

	@Test
	void getStringList_keyPath_invalid() {
		assertEquals(Collections.emptyList(), queryHandler.getStringList("INVALID_PATH"));
	}

	@Test
	void getStringList_keyPath_null() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> queryHandler.getStringList(null));
		assertEquals("The keyPath parameter cannot be null.", exception.getMessage());
	}

	@Test
	void getInt_keyPath_valid() {
		assertEquals(42, queryHandler.getInt("TEST_INT"));
	}

	@Test
	void getInt_keyPath_invalid() {
		assertEquals(0, queryHandler.getInt("INVALID_PATH"));
	}

	@Test
	void getInt_keyPath_null() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> queryHandler.getInt(null));
		assertEquals("The keyPath parameter cannot be null.", exception.getMessage());
	}

}
