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

import com.winterhavenmc.util.messagebuilder.keys.RecordKey;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlConfigurationSupplier;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;
import com.winterhavenmc.util.messagebuilder.util.MockUtility;

import org.bukkit.configuration.Configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class ItemSectionQueryHandlerTest
{
	YamlConfigurationSupplier configurationSupplier;
	ItemSectionQueryHandler queryHandler;
	Configuration configuration;


	@BeforeEach
	void setUp()
	{
		configuration = MockUtility.loadConfigurationFromResource("language/en-US.yml");
		configurationSupplier = new YamlConfigurationSupplier(configuration);
		queryHandler = new ItemSectionQueryHandler(configurationSupplier);
	}


	@Test
	void testConstructor_parameter_null()
	{
		// Arrange & Act
		ValidationException exception = assertThrows(ValidationException.class,
				() -> new ItemSectionQueryHandler(null));

		// Assert
		assertEquals("The parameter 'configurationSupplier' cannot be null.", exception.getMessage());
	}


	@Test
	void testGetRecord_parameter_valid()
	{
		// Arrange
		RecordKey recordKey = RecordKey.of("TEST_ITEM_1").orElseThrow();

		// Act
		SectionRecord itemRecord = queryHandler.getRecord(recordKey);

		// Assert
		assertInstanceOf(ValidItemRecord.class, itemRecord);
	}


	@Test
	void testGetRecord_non_existent_entry()
	{
		// Arrange
		RecordKey recordKey = RecordKey.of("NON_EXISTENT_ENTRY").orElseThrow();

		// Act
		SectionRecord sectionRecord = queryHandler.getRecord(recordKey);

		// Assert
		assertInstanceOf(InvalidItemRecord.class, sectionRecord);
	}

}
