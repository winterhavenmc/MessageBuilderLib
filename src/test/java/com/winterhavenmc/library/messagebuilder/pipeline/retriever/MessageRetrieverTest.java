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

package com.winterhavenmc.library.messagebuilder.pipeline.retriever;

import com.winterhavenmc.library.messagebuilder.messages.MessageId;
import com.winterhavenmc.library.messagebuilder.keys.RecordKey;
import com.winterhavenmc.library.messagebuilder.query.QueryHandler;
import com.winterhavenmc.library.messagebuilder.model.language.InvalidMessageRecord;
import com.winterhavenmc.library.messagebuilder.model.language.MessageRecord;
import com.winterhavenmc.library.messagebuilder.model.language.ValidMessageRecord;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class MessageRetrieverTest
{
	@Mock QueryHandler<MessageRecord> queryHandlerMock;
	@Mock ValidMessageRecord validMessageRecordMock;
	@Mock InvalidMessageRecord invalidMessageRecord;

	Retriever retriever;


	@BeforeEach
	void setUp()
	{
		retriever = new MessageRetriever(queryHandlerMock);
	}


	@Test @DisplayName("Test getRecord method with valid parameters")
	void getRecord()
	{
		// Arrange
		RecordKey messageKey = RecordKey.of(MessageId.ENABLED_MESSAGE).orElseThrow();
		when(queryHandlerMock.getRecord(messageKey)).thenReturn(validMessageRecordMock);

		// Act
		MessageRecord messageRecord = retriever.getRecord(messageKey);

		// Assert
		assertInstanceOf(ValidMessageRecord.class, messageRecord);
	}


	@Test @DisplayName("Test getRecord method with nonexistent entry")
	void getRecord_nonexistent()
	{
		// Arrange
		RecordKey messageKey = RecordKey.of(MessageId.NONEXISTENT_ENTRY).orElseThrow();
		when(queryHandlerMock.getRecord(messageKey)).thenReturn(invalidMessageRecord);

		// Act
		MessageRecord messageRecord = retriever.getRecord(messageKey);

		// Assert
		assertInstanceOf(InvalidMessageRecord.class, messageRecord);
	}

}
