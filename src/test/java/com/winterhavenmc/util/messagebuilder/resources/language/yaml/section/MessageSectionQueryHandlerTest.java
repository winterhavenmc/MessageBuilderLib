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

import com.winterhavenmc.util.messagebuilder.resources.QueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.RecordKey;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlConfigurationSupplier;
import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import com.winterhavenmc.util.messagebuilder.util.MockUtility;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class MessageSectionQueryHandlerTest
{
	ConfigurationSection section;
	YamlConfigurationSupplier configurationSupplier;
	QueryHandler<MessageRecord> queryHandler;
	Configuration configuration;


	@BeforeEach
	void setUp()
	{
		configuration = MockUtility.loadConfigurationFromResource("language/en-US.yml");
		configurationSupplier = new YamlConfigurationSupplier(configuration);
		section = configuration.getConfigurationSection(Section.MESSAGES.name());
		queryHandler = new MessageSectionQueryHandler(configurationSupplier);
	}


	@Test
	void testConstructor_parameter_null()
	{
		// Arrange & Act
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> new MessageSectionQueryHandler(null));

		// Assert
		assertEquals("The parameter 'configurationSupplier' cannot be null.", exception.getMessage());
	}


	@Test
	@Disabled
	void testConstructor_supplier_contains_null()
	{
		// Arrange
		configuration.set("MESSAGES", null);
		YamlConfigurationSupplier supplier = new YamlConfigurationSupplier(configuration);

		// Act
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() ->  new MessageSectionQueryHandler(supplier));

		// Assert
		assertEquals("The configuration section returned by the configuration supplier was an invalid 'MESSAGES' section.", exception.getMessage());
	}


	@Test
	void testGetRecord_parameter_valid()
	{
		// Arrange
		RecordKey recordKey = RecordKey.of(MessageId.ENABLED_MESSAGE).orElseThrow();

		// Act
		ValidMessageRecord messageRecord = (ValidMessageRecord) queryHandler.getRecord(recordKey);

		// Assert
		assertInstanceOf(ValidMessageRecord.class, messageRecord);
	}


	@Test
	void testGetRecord_nonexistent_entry()
	{
		// Arrange
		RecordKey messageKey = RecordKey.of(MessageId.NONEXISTENT_ENTRY).orElseThrow();

		// Act
		MessageRecord messageRecord = queryHandler.getRecord(messageKey);

		// Assert
		assertInstanceOf(InvalidMessageRecord.class, messageRecord);
	}

}
