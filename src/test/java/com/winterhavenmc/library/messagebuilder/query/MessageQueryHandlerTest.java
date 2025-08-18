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

package com.winterhavenmc.library.messagebuilder.query;

import com.winterhavenmc.library.messagebuilder.keys.MessageKey;
import com.winterhavenmc.library.messagebuilder.keys.ValidMessageKey;
import com.winterhavenmc.library.messagebuilder.model.language.InvalidMessageRecord;
import com.winterhavenmc.library.messagebuilder.model.language.MessageRecord;
import com.winterhavenmc.library.messagebuilder.model.language.ValidMessageRecord;
import com.winterhavenmc.library.messagebuilder.resources.language.SectionProvider;

import com.winterhavenmc.library.messagebuilder.validation.ValidationException;
import org.bukkit.configuration.ConfigurationSection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.winterhavenmc.library.messagebuilder.messages.MessageId.ENABLED_MESSAGE;
import static com.winterhavenmc.library.messagebuilder.messages.MessageId.NONEXISTENT_ENTRY;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MessageQueryHandlerTest
{
	@Mock ConfigurationSection messageSectionMock;
	@Mock ConfigurationSection messageEntryMock;


	@Test
	void constructor_with_null_parameter_throws_ValidationException()
	{
		// Arrange & Act
		ValidationException exception = assertThrows(ValidationException.class,
				() -> new MessageQueryHandler(null));

		// Assert
		assertEquals("The parameter 'sectionSupplier' cannot be null.", exception.getMessage());
	}


	@Test
	void getRecord_with_valid_parameter_returns_ValidMessageRecord_type()
	{
		// Arrange
		ValidMessageKey queryKey = MessageKey.of(ENABLED_MESSAGE).isValid().orElseThrow();

		when(messageSectionMock.getConfigurationSection(ENABLED_MESSAGE.name())).thenReturn(messageEntryMock);

		when(messageEntryMock.contains(anyString())).thenReturn(true);
		when(messageEntryMock.getString(MessageRecord.Field.MESSAGE_TEXT.toKey())).thenReturn("Enabled message.");
		when(messageEntryMock.getBoolean(MessageRecord.Field.ENABLED.toKey())).thenReturn(true);
		when(messageEntryMock.getLong(MessageRecord.Field.REPEAT_DELAY.toKey())).thenReturn(0L);
		when(messageEntryMock.getString(MessageRecord.Field.TITLE_TEXT.toKey())).thenReturn("Enabled title.");
//		when(messageEntryMock.getInt(MessageRecord.Field.TITLE_FADE_IN.toKey())).thenReturn(0);
//		when(messageEntryMock.getInt(MessageRecord.Field.TITLE_STAY.toKey())).thenReturn(0);
//		when(messageEntryMock.getInt(MessageRecord.Field.TITLE_FADE_OUT.toKey())).thenReturn(0);
		when(messageEntryMock.getString(MessageRecord.Field.SUBTITLE_TEXT.toKey())).thenReturn("Subtitle text.");

		SectionProvider mockProvider = () -> messageSectionMock;
		MessageQueryHandler handler = new MessageQueryHandler(mockProvider);

		// Act
		ValidMessageRecord result = (ValidMessageRecord) handler.getRecord(queryKey);

		// Assert
		assertInstanceOf(ValidMessageRecord.class, result);
		assertEquals("Enabled message.", result.message());

		// Verify
		verify(messageSectionMock, atLeastOnce()).getConfigurationSection("ENABLED_MESSAGE");
		verify(messageEntryMock, times(3)).getString(anyString());
//		verify(messageEntryMock, times(3)).getInt(anyString());
//		verify(messageEntryMock, times(1)).getLong(anyString());
		verify(messageEntryMock, times(1)).getBoolean(anyString());
	}


	@Test
	void getRecord_with_nonexistent_entry_returns_InvalidMessageRecord_type()
	{
		// Arrange
		ValidMessageKey messageKey = MessageKey.of(NONEXISTENT_ENTRY).isValid().orElseThrow();
		when(messageSectionMock.getConfigurationSection(NONEXISTENT_ENTRY.name())).thenReturn(null);

		SectionProvider mockProvider = () -> messageSectionMock;
		MessageQueryHandler handler = new MessageQueryHandler(mockProvider);


		// Act
		MessageRecord messageRecord = handler.getRecord(messageKey);

		// Assert
		assertInstanceOf(InvalidMessageRecord.class, messageRecord);

		// Verify
		verify(messageSectionMock, atLeastOnce()).getConfigurationSection("NONEXISTENT_ENTRY");
	}

}
