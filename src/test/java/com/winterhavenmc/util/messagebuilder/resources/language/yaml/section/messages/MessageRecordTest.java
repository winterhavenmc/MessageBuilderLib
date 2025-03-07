/*
 * Copyright (c) 2024-2025 Tim Savage.
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

import com.winterhavenmc.util.messagebuilder.validation.ValidationException;
import com.winterhavenmc.util.messagebuilder.util.MockUtility;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Optional;

import static com.winterhavenmc.util.messagebuilder.messages.MessageId.ENABLED_MESSAGE;
import static com.winterhavenmc.util.messagebuilder.messages.MessageId.NONEXISTENT_ENTRY;
import static org.junit.jupiter.api.Assertions.*;


class MessageRecordTest {

	Configuration configuration;
	ConfigurationSection messageSection;

	@BeforeEach
	public void setUp() {
		// create real configuration from resource
		configuration = MockUtility.loadConfigurationFromResource("language/en-US.yml");

		// get messages section of configuration
		messageSection = configuration.getConfigurationSection("MESSAGES");
	}

	@AfterEach
	public void tearDown() {
		messageSection = null;
	}

	@Test
	void constructorTest() {
		MessageRecord testRecord = new MessageRecord(
				ENABLED_MESSAGE.name(),
				true,
				"this is a test message",
				Duration.ofSeconds(11),
				"this is a test title",
				22,
				33,
				44,
				"this is a test subtitle", "", "", "");
		assertNotNull(testRecord, "the newly created record is null.");
	}

	@Test
	void testGetRecord_parameter_valid() {
		Optional<MessageRecord> messageRecord = MessageRecord.getRecord(ENABLED_MESSAGE.name(), messageSection);
		assertTrue(messageRecord.isPresent());
	}

	@Test
	void testGetRecord_parameter_messageId_null() {
		ValidationException exception = assertThrows(ValidationException.class,
				() ->  MessageRecord.getRecord(null, messageSection));

		assertEquals("The parameter 'key' cannot be null.", exception.getMessage());
	}

	@Test
	void testGetRecord_nonexistent_entry() {
		Optional<MessageRecord> messageRecord = MessageRecord.getRecord(NONEXISTENT_ENTRY.name(), messageSection);
		assertTrue(messageRecord.isEmpty());
	}

	@Test
	void testGetRecord_parameter_messageSection_null() {
		ValidationException exception = assertThrows(ValidationException.class,
				() ->  MessageRecord.getRecord(ENABLED_MESSAGE.name(), null));

		assertEquals("The parameter 'messageSection' cannot be null.", exception.getMessage());
	}

	@Test
	void testGetRecord_parameter_messageSection_invalid() {
		// Arrange
		messageSection = configuration.getConfigurationSection("ITEMS");

		// Act
		ValidationException exception = assertThrows(ValidationException.class,
				() ->  MessageRecord.getRecord(ENABLED_MESSAGE.name(), messageSection));

		// Assert
		assertEquals("The parameter 'messageSection' was invalid.", exception.getMessage());
	}

}
