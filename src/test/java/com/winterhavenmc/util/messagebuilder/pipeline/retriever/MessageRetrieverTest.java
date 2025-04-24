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
import com.winterhavenmc.util.messagebuilder.keys.RecordKey;
import com.winterhavenmc.util.messagebuilder.model.language.MessageSectionQueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.QueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.YamlConfigurationSupplier;
import com.winterhavenmc.util.messagebuilder.model.language.message.InvalidMessageRecord;
import com.winterhavenmc.util.messagebuilder.model.language.message.MessageRecord;
import com.winterhavenmc.util.messagebuilder.model.language.message.ValidMessageRecord;
import com.winterhavenmc.util.messagebuilder.util.MockUtility;

import org.bukkit.configuration.file.FileConfiguration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class MessageRetrieverTest
{
	RecordKey messageKey;
	FileConfiguration configuration;
	YamlConfigurationSupplier configurationSupplier;
	QueryHandler<MessageRecord> queryHandler;
	Retriever retriever;


	@BeforeEach
	void setUp()
	{
		messageKey = RecordKey.of(MessageId.ENABLED_MESSAGE).orElseThrow();
		configuration = MockUtility.loadConfigurationFromResource("language/en-US.yml");
		configurationSupplier = new YamlConfigurationSupplier(configuration);
		queryHandler = new MessageSectionQueryHandler(configurationSupplier);
		retriever = new MessageRetriever(queryHandler);

	}


	@Test @DisplayName("Test getRecord method with valid parameters")
	void getRecord()
	{
		// Arrange & Act
		MessageRecord messageRecord = retriever.getRecord(messageKey);

		// Assert
		assertInstanceOf(ValidMessageRecord.class, messageRecord);
	}


	@Test @DisplayName("Test getRecord method with nonexistent entry")
	void getRecord_nonexistent()
	{
		// Arrange
		RecordKey recordKey = RecordKey.of(MessageId.NONEXISTENT_ENTRY).orElseThrow();

		// Act
		MessageRecord messageRecord = retriever.getRecord(recordKey);

		// Assert
		assertInstanceOf(InvalidMessageRecord.class, messageRecord);
	}

}
