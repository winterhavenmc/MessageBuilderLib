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

package com.winterhavenmc.util.messagebuilder.resources.language.yaml;

import com.winterhavenmc.util.messagebuilder.model.language.Section;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.winterhavenmc.util.messagebuilder.util.MockUtility.loadConfigurationFromResource;
import static org.junit.jupiter.api.Assertions.*;

class YamlConfigurationSupplierTest {

	YamlConfigurationSupplier configurationSupplier;


	@BeforeEach
	void setUp() {

		// get configuration from resource
		Configuration configuration = loadConfigurationFromResource("language/en-US.yml");

		// create configuration supplier
		configurationSupplier = new YamlConfigurationSupplier(configuration);


	}

	@AfterEach
	void tearDown() {
		configurationSupplier = null;
	}

	@Test
	void testGet() {
		// Arrange & Act
		Configuration configuration = configurationSupplier.get();

		// Assert
		assertNotNull(configuration);
		assertTrue(configuration.contains("CONSTANTS"));
	}

	@Test
	void testGetSection() {
		// Arrange & Act
		ConfigurationSection configurationSection = configurationSupplier.getSection(Section.ITEMS);

		// Assert
		assertNotNull(configurationSection);
		assertTrue(configurationSection.contains("TEST_ITEM_1"));
	}

	@Test
	void testGetSection_parameter_null() {
		// Arrange & Act
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> configurationSupplier.getSection(null));

		// Assert
		assertEquals("The parameter 'section' cannot be null.", exception.getMessage());
	}

}
