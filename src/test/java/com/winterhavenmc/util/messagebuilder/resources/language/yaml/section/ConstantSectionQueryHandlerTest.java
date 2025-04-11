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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


class ConstantSectionQueryHandlerTest
{
	Configuration configuration;
	YamlConfigurationSupplier configurationSupplier;
	ConstantSectionQueryHandler queryHandler;


	@BeforeEach
	void setUp()
	{
		configuration = MockUtility.loadConfigurationFromResource("language/en-US.yml");
		configurationSupplier = new YamlConfigurationSupplier(configuration);
		queryHandler = new ConstantSectionQueryHandler(configurationSupplier);
	}


	@Test
	void testConstructor_parameter_null()
	{
		// Arrange & Act
		ValidationException exception = assertThrows(ValidationException.class,
				() -> new ConstantSectionQueryHandler(null));

		// Assert
		assertEquals("The parameter 'configurationSupplier' cannot be null.", exception.getMessage());
	}


	@Test
	void getString_keyPath_valid()
	{
		// Arrange
		RecordKey recordKey = RecordKey.of("SPAWN.DISPLAY_NAME").orElseThrow();

		// Act
		Optional<String> result = queryHandler.getString(recordKey);

		// Assert
		assertEquals(Optional.of("&aSpawn"), result);
	}


	@Test
	void getString_keyPath_invalid()
	{
		// Arrange
		RecordKey recordKey = RecordKey.of("INVALID_PATH").orElseThrow();

		// Act
		Optional<String> result = queryHandler.getString(recordKey);

		// Assert
		assertEquals(Optional.empty(), result);
	}


	@Test
	void getStringList_keyPath_valid()
	{
		// Arrange
		RecordKey recordKey = RecordKey.of("TEST_LIST").orElseThrow();

		// Act
		List<String> result = queryHandler.getStringList(recordKey);

		// Assert
		assertEquals(List.of("item 1", "item 2", "item 3"), result);
	}


	@Test
	void getStringList_keyPath_invalid()
	{
		// Arrange
		RecordKey recordKey = RecordKey.of("INVALID_PATH").orElseThrow();

		// Act
		List<String> result = queryHandler.getStringList(recordKey);

		// Assert
		assertEquals(Collections.emptyList(), result);
	}


	@Test
	void getInt_keyPath_valid()
	{
		// Arrange
		RecordKey recordKey = RecordKey.of("TEST_INT").orElseThrow();

		// Act
		int result = queryHandler.getInt(recordKey);

		// Assert
		assertEquals(42, result);
	}


	@Test
	void getInt_keyPath_invalid()
	{
		// Arrange
		RecordKey recordKey = RecordKey.of("INVALID_PATH").orElseThrow();

		// Act
		int result = queryHandler.getInt(recordKey);

		// Assert
		assertEquals(0, result);
	}


	@Test
	void testGetRecord_key_invalid()
	{
		// Arrange
		RecordKey recordKey = RecordKey.of("INVALID_PATH").orElseThrow();

		// Act
		ConstantRecord constantRecord = queryHandler.getRecord(recordKey);

		// Assert
		assertInstanceOf(InvalidConstantRecord.class, constantRecord);
	}


	@Test
	void testGetRecord_valid()
	{
		// Arrange
		RecordKey recordKey = RecordKey.of("SPAWN.DISPLAY_NAME").orElseThrow();

		// Act
		ConstantRecord constantRecord = queryHandler.getRecord(recordKey);

		// Assert
		assertInstanceOf(ValidConstantRecord.class, constantRecord);
	}

}
