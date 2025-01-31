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

package com.winterhavenmc.util.messagebuilder.pipeline;

import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import com.winterhavenmc.util.messagebuilder.resources.language.LanguageQueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlConfigurationSupplier;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.Section;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.messages.MessageRecord;
import com.winterhavenmc.util.messagebuilder.util.LocalizedException;
import com.winterhavenmc.util.messagebuilder.util.MockUtility;

import org.bukkit.configuration.file.FileConfiguration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MessageRetrieverTest {

	@Mock LanguageQueryHandler languageQueryHandlerMock;

	@AfterEach
	void tearDown() {
		this.languageQueryHandlerMock = null;
	}


	@Test
	void getRecord() {
		Retriever retriever = new MessageRetriever();
		// Arrange
		FileConfiguration configuration = MockUtility.loadConfigurationFromResource("language/en-US.yml");
		YamlConfigurationSupplier configurationSupplier = new YamlConfigurationSupplier(configuration);
		when(languageQueryHandlerMock.getSectionQueryHandler(Section.MESSAGES)).thenReturn(Section.MESSAGES.getQueryHandler(configurationSupplier));

		// Act
		Optional<MessageRecord> messageRecord = retriever.getRecord(MessageId.ENABLED_MESSAGE.name(), languageQueryHandlerMock);

		// Assert
		assertNotNull(messageRecord);
		assertTrue(messageRecord.isPresent());

		// Verify
		verify(languageQueryHandlerMock, atLeastOnce()).getSectionQueryHandler(Section.MESSAGES);
	}

	@Test
	void getRecord_section_query_handler_null() {
		Retriever retriever = new MessageRetriever();

		Optional<MessageRecord> messageRecord = retriever.getRecord(MessageId.ENABLED_MESSAGE.name(), languageQueryHandlerMock);
		assertNotNull(messageRecord);
		assertFalse(messageRecord.isPresent());
	}

	@Test
	void getMessageRecord_parameter_null_Id() {
		Retriever retriever = new MessageRetriever();
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> retriever.getRecord(null, languageQueryHandlerMock));

		// Assert
		assertEquals("The parameter 'messageId' cannot be null.", exception.getMessage());
	}

	@Test
	void getRecord_parameter_null_languageQueryHandler() {
		Retriever retriever = new MessageRetriever();
		LocalizedException exception = assertThrows(LocalizedException.class,
				() -> retriever.getRecord(MessageId.ENABLED_MESSAGE.name(), null));

		// Assert
		assertEquals("The parameter 'languageQueryHandler' cannot be null.", exception.getMessage());
	}

}