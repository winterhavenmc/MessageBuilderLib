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

import com.winterhavenmc.util.messagebuilder.resources.RecordKey;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlConfigurationSupplier;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;
import com.winterhavenmc.util.messagebuilder.util.MockUtility;

import org.bukkit.configuration.Configuration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
		ValidationException exception = assertThrows(ValidationException.class,
				() -> new ItemSectionQueryHandler(null));
		assertEquals("The parameter 'configurationSupplier' cannot be null.", exception.getMessage());
	}


	@Test
	void testGetRecord_parameter_valid() {
		// Arrange & Act
		RecordKey recordKey = RecordKey.of("TEST_ITEM_1").orElseThrow();
		ValidItemRecord itemRecord = (ValidItemRecord) queryHandler.getRecord(recordKey);

		// Assert
		assertNotNull(itemRecord);
		assertEquals("&aTest Item", itemRecord.nameSingular());
	}

//	@Test
//	void testGetRecord_parameter_invalid() {
//		RecordKey recordKey = RecordKey.of("NONEXISTENT_ITEM").orElseThrow();
//		Optional<ValidItemRecord> itemRecord = queryHandler.getRecord(recordKey);
//		assertFalse(itemRecord.isPresent());
//	}

}
