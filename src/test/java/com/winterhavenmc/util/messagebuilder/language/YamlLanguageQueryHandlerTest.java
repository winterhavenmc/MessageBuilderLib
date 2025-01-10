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

package com.winterhavenmc.util.messagebuilder.language;

import com.winterhavenmc.util.messagebuilder.language.section.Section;
import com.winterhavenmc.util.messagebuilder.language.section.messages.MessageQueryHandler;
import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import com.winterhavenmc.util.messagebuilder.language.section.items.ItemQueryHandler;
import com.winterhavenmc.util.messagebuilder.language.section.items.ItemRecord;
import com.winterhavenmc.util.messagebuilder.language.section.messages.MessageRecord;

import org.bukkit.configuration.Configuration;

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
		Configuration languageConfig = loadConfigurationFromResource("language/en-US.yml");
		queryHandler = new YamlLanguageQueryHandler(languageConfig);
	}


	@ParameterizedTest
	@EnumSource
	void getQueryHandlerTest(Section section) {
		assertEquals(section.getHandlerClass(), queryHandler.getQueryHandler(section).getClass());
	}


	@Test
	void getItemRecordTest() {
		// Arrange
		ItemQueryHandler itemQueryHandler = (ItemQueryHandler) queryHandler.getQueryHandler(Section.ITEMS);
		assertNotNull(itemQueryHandler);

		// Act
		Optional<ItemRecord> itemRecord = itemQueryHandler.getRecord("TEST_ITEM_1");

		// Assert
		assertTrue(itemRecord.isPresent());
	}


	@Test
	void getItemRecordTest_null() {
		// Arrange
		ItemQueryHandler itemQueryHandler = (ItemQueryHandler) queryHandler.getQueryHandler(Section.ITEMS);
		assertNotNull(itemQueryHandler);

		// Act
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> itemQueryHandler.getRecord(null));

		// Assert
		assertEquals("The itemKey parameter was null.", exception.getMessage());
	}

	@Test
	void getMessageRecordTest() {
		// Arrange
		MessageQueryHandler messageQueryHandler = (MessageQueryHandler) queryHandler.getQueryHandler(Section.MESSAGES);
		assertNotNull(messageQueryHandler);

		// Act
		Optional<MessageRecord> messageRecord = messageQueryHandler.getRecord(MessageId.ENABLED_MESSAGE);

		// Assert
		assertTrue(messageRecord.isPresent());
	}

	@Test
	void getMessageRecordTest_null() {
		// Arrange
		MessageQueryHandler messageQueryHandler = (MessageQueryHandler) queryHandler.getQueryHandler(Section.MESSAGES);
		assertNotNull(messageQueryHandler);

		// Act
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() ->messageQueryHandler.getRecord(null));

		// Assert
		assertEquals("The messageKey parameter cannot be null.", exception.getMessage());
	}

}
