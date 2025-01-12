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

package com.winterhavenmc.util.messagebuilder.language.yaml.section.messages;

import com.winterhavenmc.util.messagebuilder.language.yaml.section.Section;
import com.winterhavenmc.util.messagebuilder.language.yaml.section.messages.MessageSectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.language.yaml.section.messages.MessageRecord;
import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import com.winterhavenmc.util.messagebuilder.util.MockUtility;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MessageSectionQueryHandlerTest {

	ConfigurationSection section;
	MessageSectionQueryHandler queryHandler;
	FileConfiguration configuration;

	@BeforeEach
	void setUp() {
		configuration = MockUtility.loadConfigurationFromResource("language/en-US.yml");
		section = configuration.getConfigurationSection(Section.MESSAGES.name());
		queryHandler = new MessageSectionQueryHandler(section);
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
		assertEquals("The messageSection parameter cannot be null.", exception.getMessage());
	}

	@Test
	void testConstructor_parameter_invalid() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> new MessageSectionQueryHandler(configuration.getConfigurationSection(Section.CONSTANTS.name())));
		assertEquals("The messageSection parameter was an invalid 'MESSAGES' section.", exception.getMessage());
	}

	@Test
	void testGetRecord_parameter_valid() {
		// Arrange & Act
		Optional<MessageRecord> messageRecord = queryHandler.getRecord(MessageId.ENABLED_MESSAGE);

		// Assert
		assertTrue(messageRecord.isPresent());
		assertTrue(messageRecord.get().enabled());
	}

	@Test
	void testGetRecord_parameter_null() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> queryHandler.getRecord(null));
		assertEquals("The messageKey parameter cannot be null.", exception.getMessage());
	}

	@Test
	void testGetRecord_parameter_invalid() {
		Optional<MessageRecord> messageRecord = queryHandler.getRecord(MessageId.NONEXISTENT_ENTRY);
		assertFalse(messageRecord.isPresent());
	}

	@Test
	void getSectionType() {
		assertEquals(Section.MESSAGES, queryHandler.getSectionType());
	}

	@Test
	void getHandledType() {
		assertEquals(MessageRecord.class, queryHandler.getHandledType());
	}

	@Test
	void listHandledTypes() {
		assertEquals(List.of(MessageRecord.class), queryHandler.listHandledTypes());
	}

}