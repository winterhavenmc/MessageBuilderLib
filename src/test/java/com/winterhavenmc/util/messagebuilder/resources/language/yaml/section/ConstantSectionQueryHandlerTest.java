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


	@AfterEach
	void tearDown()
	{
		configurationSupplier = null;
		queryHandler = null;
	}


	@Test
	void testNotNull()
	{
		assertNotNull(queryHandler);
	}


	@Test
	void testConstructor_parameter_null()
	{
		ValidationException exception = assertThrows(ValidationException.class,
				() -> new ConstantSectionQueryHandler(null));
		assertEquals("The parameter 'configurationSupplier' cannot be null.", exception.getMessage());
	}


	@Test
	void getString_keyPath_valid()
	{
		RecordKey recordKey = RecordKey.of("SPAWN.DISPLAY_NAME").orElseThrow();
		assertEquals(Optional.of("&aSpawn"), queryHandler.getString(recordKey));
	}


	@Test
	void getString_keyPath_invalid()
	{
		RecordKey recordKey = RecordKey.of("INVALID_PATH").orElseThrow();
		assertEquals(Optional.empty(), queryHandler.getString(recordKey));
	}


	@Test
	void getStringList_keyPath_valid()
	{
		RecordKey recordKey = RecordKey.of("TEST_LIST").orElseThrow();
		assertEquals(List.of("item 1", "item 2", "item 3"), queryHandler.getStringList(recordKey));
	}


	@Test
	void getStringList_keyPath_invalid()
	{
		RecordKey recordKey = RecordKey.of("INVALID_PATH").orElseThrow();
		assertEquals(Collections.emptyList(), queryHandler.getStringList(recordKey));
	}


	@Test
	void getInt_keyPath_valid()
	{
		RecordKey recordKey = RecordKey.of("TEST_INT").orElseThrow();
		assertEquals(42, queryHandler.getInt(recordKey));
	}


	@Test
	void getInt_keyPath_invalid()
	{
		RecordKey recordKey = RecordKey.of("INVALID_PATH").orElseThrow();
		assertEquals(0, queryHandler.getInt(recordKey));
	}


//	@Test
//	void testGetRecord_key_invalid()
//	{
//		RecordKey recordKey = RecordKey.of("INVALID_PATH").orElseThrow();
//		assertEquals(Optional.empty(), queryHandler.getRecord(recordKey));
//	}


//	@Test
//	void testGetRecord()
//	{
//		RecordKey recordKey = RecordKey.of("SPAWN.DISPLAY_NAME").orElseThrow();
//		queryHandler.getRecord(recordKey).ifPresent(r ->
//				assertEquals("&aSpawn", r.obj())
//		);
//	}

}
