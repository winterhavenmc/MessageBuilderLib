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

package com.winterhavenmc.util.messagebuilder.model.language;

import com.winterhavenmc.util.messagebuilder.keys.RecordKey;
import com.winterhavenmc.util.messagebuilder.model.language.message.InvalidMessageRecord;
import com.winterhavenmc.util.messagebuilder.model.language.message.MessageRecord;
import com.winterhavenmc.util.messagebuilder.model.language.message.ValidMessageRecord;
import com.winterhavenmc.util.messagebuilder.resources.MessageQueryHandler;
import com.winterhavenmc.util.messagebuilder.resources.language.yaml.SectionProvider;

import org.bukkit.configuration.ConfigurationSection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.winterhavenmc.util.messagebuilder.messages.MessageId.ENABLED_MESSAGE;
import static com.winterhavenmc.util.messagebuilder.messages.MessageId.NONEXISTENT_ENTRY;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class MessageQueryHandlerTest
{
	@Mock ConfigurationSection messageSectionMock;
	@Mock ConfigurationSection messageEntryMock;


	@Test
	void testConstructor_parameter_null()
	{
		// Arrange & Act
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> new MessageQueryHandler(null));

		// Assert
		assertEquals("The parameter 'sectionSupplier' cannot be null.", exception.getMessage());
	}


	@Test
	void testGetRecord_parameter_valid() {
		// Arrange
		RecordKey queryKey = RecordKey.of(ENABLED_MESSAGE).orElseThrow();

		when(messageSectionMock.getConfigurationSection(ENABLED_MESSAGE.name())).thenReturn(messageEntryMock);

		when(messageEntryMock.getString(MessageRecord.Field.MESSAGE_TEXT.toKey())).thenReturn("Enabled message.");
		when(messageEntryMock.getBoolean(MessageRecord.Field.ENABLED.toKey())).thenReturn(true);
		when(messageEntryMock.getLong(MessageRecord.Field.REPEAT_DELAY.toKey())).thenReturn(0L);
		when(messageEntryMock.getString(MessageRecord.Field.TITLE_TEXT.toKey())).thenReturn("Enabled title.");
		when(messageEntryMock.getInt(MessageRecord.Field.TITLE_FADE_IN.toKey())).thenReturn(0);
		when(messageEntryMock.getInt(MessageRecord.Field.TITLE_STAY.toKey())).thenReturn(0);
		when(messageEntryMock.getInt(MessageRecord.Field.TITLE_FADE_OUT.toKey())).thenReturn(0);
		when(messageEntryMock.getString(MessageRecord.Field.SUBTITLE_TEXT.toKey())).thenReturn("Subtitle text.");

		SectionProvider mockProvider = () -> messageSectionMock;
		MessageQueryHandler handler = new MessageQueryHandler(mockProvider);

		// Act
		ValidMessageRecord result = (ValidMessageRecord) handler.getRecord(queryKey);

		// Assert
		assertInstanceOf(ValidMessageRecord.class, result);
		assertEquals("Enabled message.", result.message());
	}


	@Test
	void testGetRecord_nonexistent_entry()
	{
		// Arrange
		RecordKey messageKey = RecordKey.of(NONEXISTENT_ENTRY).orElseThrow();
		when(messageSectionMock.getConfigurationSection(NONEXISTENT_ENTRY.name())).thenReturn(null);

		SectionProvider mockProvider = () -> messageSectionMock;
		MessageQueryHandler handler = new MessageQueryHandler(mockProvider);


		// Act
		MessageRecord messageRecord = handler.getRecord(messageKey);

		// Assert
		assertInstanceOf(InvalidMessageRecord.class, messageRecord);
	}

}
