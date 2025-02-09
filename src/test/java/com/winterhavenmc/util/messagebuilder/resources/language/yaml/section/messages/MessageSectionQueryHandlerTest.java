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

package com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.messages;

import com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlConfigurationSupplier;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.Section;
import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import com.winterhavenmc.util.messagebuilder.util.MockUtility;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MessageSectionQueryHandlerTest {

	ConfigurationSection section;
	YamlConfigurationSupplier configurationSupplier;
	MessageSectionQueryHandler queryHandler;
	Configuration configuration;

	@BeforeEach
	void setUp() {
		configuration = MockUtility.loadConfigurationFromResource("language/en-US.yml");
		configurationSupplier = new YamlConfigurationSupplier(configuration);
		section = configuration.getConfigurationSection(Section.MESSAGES.name());
		queryHandler = new MessageSectionQueryHandler(configurationSupplier);
	}

	@AfterEach
	void tearDown() {
		section = null;
		queryHandler = null;
	}


	@Test
	void testConstructor_parameter_null() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> new MessageSectionQueryHandler(null));
		assertEquals("The parameter 'configurationSupplier' cannot be null.", exception.getMessage());
	}


	@Test
	@Disabled
	void testConstructor_supplier_contains_null() {
		// Arrange
		configuration.set("MESSAGES", null);
		YamlConfigurationSupplier supplier = new YamlConfigurationSupplier(configuration);

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() ->  new MessageSectionQueryHandler(supplier));
		assertEquals("The configuration section returned by the configuration supplier was an invalid 'MESSAGES' section.", exception.getMessage());
	}


	@Test
	void testGetRecord_parameter_valid() {
		// Arrange & Act
		Optional<MessageRecord> messageRecord = queryHandler.getRecord(MessageId.ENABLED_MESSAGE.name());

		// Assert
		assertTrue(messageRecord.isPresent());
		assertTrue(messageRecord.get().enabled());
	}

	@Test
	void testGetRecord_parameter_null() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> queryHandler.getRecord(null));
		assertEquals("The parameter 'key' cannot be null.", exception.getMessage());
	}

	@Test
	void testGetRecord_parameter_invalid() {
		Optional<MessageRecord> messageRecord = queryHandler.getRecord(MessageId.NONEXISTENT_ENTRY.name());
		assertFalse(messageRecord.isPresent());
	}

}
