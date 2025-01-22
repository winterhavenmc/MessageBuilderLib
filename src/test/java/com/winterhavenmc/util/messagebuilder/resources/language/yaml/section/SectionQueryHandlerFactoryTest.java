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

package com.winterhavenmc.util.messagebuilder.resources.language.yaml.section;

import com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlConfigurationSupplier;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.Section;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.SectionQueryHandlerFactory;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.items.ItemSectionQueryHandler;
import org.bukkit.configuration.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.winterhavenmc.util.messagebuilder.util.MockUtility.loadConfigurationFromResource;
import static org.junit.jupiter.api.Assertions.*;

class SectionQueryHandlerFactoryTest {

	SectionQueryHandlerFactory queryHandlerFactory;

	@BeforeEach
	void setUp() {
		Configuration configuration = loadConfigurationFromResource("language/en-US.yml");
		YamlConfigurationSupplier configurationSupplier = new YamlConfigurationSupplier(configuration);

		queryHandlerFactory = new SectionQueryHandlerFactory(configurationSupplier);
	}

	@AfterEach
	void tearDown() {
		queryHandlerFactory = null;
	}

	@Test
	void testConstructor_parameter_null() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> new SectionQueryHandlerFactory(null));

		assertEquals("The parameter 'configurationSupplier' cannot be null.", exception.getMessage());
	}


	@Test
	void getQueryHandler() {
		ItemSectionQueryHandler queryHandler = (ItemSectionQueryHandler) queryHandlerFactory.getQueryHandler(Section.ITEMS);
		assertNotNull(queryHandler);
	}

	@Test
	void createSectionHandler() {

	}

	@Test
	void createSectionHandlerDynamically() {
	}
}