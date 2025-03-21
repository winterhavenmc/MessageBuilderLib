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
import com.winterhavenmc.util.messagebuilder.resources.QueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlConfigurationSupplier;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.messages.MessageRecord;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.messages.MessageSectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;
import com.winterhavenmc.util.messagebuilder.util.MockUtility;

import org.bukkit.configuration.file.FileConfiguration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class MessageRetrieverTest {

	@Mock QueryHandler<MessageRecord> queryHandlerMock;

	@AfterEach
	void tearDown() {
		this.queryHandlerMock = null;
	}


	@Test
	void getRecord() {
		// Arrange
		FileConfiguration configuration = MockUtility.loadConfigurationFromResource("language/en-US.yml");
		YamlConfigurationSupplier configurationSupplier = new YamlConfigurationSupplier(configuration);
		QueryHandler<MessageRecord> queryHandler = new MessageSectionQueryHandler(configurationSupplier);
		Retriever retriever = new MessageRetriever(queryHandler);

		// Act
		Optional<MessageRecord> messageRecord = retriever.getRecord(MessageId.ENABLED_MESSAGE.name());

		// Assert
		assertNotNull(messageRecord);
		assertTrue(messageRecord.isPresent());
		assertEquals("This is an enabled message", messageRecord.get().message());
	}

	@Test
	void getRecord_section_query_handler_null() {
		Retriever retriever = new MessageRetriever(queryHandlerMock);

		Optional<MessageRecord> messageRecord = retriever.getRecord(MessageId.ENABLED_MESSAGE.name());
		assertNotNull(messageRecord);
		assertFalse(messageRecord.isPresent());
	}

	@Test
	void getMessageRecord_parameter_null_Id() {
		Retriever retriever = new MessageRetriever(queryHandlerMock);
		ValidationException exception = assertThrows(ValidationException.class,
				() -> retriever.getRecord(null));

		// Assert
		assertEquals("The parameter 'key' cannot be null.", exception.getMessage());
	}

}
