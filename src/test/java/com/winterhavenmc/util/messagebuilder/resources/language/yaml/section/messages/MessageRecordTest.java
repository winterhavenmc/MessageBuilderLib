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

import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import com.winterhavenmc.util.messagebuilder.util.LocalizedException;
import com.winterhavenmc.util.messagebuilder.util.MockUtility;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.winterhavenmc.util.messagebuilder.messages.MessageId.*;
import static org.junit.jupiter.api.Assertions.*;


class MessageRecordTest {

	private Configuration configuration;
	private ConfigurationSection messageSection;

	@BeforeEach
	void setUp() {
		// create real configuration from resource
		configuration = MockUtility.loadConfigurationFromResource("language/en-US.yml");

		// get messages section of configuration
		messageSection = configuration.getConfigurationSection("MESSAGES");
	}

	@AfterEach
	void tearDown() {
		messageSection = null;
	}

	//TODO: Each test should have its own distinct test entries in the language configuration resource
	// that are only used for that test, so changes to entries will not effect other tests

	@Test
	void constructorTest() {
		MessageRecord testRecord = new MessageRecord(
				ENABLED_MESSAGE.toString(),
				true,
				true,
				"this-is-a_string-key",
				List.of("list", "of", "arguments"),
				"this is a test message",
				11,
				"this is a test title",
				22,
				33,
				44,
				"this is a test subtitle");
		assertNotNull(testRecord, "the newly created record is null.");
	}

	@Test
	void testGetRecord_parameter_valid() {
		Optional<MessageRecord> messageRecord = MessageRecord.getRecord(MessageId.ENABLED_MESSAGE, messageSection);
		assertTrue(messageRecord.isPresent());
	}

	@Test
	void testGetRecord_parameter_messageId_null() {
		LocalizedException exception = assertThrows(LocalizedException.class,
				() ->  MessageRecord.getRecord(null, messageSection));

		assertEquals("The parameter 'messageId' cannot be null.", exception.getMessage());
	}

	@Test
	void testGetRecord_parameter_keyPath_invalid() {
		Optional<MessageRecord> messageRecord = MessageRecord.getRecord(NONEXISTENT_ENTRY, messageSection);
		assertTrue(messageRecord.isEmpty());
	}

	@Test
	void testGetRecord_parameter_itemSection_null() {
		LocalizedException exception = assertThrows(LocalizedException.class,
				() ->  MessageRecord.getRecord(ENABLED_MESSAGE, null));

		assertEquals("The parameter 'messageSection' cannot be null.", exception.getMessage());
	}

	@Test
	void testGetRecord_parameter_itemSection_invalid() {
		// Arrange
		messageSection = configuration.getConfigurationSection("ITEMS");

		// Act
		LocalizedException exception = assertThrows(LocalizedException.class,
				() ->  MessageRecord.getRecord(ENABLED_MESSAGE, messageSection));

		// Assert
		assertEquals("The configuration section returned by the configuration supplier was an invalid 'MESSAGES' section.", exception.getMessage());
	}

}
