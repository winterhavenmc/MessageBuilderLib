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

package com.winterhavenmc.library.messagebuilder.adapters.pipeline.retrievers;

import com.winterhavenmc.library.messagebuilder.adapters.util.MessageId;
import com.winterhavenmc.library.messagebuilder.core.ports.resources.language.MessageRepository;
import com.winterhavenmc.library.messagebuilder.models.keys.MessageKey;
import com.winterhavenmc.library.messagebuilder.models.keys.ValidMessageKey;
import com.winterhavenmc.library.messagebuilder.models.language.InvalidMessageRecord;
import com.winterhavenmc.library.messagebuilder.models.language.MessageRecord;
import com.winterhavenmc.library.messagebuilder.models.language.ValidMessageRecord;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class LocalizedMessageRetrieverTest
{
	@Mock MessageRepository messageRepositoryMock;
	@Mock ValidMessageRecord validMessageRecordMock;
	@Mock InvalidMessageRecord invalidMessageRecord;

	LocalizedMessageRetriever retriever;


	@BeforeEach
	void setUp()
	{
		retriever = new LocalizedMessageRetriever(messageRepositoryMock);
	}


	@Test @DisplayName("Test getRecord method with valid parameters")
	void getRecord_with_valid_parameters()
	{
		// Arrange
		ValidMessageKey messageKey = MessageKey.of(MessageId.ENABLED_MESSAGE).isValid().orElseThrow();
		when(messageRepositoryMock.getMessageRecord(messageKey)).thenReturn(validMessageRecordMock);

		// Act
		MessageRecord messageRecord = retriever.getRecord(messageKey);

		// Assert
		assertInstanceOf(ValidMessageRecord.class, messageRecord);
	}


	@Test @DisplayName("Test getRecord method with nonexistent entry")
	void getRecord_nonexistent_entry()
	{
		// Arrange
		ValidMessageKey messageKey = MessageKey.of(MessageId.NONEXISTENT_ENTRY).isValid().orElseThrow();
		when(messageRepositoryMock.getMessageRecord(messageKey)).thenReturn(invalidMessageRecord);

		// Act
		MessageRecord messageRecord = retriever.getRecord(messageKey);

		// Assert
		assertInstanceOf(InvalidMessageRecord.class, messageRecord);
	}

}
