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

package com.winterhavenmc.util.messagebuilder.pipeline.retriever;

import com.winterhavenmc.util.messagebuilder.messages.MessageId;
import com.winterhavenmc.util.messagebuilder.resources.QueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.RecordKey;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlConfigurationSupplier;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.MessageRecord;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.ValidMessageRecord;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.section.MessageSectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.validation.ValidationException;
import com.winterhavenmc.util.messagebuilder.util.MockUtility;

import org.bukkit.configuration.file.FileConfiguration;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class MessageRetrieverTest
{
	@Test @DisplayName("Test getRecord method with valid parameters")
	void getRecord()
	{
		// Arrange
		FileConfiguration configuration = MockUtility.loadConfigurationFromResource("language/en-US.yml");
		YamlConfigurationSupplier configurationSupplier = new YamlConfigurationSupplier(configuration);
		RecordKey recordKey = RecordKey.of(MessageId.ENABLED_MESSAGE).orElseThrow();
		QueryHandler<MessageRecord> queryHandler = new MessageSectionQueryHandler(configurationSupplier);

		Retriever retriever = new MessageRetriever(queryHandler);

		// Act
		MessageRecord messageRecord = retriever.getRecord(recordKey);

		// Assert
		assertNotNull(messageRecord);
		assertInstanceOf(ValidMessageRecord.class, messageRecord);
		assertEquals(recordKey, messageRecord.key());
	}


	@Test
	@Disabled("Null queryHandler should be impossible.")
	void testConstructor_parameter_null_query_handler()
	{
		// Arrange & Act
		ValidationException exception = assertThrows(ValidationException.class,
				() -> new MessageRetriever(null));

		// Assert
		assertEquals("The parameter 'queryHandler' cannot be null.", exception.getMessage());
	}

}
