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

package com.winterhavenmc.util.messagebuilder.language.yaml;

import com.winterhavenmc.util.messagebuilder.language.LanguageQueryHandler;
import com.winterhavenmc.util.messagebuilder.language.yaml.section.Section;
import com.winterhavenmc.util.messagebuilder.language.yaml.section.messages.MessageSectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import com.winterhavenmc.util.messagebuilder.language.yaml.section.items.ItemSectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.language.yaml.section.items.ItemRecord;
import com.winterhavenmc.util.messagebuilder.language.yaml.section.messages.MessageRecord;

import org.bukkit.configuration.Configuration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.winterhavenmc.util.messagebuilder.util.MockUtility.loadConfigurationFromResource;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class YamlLanguageQueryHandlerTest {

	LanguageQueryHandler queryHandler;


	@BeforeEach
	void setUp() {
		Configuration configuration = loadConfigurationFromResource("language/en-US.yml");
		YamlConfigurationSupplier configurationSupplier = new YamlConfigurationSupplier(configuration);
		queryHandler = new YamlLanguageQueryHandler(configurationSupplier);
	}

	@AfterEach
	void tearDown() {
		queryHandler = null;
	}

	@Test
	void testConstructor_parameter_null() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> new YamlLanguageQueryHandler(null));

		assertEquals("The configuration parameter was null.", exception.getMessage());
	}


	@ParameterizedTest
	@EnumSource
	void getQueryHandlerTest(Section section) {
		assertEquals(section.getHandlerClass(), queryHandler.getQueryHandler(section).getClass());
	}


	@Test
	void getItemRecordTest() {
		// Arrange
		ItemSectionQueryHandler itemSectionQueryHandler = (ItemSectionQueryHandler) queryHandler.getQueryHandler(Section.ITEMS);
		assertNotNull(itemSectionQueryHandler);

		// Act
		Optional<ItemRecord> itemRecord = itemSectionQueryHandler.getRecord("TEST_ITEM_1");

		// Assert
		assertTrue(itemRecord.isPresent());
	}


	@Test
	void getItemRecordTest_null() {
		// Arrange
		ItemSectionQueryHandler itemSectionQueryHandler = (ItemSectionQueryHandler) queryHandler.getQueryHandler(Section.ITEMS);
		assertNotNull(itemSectionQueryHandler);

		// Act
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> itemSectionQueryHandler.getRecord(null));

		// Assert
		assertEquals("The keyPath parameter was null.", exception.getMessage());
	}

	@Test
	void getMessageRecordTest() {
		// Arrange
		MessageSectionQueryHandler messageSectionQueryHandler = (MessageSectionQueryHandler) queryHandler.getQueryHandler(Section.MESSAGES);
		assertNotNull(messageSectionQueryHandler);

		// Act
		Optional<MessageRecord> messageRecord = messageSectionQueryHandler.getRecord(MessageId.ENABLED_MESSAGE);

		// Assert
		assertTrue(messageRecord.isPresent());
	}

	@Test
	void getMessageRecordTest_null() {
		// Arrange
		MessageSectionQueryHandler messageSectionQueryHandler = (MessageSectionQueryHandler) queryHandler.getQueryHandler(Section.MESSAGES);
		assertNotNull(messageSectionQueryHandler);

		// Act
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> messageSectionQueryHandler.getRecord(null));

		// Assert
		assertEquals("The messageKey parameter cannot be null.", exception.getMessage());
	}

	@Test
	void testGetItemRecord() {
	}

	@Test
	void testGetMessageRecord() {
	}
}
